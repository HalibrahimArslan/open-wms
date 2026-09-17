package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurPartialDetails;
import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.repository.AurPartialDetailsRepository;
import com.hisarresearch.wms.repository.AurPartialItemRepository;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.service.dto.AurPartialResponseDto;
import com.hisarresearch.wms.service.mapper.AurPartialDetailsMapper;
import com.hisarresearch.wms.service.mapper.AurPartialItemMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.erp.MikroServices;
import com.hisarresearch.wms.utility.AurHelper;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AurPartialItem}.
 */
@Service
@Transactional
public class AurPartialItemService {

    private static final String ENTITY_NAME = "AurPartialItemService";

    private final Logger log = LoggerFactory.getLogger(AurPartialItemService.class);

    private final AurPartialItemRepository aurPartialItemRepository;

    private final AurPartialItemMapper aurPartialItemMapper;

    private final AurPartialDetailsRepository aurPartialDetailsRepository;

    @Autowired
    UserService userService;

    @Autowired
    private MikroServices mikroService;

    @Autowired
    AurPartialDetailsService aurPartialDetailsService;

    @Autowired
    private AurPartialDetailsMapper aurPartialDetailsMapper;

    public AurPartialItemService(AurPartialItemRepository aurPartialItemRepository, AurPartialItemMapper aurPartialItemMapper, AurPartialDetailsRepository aurPartialDetailsRepository) {
        this.aurPartialItemRepository = aurPartialItemRepository;
        this.aurPartialItemMapper = aurPartialItemMapper;
        this.aurPartialDetailsRepository = aurPartialDetailsRepository;
    }

    /**
     * Save a aurPartialItem.
     *
     * @param aurPartialItemDTO the entity to save.
     * @return the persisted entity.
     */
    public AurPartialItemDTO save(AurPartialItemDTO aurPartialItemDTO) {
        log.debug("Request to save AurPartialItem : {}", aurPartialItemDTO);
        AurPartialItem aurPartialItem = aurPartialItemMapper.toEntity(aurPartialItemDTO);
        aurPartialItem = aurPartialItemRepository.save(aurPartialItem);
        return aurPartialItemMapper.toDto(aurPartialItem);
    }

    /**
     * Partially update a aurPartialItem.
     *
     * @param aurPartialItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AurPartialItemDTO> partialUpdate(AurPartialItemDTO aurPartialItemDTO) {
        log.debug("Request to partially update AurPartialItem : {}", aurPartialItemDTO);

        return aurPartialItemRepository
            .findById(aurPartialItemDTO.getId())
            .map(
                existingAurPartialItem -> {
                    aurPartialItemMapper.partialUpdate(existingAurPartialItem, aurPartialItemDTO);

                    return existingAurPartialItem;
                }
            )
            .map(aurPartialItemRepository::save)
            .map(aurPartialItemMapper::toDto);
    }

    /**
     * Get all the aurPartialItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AurPartialItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AurPartialItems");
        return aurPartialItemRepository.findAll(pageable).map(aurPartialItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AurPartialItemDTO> findAllList() {
        log.debug("Request to get all AurPartialItems");
        return aurPartialItemRepository.findAll().stream().map(aurPartialItemMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one aurPartialItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AurPartialItemDTO> findOne(Long id) {
        log.debug("Request to get AurPartialItem : {}", id);
        return aurPartialItemRepository.findById(id).map(aurPartialItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AurPartialItemDTO> findOneByBarcode(String barcode) {
        log.debug("Request to get AurPartialItem : {}", barcode);
        return aurPartialItemRepository.findAllByPackageBarcodeAndStatusTrue(barcode).map(aurPartialItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AurPartialItemDTO> findOneByStockCode(String stockCode) {
        log.debug("Request to get AurPartialItem : {}", stockCode);
        return aurPartialItemRepository.findAllByPackageBarcodeAndStatusTrue(stockCode).map(aurPartialItemMapper::toDto);
    }

    /**
     * Delete the aurPartialItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AurPartialItem : {}", id);
        aurPartialItemRepository.deleteById(id);
    }


    /**
     * Delete the aurPartialItem with child items by id.
     *
     * @param id the id of the entity.
     */
    public void deleteWithChildren(Long id) {
        log.debug("Request to delete AurPartialItem with child items : {}", id);
        aurPartialDetailsService.deleteBulk(id);
        aurPartialItemRepository.deleteById(id);
    }

    public List<AurPartialResponseDto> getPartialItemDetail(List<String> packageCodes) {
        List<AurPartialResponseDto> aurPartialResponseDto = new ArrayList<>();
        for (String item : packageCodes) {
            Optional<AurPartialItem> aurPartialItem = aurPartialItemRepository.findAllByPackageCodeAndStatusTrue(item);
            if (aurPartialItem.isPresent()) {
                AurPartialResponseDto dto = new AurPartialResponseDto();
                List<AurPartialDetails> partialDetails = aurPartialDetailsRepository.findByAurPartialItem_Id(aurPartialItem.get().getId());
                dto.setPackageCode(aurPartialItem.get().getPackageCode());
                dto.setPackageName(aurPartialItem.get().getPackageName());
                dto.setPackageBarcode(aurPartialItem.get().getPackageBarcode());
                dto.setPackageDetail(aurPartialDetailsMapper.toDto(partialDetails));

                aurPartialResponseDto.add(dto);
            }
        }
        return aurPartialResponseDto;
    }

