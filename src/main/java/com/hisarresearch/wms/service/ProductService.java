package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.domain.ProductId;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.repository.ProductRepository;
import com.hisarresearch.wms.service.criteria.ProductCriteria;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.MikroStockListByDepoListDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.dto.product.ProductWithAmountDTO;
import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;
import com.hisarresearch.wms.service.erp.ErpTokenService;
import com.hisarresearch.wms.service.mapper.ProductWithoutAddressMapper;
import com.hisarresearch.wms.service.erp.UyumsoftService;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductAddressService productAddressService;

    @Autowired
    private ProductWithoutAddressMapper productWithoutAddressMapper;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private ErpTokenService erpTokenService;


    private final ProductRepository productRepository;

    private final ProductQueryService productQueryService;

    private final HttpService httpService;

    private final UserService userService;


    private final AurLogService logService;

    private final CompanyService companyService;

    private final UyumsoftService uyumsoftService;

    public ProductService(ProductRepository productRepository, ProductQueryService productQueryService,
                          HttpService httpService, UserService userService, AurLogService logService,
                          CompanyService companyService, UyumsoftService uyumsoftService) {
        this.productRepository = productRepository;
        this.productQueryService = productQueryService;
        this.httpService = httpService;
        this.userService = userService;
        this.logService = logService;
        this.companyService = companyService;
        this.uyumsoftService = uyumsoftService;
    }

    public List<ProductWithAmountDTO> getProductsWithAmount(ProductCriteria productCriteria, Pageable pageable) throws Exception {
        long logId = logService.logRequest("getProductsWithAmount", "stock-via-amount", productCriteria.toString());
        Page<Product> products = productQueryService.findByCriteria(productCriteria, pageable);
        logService.logResponse(logId, products.toString());

        if (productCriteria.getDepoCode() != null) {
            List<String> availableErpCodes = new ArrayList<>();
            availableErpCodes.add(ErpConnectionType.MIKRO_V15.getErpCodeAsString());
            availableErpCodes.add(ErpConnectionType.UYUMSOFT.getErpCodeAsString());
            if (productCriteria.getCompanyCode() != null && productCriteria.getCompanyCode().getEquals() != null ) {
                AurCompanyDTO companyDTO = companyService.findByCompanyCode(productCriteria.getCompanyCode().getEquals());
                if(availableErpCodes.contains(companyDTO.getErpTipi())){
                    String depoCode = productCriteria.getDepoCode().getEquals();
                    List<Integer> depoList = new ArrayList<>();
                    depoList.add(Integer.valueOf(depoCode));
                    return fillErpStockAmountFromErp(depoList, products, depoCode,companyDTO.getErpTipi());
                }
            }
            List<ProductWithAmountDTO> result = new ArrayList<>();
            products.forEach(product -> {
                ProductWithAmountDTO productWithAmountDTO = new ProductWithAmountDTO();
                productWithAmountDTO.setProduct(product);
                result.add(productWithAmountDTO);
            });
            return result;

        }

        throw new BadRequestException("Depo code not found");

    }

    public List<ProductWithAmountDTO> fillErpStockAmountFromErp(List<Integer> depoList, Page<Product> productList, String depoCode,String erpType) throws Exception {
        List<ProductWithAmountDTO> productDTOS = new ArrayList<>();
        RequestDto requestDto = new RequestDto();
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = erpTokenService.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        long logId = logService.logRequest("fillErpStockAmountFromErp", "stock-info", productList.getContent().toString());

        List<String> distinctStockCodes = productList.getContent()
            .stream()
            .map(Product::getStokKodu)
            .distinct()
            .collect(Collectors.toList());
        if(erpType.equals(ErpConnectionType.MIKRO_V15.getErpCodeAsString())){
            requestDto.setServiceName("depoService.stokListByDepoList");
            MikroStockListByDepoListDTO data = new MikroStockListByDepoListDTO(depoList, distinctStockCodes);
            requestDto.setData(data);
            List<LinkedHashMap> response = (List<LinkedHashMap>) httpService.executeService(token, aurCompanyDto.getApiEndPoint(), requestDto);
            for (Product product : productList.getContent()) {
                logService.logResponse(logId, product.toString());
                Optional<LinkedHashMap> searchProduct = response.stream().filter(item -> item.get("stokKodu").equals(product.getStokKodu())).findFirst();
                if (searchProduct.isPresent() && searchProduct.get().get("stokMiktar") != null) {
                    logService.logResponse(logId, searchProduct.get().toString());
                    Object stokMiktarObject = searchProduct.get().get("stokMiktar");
                    Double stokMiktar = null;

                    if (stokMiktarObject instanceof Double) {
                        stokMiktar = (Double) stokMiktarObject;
                    } else if (stokMiktarObject instanceof Integer) {
                        stokMiktar = ((Integer) stokMiktarObject).doubleValue();
                    }

                    if (stokMiktar != null && product != null) {
                        ProductWithAmountDTO productDTO = new ProductWithAmountDTO();
                        product.setMiktar(stokMiktar);
                        productDTO.setProduct(product);
                        productDTO.setDepoAmount(productAddressService.getAmountOfProductAtDepo(product.getId().getBarkod(), depoCode, product.getId().getCompanyCode()));
                        productDTOS.add(productDTO);
                    }
                }

            }
        }
        if(erpType.equals(ErpConnectionType.UYUMSOFT.getErpCodeAsString())){
            Map<String,Object> params = new HashMap<>();
            params.put("stockList", distinctStockCodes);
            List<String> formattedDepoList = depoList.stream()
                .map(depo -> depo.toString().replaceAll("(\\d{2})(\\d{2})", "$1 $2"))
                .collect(Collectors.toList());

            params.put("depoList", formattedDepoList);
            params.put("firmCode","2022C");
            params.put("productCode","INT1");
            params.put("aktifPasif","Aktif");
            params.put("teminKod","DEPO1");

            requestDto.setServiceName("depoService.getStokMiktarBulk");
            requestDto.setData(params);
            List<LinkedHashMap> response = (List<LinkedHashMap>) uyumsoftService.executeServiceWrapper(token, aurCompanyDto.getApiEndPoint(), requestDto);
            for (Product product : productList.getContent()) {
                logService.logResponse(logId, product.toString());
                Optional<LinkedHashMap> searchProduct = response.stream().filter(item -> item.get("stok_kod").equals(product.getStokKodu())).findFirst();
                if (searchProduct.isPresent() && searchProduct.get().get("stok_miktar") != null) {
                    logService.logResponse(logId, searchProduct.get().toString());
                    Object stokMiktarObject = searchProduct.get().get("stok_miktar");
                    Double stokMiktar = null;

                    if (stokMiktarObject instanceof Double) {
                        stokMiktar = (Double) stokMiktarObject;
                    } else if (stokMiktarObject instanceof Integer) {
                        stokMiktar = ((Integer) stokMiktarObject).doubleValue();
                    } else if (stokMiktarObject instanceof String) {
                        stokMiktar = Double.parseDouble((String) stokMiktarObject);
                    }

                    if (stokMiktar != null && product != null) {
                        ProductWithAmountDTO productDTO = new ProductWithAmountDTO();
                        product.setMiktar(stokMiktar);
                        productDTO.setProduct(product);
                        productDTO.setDepoAmount(productAddressService.getAmountOfProductAtDepo(product.getId().getBarkod(), depoCode, product.getId().getCompanyCode()));
                        productDTOS.add(productDTO);
                    }
                }

            }


        }

        return productDTOS;
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(String barcode, String companyCode) {
        ProductId id = new ProductId(barcode, companyCode);
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Map<String, Product> findByBarcodes(List<String> barcodes, String companyCode) {
        if (barcodes == null || barcodes.isEmpty()) {
            return Collections.emptyMap();
        }
        return productRepository.findByIdBarkodInAndIdCompanyCode(barcodes, companyCode).stream()
            .collect(Collectors.toMap(product -> product.getId().getBarkod(), product -> product, (a, b) -> a));
    }

    public Product saveProduct(Product searchProduct){
        Optional<Product> product = productRepository.findById(searchProduct.getId());
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setPhysicalAttributes(searchProduct.getPhysicalAttributes());
            existingProduct.setMiktar(searchProduct.getMiktar());
            existingProduct.setDescription(searchProduct.getDescription());
            existingProduct.setStokBirimi(searchProduct.getStokBirimi());
            return productRepository.save(existingProduct);
        }
        return productRepository.save(searchProduct);
    }

    @Transactional
    public void updatePhysicalAttributes(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return;
        }
        productRepository.saveAll(products);
    }

    @Transactional
    public ProductWithoutAddressDTO create(ProductWithoutAddressDTO dto) {
        if (findById(dto.getBarcode(), dto.getCompanyCode()).isPresent()) {
            throw new BadRequestAlertException(translationService.getErrorMessage("product.alreadyExists"), "product", "productAlreadyExists");
        }
        Product product = productWithoutAddressMapper.toEntity(dto);
        return productWithoutAddressMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductWithoutAddressDTO partialUpdate(ProductWithoutAddressDTO dto) {
        Product existingProduct = findById(dto.getBarcode(), dto.getCompanyCode())
            .orElseThrow(() -> new BadRequestAlertException(translationService.getErrorMessage("product.notFound"), "product", "productNotFound"));
        productWithoutAddressMapper.partialUpdateAllowedFields(existingProduct, dto);
        return productWithoutAddressMapper.toDto(productRepository.save(existingProduct));
    }



}
