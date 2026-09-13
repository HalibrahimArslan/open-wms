package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.repository.ProductAddressv2Repository;
import com.hisarresearch.wms.service.criteria.ProductAddressCriteria;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.MikroStockListByDepoListDTO;
import com.hisarresearch.wms.service.dto.address.AurDepoStockCodeDto;
import com.hisarresearch.wms.service.dto.address.AurDepoStokUrunAdresDto;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.erp.ErpTokenService;
import com.hisarresearch.wms.service.picking.ProductAddressStrategy;
import com.hisarresearch.wms.service.picking.ProductAddressStrategyFactory;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductAddressService {
    private final Logger log = LoggerFactory.getLogger(ProductAddressService.class);
    private static final String ENTITY_NAME = "productAddress";

    private final ProductAddressQueryService productAddressQueryService;

    private final ErpTokenService erpTokenService;

    private final UserService userService;

    private final HttpService httpService;

    private final ProductAddressv2Repository productAddressv2Repository;

    private final ProductAddressStrategyFactory productAddressStrategyFactory;

    private final AddressService addressService;

    public ProductAddressService(ProductAddressQueryService productAddressQueryService,
                                 ErpTokenService erpTokenService, UserService userService,
                                 HttpService httpService, ProductAddressv2Repository productAddressv2Repository,
                                 ProductAddressStrategyFactory productAddressStrategyFactory, AddressService addressService) {
        this.productAddressQueryService = productAddressQueryService;
        this.erpTokenService = erpTokenService;
        this.userService = userService;
        this.httpService = httpService;
        this.productAddressv2Repository = productAddressv2Repository;
        this.productAddressStrategyFactory = productAddressStrategyFactory;
        this.addressService = addressService;
    }

    public List<ProductAddressv2> findAll(ProductAddressCriteria criteria, Pageable pageable) throws Exception {
        log.debug("Request to get all ProductAddressv2");
        Page<ProductAddressv2> productAddress = productAddressQueryService.findByCriteria(criteria, pageable);
        if (criteria.getDepoCode() != null && criteria.getCompanyCode() != null && criteria.getCompanyCode().getEquals().equals("4")) {
            String depoCode = criteria.getDepoCode().getEquals();
            List<Integer> depoList = new ArrayList<>();
            depoList.add(Integer.valueOf(depoCode));

            return fillErpStockAmountFromErp(depoList, productAddress);
        }

        return productAddress.getContent();
    }

    public List<ProductAddressv2> fillErpStockAmountFromErp(List<Integer> depoList, Page<ProductAddressv2> productAddress) throws Exception {
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        List<String> distinctStockCodes = productAddress.getContent()
            .stream()
            .map(ProductAddressv2::getStokKod)
            .distinct()
            .collect(Collectors.toList());
        String token = erpTokenService.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        RequestDto requestDto = new RequestDto();
        requestDto.setServiceName("depoService.stokListByDepoList");
        MikroStockListByDepoListDTO data = new MikroStockListByDepoListDTO(depoList, distinctStockCodes);
        requestDto.setData(data);
        List<LinkedHashMap> response = (List<LinkedHashMap>) httpService.executeService(token, aurCompanyDto.getApiEndPoint(), requestDto);
        for (ProductAddressv2 productAddressv2 : productAddress.getContent()) {
            Optional<LinkedHashMap> searchProduct = response.stream().filter(item -> item.get("stokKodu").equals(productAddressv2.getStokKod())).findFirst();
            if (searchProduct.isPresent() && searchProduct.get().get("stokMiktar") != null) {
                Object stokMiktarObject = searchProduct.get().get("stokMiktar");
                Double stokMiktar = null;

                if (stokMiktarObject instanceof Double) {
                    stokMiktar = (Double) stokMiktarObject;
                } else if (stokMiktarObject instanceof Integer) {
                    stokMiktar = ((Integer) stokMiktarObject).doubleValue();
                }

                if (stokMiktar != null && productAddressv2.getProduct() != null) {
                    productAddressv2.getProduct().setMiktar(stokMiktar);
                }
            }
        }

        return productAddress.getContent();

    }

    public double getAmountOfProductAtDepo(String barcode, String depoNo, String companyCode) {
        List<ProductAddressv2> productAddressv2List = productAddressv2Repository.findByDepoCodeAndCompanyCodeAndProduct_Id_BarkodAndStatus(depoNo, companyCode, barcode, true);
        return productAddressv2List.stream().mapToDouble(ProductAddressv2::getMiktar).sum();
    }

    public List<AurDepoUrunAdres> getActiveAddressList(String depoCode,String companyCode) {
        List<ProductAddressv2> productAddresses = productAddressv2Repository.findDistinctByStatusTrueAndDepoCodeAndCompanyCode(depoCode, companyCode);
        return productAddresses.stream()
            .map(ProductAddressv2::getUrunAdres)
            .distinct()
            .collect(Collectors.toList());

    }

    public List<AurDepoUrunAdres> getActiveAddressListByRayon(String depoCode,String companyCode,String rayon) {
        List<ProductAddressv2> productAddresses = productAddressv2Repository.findDistinctByStatusTrueAndDepoCodeAndCompanyCodeAndUrunAdres_Reyon(depoCode, companyCode,rayon);
        return productAddresses.stream()
            .map(ProductAddressv2::getUrunAdres)
            .distinct()
            .collect(Collectors.toList());

    }

    public void deleteProductAddressAndSktList(String depoCode, String companyCode) {
        productAddressv2Repository.findByDepoCodeAndCompanyCodeAndStatusTrue(depoCode, companyCode).forEach(productAddress -> {
            productAddress.setStatus(false);
            productAddress.getProductAddressSktList().forEach(productAddressSkt -> productAddressSkt.setStatus(false));
        });
    }

    public void deleteProductAddressAndSktList(String depoCode, String companyCode, String barcode, AurDepoUrunAdres address) {
        productAddressv2Repository.findByDepoCodeAndCompanyCodeAndProduct_Id_BarkodAndUrunAdres_UrunAdresIdAndStatusTrue(depoCode, companyCode, barcode, address.getUrunAdresId()).forEach(productAddress -> {
            productAddress.setStatus(false);
            productAddress.getProductAddressSktList().forEach(productAddressSkt -> productAddressSkt.setStatus(false));
        });
    }

    public List<AurDepoStockCodeDto> getProductAddressForPicking(AurDepoStokUrunAdresDto dto) {
        List<AurDepoStockCodeDto> result = new ArrayList<>();
        ProductAddressStrategy strategy = productAddressStrategyFactory.getStrategy(dto.getDepoCode());

        dto.getStockList().forEach(stockCode -> {
            List<ProductAddressv2> searchList = strategy.findPickingProductAddresses(dto, stockCode);
            searchList.forEach(searchItem -> {
                AurDepoStockCodeDto saveDto = new AurDepoStockCodeDto();
                saveDto.setAddress(searchItem.getUrunAdres().getAdres());
                saveDto.setStockCode(searchItem.getStokKod());
                saveDto.setMiktar(searchItem.getMiktar());
                saveDto.setBarcode(searchItem.getProduct().getId().getBarkod());
                saveDto.setAdresId(searchItem.getUrunAdres().getUrunAdresId());
                saveDto.setStockName(searchItem.getProduct().getStokAdi());
                result.add(saveDto);
            });

        });
        return result;
    }

    @Transactional
    public void isValidToSave(String depoCode, Long addressId, String barcode) {
        log.debug("Is valid situation to product address save");
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        AurDepoUrunAdres processAddress = addressService.findById(addressId).orElseThrow(InvalidAddressException::new);

        if (!processAddress.getToplamaGozu()) {
            return;
        }

        productAddressv2Repository.findByDepoCodeAndCompanyCodeAndProduct_Id_BarkodAndStatus(depoCode,companyCode,barcode,true)
            .stream()
            .map(ProductAddressv2::getUrunAdres)
            .filter(AurDepoUrunAdres::getToplamaGozu)
            .filter(address -> !Objects.equals(address.getUrunAdresId(), addressId))
            .findFirst()
            .ifPresent(address -> {
                throw new BusinessException("Barkodlu ürün başka toplama gözünde bulunmaktadır", ENTITY_NAME, "multiplePickingAreaProduct");
            });


    }

}
