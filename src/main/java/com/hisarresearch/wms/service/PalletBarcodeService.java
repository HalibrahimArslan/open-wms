package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.CustomerAddress;
import com.hisarresearch.wms.domain.barcode.PalletBarcode;
import com.hisarresearch.wms.domain.barcode.PalletBarcodeOrderRel;
import com.hisarresearch.wms.domain.enumeration.PalletBarcodeStatus;
import com.hisarresearch.wms.repository.barcode.PalletBarcodeRepository;
import com.hisarresearch.wms.service.dto.barcode.PalletBarcodeResponseDto;
import com.hisarresearch.wms.service.dto.barcode.PalletBarcodeWithDetailDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PalletBarcodeService {
    private final Logger log = LoggerFactory.getLogger(PalletBarcodeService.class);

    private final String ENTITY_NAME = "palletBarcode";

    private final PalletBarcodeRepository palletBarcodeRepository;

    private final AurLogService aurLogService;

    private CustomerAddressService customerAddressService;


    public PalletBarcodeService(PalletBarcodeRepository palletBarcodeRepository, AurLogService aurLogService, CustomerAddressService customerAddressService) {
        this.palletBarcodeRepository = palletBarcodeRepository;
        this.aurLogService = aurLogService;
        this.customerAddressService = customerAddressService;
    }

    public PalletBarcode savePalletBarcode(String newPalletBarcode) {
        log.info("Save Pallet Barcode with {}", newPalletBarcode);
        Long logId = aurLogService.logRequest(ENTITY_NAME, "savePalletBarcode", newPalletBarcode);
        Optional<PalletBarcode> isExist = palletBarcodeRepository.findByBarcode(newPalletBarcode);

        if (isExist.isPresent()) {
            aurLogService.logResponse(logId, "Invalid pallet barcode");
            throw new BadRequestAlertException("Invalid pallet barcode", ENTITY_NAME, "palletBarcodeExist");
        } else {
            aurLogService.logResponse(logId, "Successful saved pallet barcode");
            return saveOne(newPalletBarcode);
        }
    }

    public PalletBarcode saveOne(String barcode) {
        log.info("Save One Pallet Barcode With {}", barcode);
        PalletBarcode palletBarcode = new PalletBarcode();
        palletBarcode.setBarcode(barcode);
        palletBarcode.setPalletBarcodeStatus(PalletBarcodeStatus.CREATED);
        palletBarcodeRepository.save(palletBarcode);
        log.debug("Saved One Pallet Barcode {}", palletBarcode);
        return palletBarcode;
    }

    @Transactional
    public Optional<PalletBarcode> findOne(Long id) {
        return palletBarcodeRepository.findById(id);
    }


    @Transactional
    public String findLastCreatedPalletBarcode() {
        Optional<PalletBarcode> searchBarcode = palletBarcodeRepository.findTopByOrderByIdDesc();

        return searchBarcode.map(palletBarcode -> palletBarcode.getBarcode().substring(0, 12)).orElse("999999000001");
    }

    @Transactional
    public Optional<PalletBarcode> findByBarcode(String barcode) {
        return palletBarcodeRepository.findByBarcode(barcode);
    }

    @Transactional
    public List<PalletBarcodeResponseDto> getEnablePalletBarcodeList(PalletBarcodeStatus palletBarcodeStatus) {
        return palletBarcodeRepository.findByPalletBarcodeStatus(palletBarcodeStatus).stream().filter(Objects::nonNull).map(this::palletBarcodeToPalletBarcodeDto).collect(Collectors.toList());
    }

    public List<String> generatePalletBarcode(int quantity) {
        log.debug("Generate pallet barcode with quantity of {}", quantity);

        List<String> newPalletBarcodeList = new ArrayList<>();

        for (int i = 1; i <= quantity; i++) {
            EAN13CheckDigit check = new EAN13CheckDigit();
            String data = findLastCreatedPalletBarcode();
            long longData = Long.parseLong(data);
            String newData;
            String checkDigit;

            longData = longData + 1;
            newData = String.valueOf(longData);

            try {
                checkDigit = check.calculate(newData);
            } catch (CheckDigitException e) {
                e.printStackTrace();
                throw new BadRequestAlertException("Yeni barkod numarası oluşturma sırasında hata alındı! Üretilmek istenen barkod: " + newData, ENTITY_NAME, "EAN-13");
            }

            newData = newData + checkDigit;
            boolean control = check.isValid(newData);
            if (control) {
                savePalletBarcode(newData);
                newPalletBarcodeList.add(newData);
            }

        }
        log.debug("Created pallet barcode list {}", newPalletBarcodeList);
        return newPalletBarcodeList;
    }


    public PalletBarcodeResponseDto palletBarcodeToPalletBarcodeDto(PalletBarcode palletBarcode) {
        if (palletBarcode == null) {
            return null;
        } else {
            PalletBarcodeResponseDto palletBarcodeResponseDto = new PalletBarcodeResponseDto();
            palletBarcodeResponseDto.setPalletBarcode(palletBarcode.getBarcode());
            palletBarcodeResponseDto.setPalletBarcodeStatus(palletBarcode.getPalletBarcodeStatus());

            return palletBarcodeResponseDto;
        }
    }

    public List<String> createPalletBarcode(int quantity) {
        List<PalletBarcodeResponseDto> dto = getEnablePalletBarcodeList(PalletBarcodeStatus.CREATED);
        if (dto.size() == quantity) {
            return dto.stream().map(PalletBarcodeResponseDto::getPalletBarcode).collect(Collectors.toList());
        } else if (dto.size() > quantity) {
            return dto.stream().sorted(Comparator.comparing(PalletBarcodeResponseDto::getPalletBarcode)).map(PalletBarcodeResponseDto::getPalletBarcode).limit(quantity).collect(Collectors.toList());
        } else {

            List<String> response = generatePalletBarcode(quantity - dto.size());
            dto.forEach(existingItem -> response.add(existingItem.getPalletBarcode()));
            return response;
        }
    }

    /**
     * Partially update a palletBarcode.
     *
     * @param palletBarcode the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PalletBarcode> partialUpdate(PalletBarcode palletBarcode) {
        log.debug("Request to partially update PalletBarcode : {}", palletBarcode);

        return palletBarcodeRepository.findById(palletBarcode.getId())
            .map(
                existingItem -> {
                    if (palletBarcode.getPalletBarcodeStatus() != null) {
                        existingItem.setPalletBarcodeStatus(palletBarcode.getPalletBarcodeStatus());
                    }
                    if (palletBarcode.getBarcode() != null) {
                        existingItem.setBarcode(palletBarcode.getBarcode());
                    }

                    return existingItem;
                })
            .map(palletBarcodeRepository::save);
    }

    public void updateOneById(Long palletBarcodeId) {
        log.debug("Request to partially update by id PalletBarcode : {}", palletBarcodeId);
        palletBarcodeRepository.findById(palletBarcodeId).get().setPalletBarcodeStatus(PalletBarcodeStatus.ASSIGNED);
    }

    public List<PalletBarcodeWithDetailDTO> findPrintableList() {
        log.debug("Request to get will be print pallet barcodes");
        List<PalletBarcode> palletBarcodes = palletBarcodeRepository.findByPalletBarcodeStatus(PalletBarcodeStatus.ASSIGNED);
        List<PalletBarcodeWithDetailDTO> palletBarcodeWithDetailDTOS = new ArrayList<>();
        palletBarcodes.forEach(palletBarcode -> {
            PalletBarcodeWithDetailDTO palletBarcodeWithDetailDTO = new PalletBarcodeWithDetailDTO();
            palletBarcodeWithDetailDTO.setId(palletBarcode.getId());
            palletBarcodeWithDetailDTO.setBarcode(palletBarcode.getBarcode());
            List<PalletBarcodeOrderRel> details = new ArrayList<>(palletBarcode.getDetails());
            List<PalletBarcodeOrderRel> activeDetails = details.stream().filter(detail -> {
                String orderStatus = detail.getAurOrder().getStatus();
                return orderStatus.equals("OPEN") || orderStatus.equals("IN_PROGRESS") || orderStatus.equals("OUT_PROGRESS");
            }).collect(Collectors.toList());
            List<String> distinctOrderNos = activeDetails
                .stream()
                .map(PalletBarcodeOrderRel::getAurTmpDetail)
                .filter(Objects::nonNull)
                .map(AurOrderDetail::getSiparisNo)
                .distinct()
                .collect(Collectors.toList());

            if((long) activeDetails.size() > 0){
                AurOrderMaster aurOrderMaster = activeDetails.get(0).getAurOrder();
                Optional<CustomerAddress> customerAddress = customerAddressService.getCustomerAddress(aurOrderMaster.getId());
                String phoneNumber = customerAddress.isPresent() ? customerAddress.get().getSevkTel() : "";
                String district = customerAddress.isPresent() ? customerAddress.get().getSevkAddress() : "";

                palletBarcodeWithDetailDTO.setCustomerName(aurOrderMaster.getFirmName());
                palletBarcodeWithDetailDTO.setAddress(district);
                palletBarcodeWithDetailDTO.setPhoneNumber(phoneNumber);
                palletBarcodeWithDetailDTO.setOrderNo(StringUtils.collectionToCommaDelimitedString(distinctOrderNos));
                palletBarcodeWithDetailDTOS.add(palletBarcodeWithDetailDTO);
            }
        });
        return palletBarcodeWithDetailDTOS;
    }

}