    public List<AurPartialResponseDto> getPartialItemDetailById(List<Long> idList) {
        List<AurPartialResponseDto> aurPartialResponseDto = new ArrayList<>();
        for (Long item : idList) {
            Optional<AurPartialItem> aurPartialItem = aurPartialItemRepository.findById(item);
            if (aurPartialItem.isPresent()) {
                AurPartialResponseDto dto = new AurPartialResponseDto();
                List<AurPartialDetails> partialDetails = aurPartialDetailsRepository.findByAurPartialItem_Id(aurPartialItem.get().getId());
                dto.setId(item);
                dto.setPackageCode(aurPartialItem.get().getPackageCode());
                dto.setPackageBarcode(aurPartialItem.get().getPackageBarcode());
                dto.setPackageName(aurPartialItem.get().getPackageName());
                dto.setPackageDetail(aurPartialDetailsMapper.toDto(partialDetails));

                aurPartialResponseDto.add(dto);
            }
        }

        return aurPartialResponseDto;
    }

    public List<AurPartialResponseDto> findRelevantItemByBarcode(List<String> barcodeList) {
        AurHelper helper = new AurHelper();
        List<Long> idList = new ArrayList<>();
        List<Long> distinctPartialItemIdList = aurPartialDetailsRepository.findByBarcodeIn(barcodeList)
            .stream()
            .filter(helper.distinctByKey(AurPartialDetails::getAurPartialItem))
            .map(item -> item.getAurPartialItem().getId())
            .collect(Collectors.toList());

        distinctPartialItemIdList.forEach(master -> {
            List<AurPartialDetails> aurPartialDetails = aurPartialDetailsRepository.findByAurPartialItem_Id(master);
            List<String> searchItemBarcodes = aurPartialDetails.stream().map(AurPartialDetails::getBarcode).collect(Collectors.toList());

            if (new HashSet<>(barcodeList).containsAll(searchItemBarcodes)) {
                idList.add(master);
            }
        });

        return getPartialItemDetailById(idList);
    }

    @Transactional
    public AurPartialItem updatePartialFromMicro(Long id) throws Exception {
        //AurPartialItem partialItem = aurPartialItemRepository.findById(id).orElseThrow(BadRequestException::new);
        //List<AurPartialDetails> existPartialDetails = aurPartialDetailsRepository.findByAurPartialItem_Id(partialItem.getId());
        //aurPartialDetailsRepository.deleteAll(existPartialDetails);
        AurPartialItem partialItem = aurPartialItemRepository.findById(id).orElseThrow(() -> new BusinessException("Parçalı ürün tanımı bulunamadı: "+id, ENTITY_NAME));
        aurPartialDetailsRepository.deleteByPartialItemId(partialItem.getId());
        Set<AurPartialDetails> partialDetailsList =  aurPartialDetailsService.updatePartialDetailsFromMicro(partialItem);
        partialItem.setAurPartialDetails(partialDetailsList);
        return partialItem;
    }

    @Transactional
    public AurPartialItem transferPartialFromMicro(String stockCode) throws Exception {
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = mikroService.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        LinkedHashMap<String, String> erpPartialItem = mikroService.getPartialItem(token,aurCompanyDto.getApiEndPoint(),stockCode);
        AurPartialItem createPartialItem = new AurPartialItem();
        createPartialItem.setPackageBarcode(erpPartialItem.get("packageBarcode"));
        createPartialItem.setPackageCode(erpPartialItem.get("packageCode"));
        createPartialItem.setPackageName(erpPartialItem.get("packageName"));
        createPartialItem.setStatus(true);
        AurPartialItem createdOne = aurPartialItemRepository.save(createPartialItem);
        Set<AurPartialDetails> createdPartialDetails = aurPartialDetailsService.updatePartialDetailsFromMicro(createdOne);
        createdOne.setAurPartialDetails(createdPartialDetails);
        return createdOne;
    }

    public void validatePartialGroup(Long partialItemId,
                                      List<AurOrderDetail> pieces,
                                      List<String> errors) {

        String mainName = aurPartialItemRepository.findById(partialItemId)
            .map(AurPartialItem::getPackageName)
            .orElse("ID=" + partialItemId);

        List<AurPartialDetails> recipe = aurPartialDetailsService
            .findByAurPartialItemId(partialItemId);

        if (recipe.isEmpty()) {
            errors.add(String.format("%s için reçete bulunamadı", mainName));
            return;
        }

        Map<String, BigDecimal> recipeMap = recipe.stream()
            .collect(Collectors.toMap(
                AurPartialDetails::getStockCode,
                d -> BigDecimal.valueOf(d.getQuantity()),
                (a, b) -> a
            ));

        BigDecimal referenceSetCount = null;

        for (AurOrderDetail piece : pieces) {
            BigDecimal recipeQty = recipeMap.get(piece.getStokKodu());

            if (recipeQty == null || recipeQty.signum() == 0) {
                errors.add(String.format(
                    "%s - %s parçası reçetede tanımlı değil",
                    mainName, piece.getStokKodu()));
                return;
            }

            BigDecimal teslim = BigDecimal.valueOf(
                piece.getTeslimMiktar() != null ? piece.getTeslimMiktar() : 0.0
            );

            BigDecimal[] divAndRem = teslim.divideAndRemainder(recipeQty);
            BigDecimal setCount = divAndRem[0];
            BigDecimal remainder = divAndRem[1];

            if (remainder.signum() != 0) {
                errors.add(String.format(
                    "%s - %s parçası tam set oluşturmuyor (teslim: %s, reçete: %s)",
                    mainName, piece.getStokKodu(), teslim, recipeQty));
                return;
            }

            if (referenceSetCount == null) {
                referenceSetCount = setCount;
            } else if (referenceSetCount.compareTo(setCount) != 0) {
                errors.add(String.format(
                    "%s - parçalar eşit set sayısı oluşturmuyor (beklenen: %s set, %s parçası: %s set)",
                    mainName, referenceSetCount, piece.getStokKodu(), setCount));
                return;
            }
        }
    }


}
