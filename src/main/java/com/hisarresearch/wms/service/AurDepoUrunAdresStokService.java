package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.domain.enumeration.OrderStatus;
import com.hisarresearch.wms.domain.enumeration.ProductCategoryTypeEnum;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import com.hisarresearch.wms.exception.validation.InvalidProductAddressException;
import com.hisarresearch.wms.exception.validation.InvalidSituationGenericError;
import com.hisarresearch.wms.repository.AurVwDepoStokAdresRepository;
import com.hisarresearch.wms.repository.address.AurDepoAdresRepository;
import com.hisarresearch.wms.repository.address.AurDepoUrunAdresStokRepository;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.service.dto.ProductAddressSaveDTO;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeAddressDTO;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.address.*;
import com.hisarresearch.wms.service.mapper.AurDepoUrunAdresStokMapper;
import com.hisarresearch.wms.service.mapper.ProductAddressMapper;
import com.hisarresearch.wms.service.mapper.ProductAddressDefinitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AurDepoUrunAdresStokService {

    private static final String ENTITY_NAME = "aurDepoUrunAdresStok";

    private final Logger log = LoggerFactory.getLogger(AurDepoUrunAdresStokService.class);

    @Autowired
    private AurDepoUrunAdresStokRepository aurDepoUrunAdresStokRepository;

    @Autowired
    private AurDepoAdresRepository aurDepoAdresRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AurPartialItemService aurPartialItemService;

    @Autowired
    private AurPartialDetailsService aurPartialDetailsService;

    @Autowired
    private ProductAddressMapper productAddressMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressMovementHistoryService addressMovementHistoryService;

    @Autowired
    private AurVwDepoStokAdresRepository aurVwDepoStokAdresRepository;

    @Autowired
    private AurDepoUrunAdresStokMapper aurDepoUrunAdresStokMapper;

    @Autowired
    private ProductAddressSktService productAddressSktService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private AurLogService logService;

    @Autowired
    private ProductAddressDefinitionMapper productAddressDefinitionMapper;

    @Autowired
    private ProductAddressService productAddressService;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    AurOrderMasterService aurOrderMasterService;


    @Transactional
    public Optional<AurDepoUrunAdresStok> findById(Long id) {
        return aurDepoUrunAdresStokRepository.findById(id);

    }

    @Transactional
    public Optional<AurDepoUrunAdresStok> findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(String barcode, Long urunAdresId, Boolean status, String depoCode) {
        return aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, urunAdresId, status, depoCode);
    }

    public void assignProductToAddress(ProductAddressSaveDTO productAddressSaveDTO) throws BadRequestAlertException, InvalidAddressException, InvalidOrderException {
        log.debug("AurDepoUrunAdresStokService service assignProductToAddress is processing by productAddressSaveDTO : {} ", productAddressSaveDTO);
        Optional<AurPartialItemDTO> partialItemDTO = aurPartialItemService.findOneByBarcode(productAddressSaveDTO.getBarcode());

        if (partialItemDTO.isPresent() && productAddressSaveDTO.getCheckPartial() != null && productAddressSaveDTO.getCheckPartial()) {
            List<AurPartialDetails> detailsList = aurPartialDetailsService.findByAurPartialItemId(partialItemDTO.get().getId());
            for (AurPartialDetails apd : detailsList) {
                productAddressSaveDTO.setBarcode(apd.getBarcode());
                productAddressSaveDTO.setStokKod(apd.getStockCode());
                saveProductAddress(productAddressSaveDTO);
            }

        } else {
            saveProductAddress(productAddressSaveDTO);
        }
    }

    public AurDepoUrunAdresStok saveProductAddress(ProductAddressSaveDTO productAddressSaveDTO) throws BadRequestAlertException, InvalidAddressException, InvalidOrderException {
        long logId = logService.logRequest("saveProductAddress", ENTITY_NAME, productAddressSaveDTO.toString());
        Optional<AurDepoUrunAdresStok> desiredItem;
        AurDepoUrunAdresStok aurDepoUrunAdresStok;

        AurDepoUrunAdres searchAddress = aurDepoAdresRepository.
            findByAdresAndDepoNoAndCompanyCodeAndStatus(productAddressSaveDTO.getUrunAdres(),
                productAddressSaveDTO.getDepoCode(),
                productAddressSaveDTO.getCompanyCode(),
                true).orElseThrow(() -> new BadRequestAlertException("Adres yok", ENTITY_NAME, "zeroCheck"));


        desiredItem = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(
            productAddressSaveDTO.getBarcode(),
            searchAddress.getUrunAdresId(),
            true,
            productAddressSaveDTO.getDepoCode());

        if (warehouseService.hasUniquePickingAddress(productAddressSaveDTO.getDepoCode(), productAddressSaveDTO.getCompanyCode())) {
            productAddressService.isValidToSave(productAddressSaveDTO.getDepoCode(), searchAddress.getUrunAdresId(), productAddressSaveDTO.getBarcode());
        }

        if (productAddressSaveDTO.getMiktar() < 0) {
            throw new BadRequestAlertException("Sıfır hatası", ENTITY_NAME, "zeroCheck");
        }

        if (desiredItem.isPresent()) {
            desiredItem.get().setMiktar(desiredItem.get().getMiktar() + productAddressSaveDTO.getMiktar());
            aurDepoUrunAdresStok = desiredItem.get();
        } else {
            Optional<AurOrderMaster> aurOrderMaster = aurOrderMasterService.findByOrderInfo(productAddressSaveDTO.getOrderNo());
            Long paletBarkodId = aurOrderMaster.map(AurOrderMaster::getId).orElse(1L);
            AurDepoUrunAdresStokDto stockDto = new AurDepoUrunAdresStokDto();
            stockDto.setBarcode(productAddressSaveDTO.getBarcode());
            stockDto.setUrunAdresId(searchAddress.getUrunAdresId());
            stockDto.setCompanyCode(productAddressSaveDTO.getCompanyCode());
            stockDto.setDepoCode(productAddressSaveDTO.getDepoCode());
            stockDto.setStokKod(productAddressSaveDTO.getStokKod());
            stockDto.setMiktar(productAddressSaveDTO.getMiktar());
            stockDto.setPaletBarkodId(paletBarkodId);
            stockDto.setBarkodTipi(productAddressSaveDTO.getBarkodTipi());
            aurDepoUrunAdresStok = createAddressStockRecord(stockDto);
        }
        saveProductInfo(productAddressSaveDTO.getBarcode(), productAddressSaveDTO.getCompanyCode(),
            productAddressSaveDTO.getStokKod(), productAddressSaveDTO.getStokAdi());
        saveProductAddressSkt(aurDepoUrunAdresStok, productAddressSaveDTO.getSktDateList());
        logService.logResponse(logId, "success");
        return aurDepoUrunAdresStok;
    }

    public void upsertUniqueBarcodeTotalToAddress(UniqueBarcodeAddressDTO dto, double totalQuantity) {
        Optional<AurDepoUrunAdresStok> existing = aurDepoUrunAdresStokRepository
            .findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(dto.getErpBarkod(), dto.getAddressId(), true, dto.getDepoCode());
        if (existing.isPresent()) {
            existing.get().setMiktar(totalQuantity);
            aurDepoUrunAdresStokRepository.save(existing.get());
        } else {
            AurDepoUrunAdresStokDto stockDto = new AurDepoUrunAdresStokDto();
            stockDto.setBarcode(dto.getErpBarkod());
            stockDto.setUrunAdresId(dto.getAddressId());
            stockDto.setCompanyCode(dto.getCompanyCode());
            stockDto.setDepoCode(dto.getDepoCode());
            stockDto.setStokKod(dto.getStokKod());
            stockDto.setMiktar(totalQuantity);
            stockDto.setPaletBarkodId(1L);
            createAddressStockRecord(stockDto);
            saveProductInfo(dto.getErpBarkod(), dto.getCompanyCode(), dto.getStokKod(), dto.getStokAdi());
        }
    }

    private AurDepoUrunAdresStok createAddressStockRecord(AurDepoUrunAdresStokDto dto) {
        dto.setStatus(true);
        return aurDepoUrunAdresStokRepository.save(aurDepoUrunAdresStokMapper.toEntity(dto));
    }

    private void saveProductInfo(String barcode, String companyCode, String stokKod, String stokAdi) {
        ProductId id = new ProductId(barcode, companyCode);
        Product product = new Product();
        product.setId(id);
        product.setStokAdi(stokAdi);
        product.setStokKodu(stokKod);
        productService.saveProduct(product);
    }

    public void transferFromTemporaryAreaToAddress(ProductAddressSaveDTO productAddressSaveDTO) {
        long logId = logService.logRequest("transferFromTempAreaToAddress", ENTITY_NAME, productAddressSaveDTO.toString());
        AurDepoUrunAdres temporaryAddress = addressService.checkTemporaryAddress(productAddressSaveDTO.getDepoCode());
        AurDepoUrunAdresStok productAtTmpArea = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(productAddressSaveDTO.getBarcode(), temporaryAddress.getUrunAdresId(), true, productAddressSaveDTO.getDepoCode()).orElseThrow(
            () -> {
                logService.logResponse(logId, "There is any item at temporary area");
                return new BadRequestAlertException("There is any item at temporary area", ENTITY_NAME, "invalidBarcode");
            }
        );

        double transactionAmount = productAtTmpArea.getMiktar() - productAddressSaveDTO.getMiktar();

        if (transactionAmount < 0 || productAddressSaveDTO.getMiktar() > productAtTmpArea.getMiktar()) {
            logService.logResponse(logId, "Gecici Adresteki Miktardan Fazlasını Giremezsiniz");
            throw new BadRequestAlertException("Gecici Adresteki Miktardan Fazlasını Giremezsiniz", ENTITY_NAME, "invalidQuantity");
        }
        assignProductToAddress(productAddressSaveDTO);
        if (transactionAmount == 0) {
            productAtTmpArea.setStatus(false);
            aurDepoUrunAdresStokRepository.save(productAtTmpArea);
        } else {
            productAtTmpArea.setMiktar(transactionAmount);
        }

        logService.logResponse(logId, "success");
    }

    public List<AurDepoStockCodeDto> getAmountAddressByBarcodeAndAddress(String depoCode, List<AurDepoStockCodeDto> aurDepoStockCodeDtoList) {
        for (AurDepoStockCodeDto item : aurDepoStockCodeDtoList) {
            Long productAddressId = addressService.getUrunAddressId(item.getAddress());
            Double productAmount = getAmountOfProduct(item.getBarcode(), productAddressId, depoCode);
            item.setMiktar(productAmount);
        }
        return aurDepoStockCodeDtoList;
    }

    public Double getAmountOfProduct(String barcode, Long addressId, String depoCode) {
        Optional<AurDepoUrunAdresStok> searchItem = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, addressId, true, depoCode);

        if (searchItem.isPresent()) {
            return searchItem.get().getMiktar();
        } else {
            return 0.0;
        }
    }

    @Transactional
    public List<AurDepoStockCodeDto> getAmountOfProductByBarcode(String barcode, String depoCode) {
        List<AurDepoStockCodeDto> response = new ArrayList<>();
        aurDepoUrunAdresStokRepository.findByBarcodeAndStatusAndDepoCode(barcode, true, depoCode)
            .forEach(item -> {
                AurDepoStockCodeDto dto = new AurDepoStockCodeDto();
                dto.setBarcode(barcode);
                dto.setAdresId(item.getUrunAdresId());
                dto.setMiktar(item.getMiktar());
                dto.setStockCode(item.getStokKod());
                dto.setAddress(
                    addressService.findById(item.getUrunAdresId())
                        .map(AurDepoUrunAdres::getAdres)
                        .orElse(null)
                );

                response.add(dto);
            });

        return response;
    }

    public String getAddressInfoByProduct(Long id) {
        Optional<AurDepoUrunAdresStok> productAddress = aurDepoUrunAdresStokRepository.findById(id);
        if (productAddress.isPresent()) {
            Optional<AurDepoUrunAdres> address = addressService.findById(productAddress.get().getUrunAdresId());
            if (address.isPresent()) {
                return address.get().getAdres();
            } else {
                return "Invalid Address";
            }
        } else {
            return "Invalid Address Product Relation";
        }
    }

    public void deleteFromDispatchArea(Integer depoCode, String barcode, Double amount, long controlAddressId) {
        aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, controlAddressId, true, String.valueOf(depoCode))
            .ifPresent(productAddress -> {
                double transientAmount = productAddress.getMiktar() - amount;
                if (transientAmount <= 0) {
                    productAddress.setStatus(false);
                } else {
                    productAddress.setMiktar(transientAmount);
                }
            });

    }

    public List<ProductCategoryDistributionDto> getProductAddressDistribution() {
        List<ProductCategoryDistributionDto> response = new ArrayList<>();
        Arrays.stream(ProductCategoryTypeEnum.values()).forEach(categoryItem -> {
            ProductCategoryDistributionDto dto = new ProductCategoryDistributionDto();
            long counter = 0L;
            int[] itemValues = categoryItem.getValue();
            for (int item : itemValues) {
                long amount = aurDepoUrunAdresStokRepository.findByStokKodStartingWithAndStatus(String.valueOf(item), true).size();
                counter = amount + counter;
            }
            dto.setProductCategoryName(categoryItem.name());
            dto.setCount(counter);
            response.add(dto);
        });

        return response;

    }

    public void decreaseProductAmount(String barcode, String depoCode, Double processAmount, Long addressId) {
        AurDepoUrunAdresStok searchOne = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, addressId, true, depoCode).orElseThrow(() -> new RuntimeException("There is no product at addressId" + addressId));
        Double addressAmount = searchOne.getMiktar();
        if (processAmount > addressAmount) {
            throw new RuntimeException("There is no enough amount at address");
        } else {
            double finalAmount = addressAmount - processAmount;
            searchOne.setMiktar(finalAmount);
            if (finalAmount == 0) {
                searchOne.setStatus(false);
            }
        }
    }

    public void increaseProductAmount(AurOrderDetail aurOrderDetail, Double processAmount, Long addressId) {
        AurDepoUrunAdres address = addressService.findById(addressId).orElseThrow(InvalidAddressException::new);
        AurDepoUrunAdresStok searchOne = aurDepoUrunAdresStokRepository
            .findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(aurOrderDetail.getBarkod(), addressId, true, String.valueOf(address.getDepoNo()))
            .orElse(null);
        if (searchOne == null) {
            ProductAddressSaveDTO dto = new ProductAddressSaveDTO();
            dto.setStokKod(aurOrderDetail.getStokKodu());
            dto.setBarcode(aurOrderDetail.getBarkod());
            dto.setUrunAdres(address.getAdres());
            dto.setDepoCode(String.valueOf(address.getDepoNo()));
            dto.setCompanyCode(address.getCompanyCode());
            dto.setBarkodTipi("RAF");
            dto.setMiktar(processAmount);
            dto.setCheckPartial(false);
            dto.setStokAdi(aurOrderDetail.getStokAdi());
            dto.setStatus(true);
            assignProductToAddress(dto);
            return;
        }
        Double addressAmount = searchOne.getMiktar();
        searchOne.setMiktar(addressAmount + processAmount);
    }

    public AurDepoUrunAdresStok updateProductAmount(String barcode, String depoCode, Double updateAmount, Long addressId) {
        addressService.findById(addressId).orElseThrow(InvalidAddressException::new);
        AurDepoUrunAdresStok searchOne = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, addressId, true, depoCode).orElseThrow(() -> new RuntimeException("There is no product at addressId" + addressId));
        searchOne.setMiktar(updateAmount);
        return searchOne;
    }

    public void productAddressReplacement(AurProductAddressReplacementDto dto) {
        String depoCode = String.valueOf(dto.getDepoNo());
        String barcode = dto.getBarcode();
        long oldAddressId = dto.getOldUrunAdresId();

        AurDepoUrunAdresStok transferProductAddress = aurDepoUrunAdresStokRepository.
            findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(dto.getBarcode(),
                oldAddressId,
                true,
                dto.getDepoNo().toString()).orElseThrow(() -> new InvalidProductAddressException(barcode, oldAddressId, "Kayıt Bulunamadı"));


        if (dto.getMiktar() == 0) {
            throw new BusinessException("Ürün alınan adreste miktar sıfır olduğundan transfer gerçekleştirilemez", ENTITY_NAME, "invalidQuantity");
        }
        if (transferProductAddress.getMiktar() < dto.getMiktar()) {
            throw new BusinessException("Adresteki miktardan fazlasını sevk edemezsiniz", ENTITY_NAME, "invalidQuantity");
        }
        if (dto.getMiktar() <= transferProductAddress.getMiktar()) {
            decreaseProductAmount(barcode, depoCode, dto.getMiktar(), oldAddressId);
            AurDepoUrunAdresStok targetProductAddress = saveProductAddress(replacementDtoToAddressSaveDto(dto));
            addressMovementHistoryService.saveAddressReplacementMovement(dto, targetProductAddress, transferProductAddress.getMiktar());

        }

    }

    public ProductAddressSaveDTO replacementDtoToAddressSaveDto(AurProductAddressReplacementDto replacementDto) {
        Integer companyCode = userService.getUserCompanyCode();
        com.hisarresearch.wms.service.dto.ProductAddressSaveDTO response = new com.hisarresearch.wms.service.dto.ProductAddressSaveDTO();
        response.setStokKod(replacementDto.getStokKodu());
        response.setBarcode(replacementDto.getBarcode());
        AurDepoUrunAdres newAddress = addressService.findById(replacementDto.getNewUrunAdresId()).orElseThrow(InvalidAddressException::new);
        response.setUrunAdres(newAddress.getAdres());
        response.setOrderNo("");
        response.setDepoCode(String.valueOf(replacementDto.getDepoNo()));
        response.setCompanyCode(String.valueOf(companyCode));
        response.setBarkodTipi("RAF");
        response.setStatus(true);
        response.setMiktar(replacementDto.getMiktar());

        return response;
    }

    public void productAddressDefinition(ProductAddressDefinitionDTO productAddressDefinitionDTO) {
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        ProductAddressv2 productAddressv2 = productAddressDefinitionMapper.toEntity(productAddressDefinitionDTO);
        String depoCode = productAddressv2.getDepoCode();
        Long addressId = productAddressv2.getUrunAdres().getUrunAdresId();

        if (warehouseService.hasUniquePickingAddress(depoCode, companyCode)) {
            productAddressService.isValidToSave(depoCode, addressId, productAddressDefinitionDTO.getBarcode());
        }

        Product product = productAddressv2.getProduct();
        product.getId().setCompanyCode(companyCode);
        productService.saveProduct(product);


        Optional<AurDepoUrunAdresStok> processAddress = aurDepoUrunAdresStokRepository.
            findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(productAddressDefinitionDTO.getBarcode(),
                addressId,
                true,
                productAddressDefinitionDTO.getDepoNo());

        processAddress.ifPresentOrElse(address -> {
            if (productAddressDefinitionDTO.getMiktar() == 0) {
                address.setStatus(false);
                defineProductAddressSkt(address, productAddressDefinitionDTO.getSktDateList());
            }
            if (productAddressDefinitionDTO.getMiktar().doubleValue() != address.getMiktar().doubleValue() && productAddressDefinitionDTO.getMiktar() != 0) {
                AurDepoUrunAdresStok updatedOne = updateProductAmount(productAddressDefinitionDTO.getBarcode(), depoCode, productAddressDefinitionDTO.getMiktar(), addressId);
                defineProductAddressSkt(updatedOne, productAddressDefinitionDTO.getSktDateList());
            }
            addressMovementHistoryService.saveAddressDefinitionMovement(productAddressDefinitionDTO, address.getMiktar());

        }, () -> {
            if (productAddressDefinitionDTO.getMiktar() > 0) {
                AurDepoUrunAdresStok aurDepoUrunAdresStok = new AurDepoUrunAdresStok(true,
                    addressId, productAddressDefinitionDTO.getStokKodu(), "RAF",
                    1L, companyCode, depoCode, productAddressDefinitionDTO.getMiktar(), productAddressDefinitionDTO.getBarcode());

                AurDepoUrunAdresStok createdOne = aurDepoUrunAdresStokRepository.save(aurDepoUrunAdresStok);
                defineProductAddressSkt(createdOne, productAddressDefinitionDTO.getSktDateList());
                addressMovementHistoryService.saveAddressDefinitionMovement(productAddressDefinitionDTO, 0.0);
            }
        });


    }

    public ProductAddressDTO checkProductAddress(String depoCode, String addressId, String barcode) throws InvalidSituationGenericError {
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        if (warehouseService.hasUniquePickingAddress(depoCode, companyCode)) {
            productAddressService.isValidToSave(depoCode, Long.valueOf(addressId), barcode);
        }
        ProductAddressDTO response;
        Optional<AurDepoUrunAdresStok> searchOne = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(barcode, Long.parseLong(addressId), true, depoCode);
        if (searchOne.isEmpty()) {
            return new ProductAddressDTO();
        } else {
            response = productAddressMapper.toDtoId(searchOne.get());
            Optional<AurPartialItemDTO> aurPartialItem = aurPartialItemService.findOneByBarcode(barcode);
            if (aurPartialItem.isPresent()) {
                response.setPiece(true);
                response.setPartialItemId(aurPartialItem.get().getId());
            } else {
                response.setPiece(false);
                response.setPartialItemId(0L);
            }

            return response;
        }
    }

    @Transactional
    public void saveProductAddressSkt(AurDepoUrunAdresStok aurDepoUrunAdresStok, List<AurOrderDetailSkt> aurTmpDetailSktList) {
        if (aurTmpDetailSktList == null) {
            return;
        }

        for (AurOrderDetailSkt sktDetail : aurTmpDetailSktList) {
            if (sktDetail != null) {
                ProductAddressv2 productAddressv2 = new ProductAddressv2(aurDepoUrunAdresStok.getId());
                productAddressSktService.createOrUpdateSktList(productAddressv2, sktDetail.getSktDate(), sktDetail.getQuantity());
            }
        }
    }

    @Transactional
    public void defineProductAddressSkt(AurDepoUrunAdresStok aurDepoUrunAdresStok, List<ProductAddressSkt> productAddressSktList) {
        if (productAddressSktList == null) {
            return;
        }

        for (ProductAddressSkt sktDetail : productAddressSktList) {
            if (sktDetail != null) {
                ProductAddressv2 productAddressv2 = new ProductAddressv2(aurDepoUrunAdresStok.getId());
                productAddressSktService.createOrReplaceSktList(productAddressv2, sktDetail.getSktDate(), sktDetail.getQuantity());
            }
        }
    }

    public void savePartialProductToAddress(PartialProductSaveToAddressDTO dto) {
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        Optional<AurPartialItemDTO> partialItem = aurPartialItemService.findOne(dto.getPartialItemId());
        Long addressId = addressService.getUrunAddressId(dto.getAddress());
        if (partialItem.isPresent()) {
            List<AurPartialDetails> partialDetails = aurPartialDetailsService.findByAurPartialItemId(dto.getPartialItemId());

            Optional<AurDepoUrunAdresStok> partialItemAddress = aurDepoUrunAdresStokRepository.findByBarcodeAndUrunAdresIdAndStatusAndDepoCode(dto.getBarcode(), addressId, true, dto.getDepoCode());

            partialItemAddress.ifPresent(item -> {
                if (item.getMiktar() < dto.getAmount()) {
                    throw new RuntimeException("Adresteki miktardan fazlasını giremezsiniz");
                } else {
                    item.setMiktar(item.getMiktar() - dto.getAmount());
                }
            });
            partialItemAddress.orElseThrow(() -> new RuntimeException("Adreste ürün bulunamadı"));
            partialDetails.forEach(item -> {
                ProductAddressSaveDTO saveDto = new ProductAddressSaveDTO();
                saveDto.setUrunAdres(dto.getAddress());
                saveDto.setBarcode(item.getBarcode());
                saveDto.setDepoCode(dto.getDepoCode());
                saveDto.setStokKod(item.getStockCode());
                saveDto.setMiktar(item.getQuantity() * dto.getAmount());
                saveDto.setCompanyCode(companyCode);
                saveDto.setBarkodTipi("RAF");
                saveProductAddress(saveDto);

            });

        } else {
            throw new RuntimeException("Mevcut olmayan parçalı ürün");
        }

    }

    public List<AurDepoStockCodeDto> getPartialDetailInformation(Long aurPartialItemId, String depoCode) {
        aurPartialItemService.findOne(aurPartialItemId).orElseThrow(() -> new RuntimeException("There is no partial item related to " + aurPartialItemId));
        List<AurDepoStockCodeDto> response = new ArrayList<>();
        aurPartialDetailsService.findByAurPartialItemId(aurPartialItemId).forEach(item -> {
            List<AurDepoStockCodeDto> productWithAmount = getAmountOfProductByBarcode(item.getBarcode(), depoCode);
            productWithAmount.forEach(product -> product.setStockName(item.getStockName()));
            response.addAll(productWithAmount);
        });

        return response.stream().sorted(Comparator.comparing(AurDepoStockCodeDto::getAddress)).collect(Collectors.toList());
    }

    public void deleteProductsFromControlAreaByOrder(Long id) {
        AurOrderMaster aurOrderMaster = aurOrderMasterService.findById(id).orElseThrow(InvalidOrderException::new);
        Set<AurOrderDetail> details = aurOrderMaster.getDetails();
        Long controlAddressId = aurOrderMaster.getControlAddress().getUrunAdresId();
        Integer depoCode = aurOrderMaster.getDepoNo();
        details.forEach(detail -> {
            if (!detail.getPiece() && !detail.getStatus().equals(OrderStatus.SUSPENDED.name())) {
                String barcode = detail.getBarkod();
                Double transferAmount = detail.getObserverAmount();
                Optional<AurPartialItemDTO> partialItem = aurPartialItemService.findOneByBarcode(barcode);
                if (partialItem.isPresent()) {
                    aurPartialDetailsService.findByAurPartialItemId(partialItem.get().getId()).forEach(partialDetail -> {
                        Double amount = transferAmount * partialDetail.getQuantity();
                        aurDepoUrunAdresStokService.deleteFromDispatchArea(depoCode, partialDetail.getBarcode(), amount, controlAddressId);
                    });
                } else {
                    aurDepoUrunAdresStokService.deleteFromDispatchArea(depoCode, barcode, transferAmount, controlAddressId);
                }
            }
        });
    }

}
