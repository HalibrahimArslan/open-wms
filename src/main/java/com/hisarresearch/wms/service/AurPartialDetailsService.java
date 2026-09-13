package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurPartialDetails;
import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.repository.AurPartialDetailsRepository;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.AurPartialDetailsDTO;
import com.hisarresearch.wms.service.mapper.AurPartialDetailsMapper;

import java.util.*;

import com.hisarresearch.wms.service.erp.MikroServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AurPartialDetails}.
 */
@Service
@Transactional
public class AurPartialDetailsService {

    private final Logger log = LoggerFactory.getLogger(AurPartialDetailsService.class);

    private final AurPartialDetailsRepository aurPartialDetailsRepository;

    private final AurPartialDetailsMapper aurPartialDetailsMapper;

    private final UserService userService;

    private final MikroServices mikroServices;

    private final AurOrderDetailService aurOrderDetailService;

    public AurPartialDetailsService(
        AurPartialDetailsRepository aurPartialDetailsRepository,
        AurPartialDetailsMapper aurPartialDetailsMapper, UserService userService, MikroServices mikroServices, AurOrderDetailService aurOrderDetailService
    ) {
        this.aurPartialDetailsRepository = aurPartialDetailsRepository;
        this.aurPartialDetailsMapper = aurPartialDetailsMapper;
        this.userService = userService;
        this.mikroServices = mikroServices;
        this.aurOrderDetailService = aurOrderDetailService;
    }

    /**
     * Save a aurPartialDetails.
     *
     * @param aurPartialDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public AurPartialDetailsDTO save(AurPartialDetailsDTO aurPartialDetailsDTO) {
        log.debug("Request to save AurPartialDetails : {}", aurPartialDetailsDTO);
        AurPartialDetails aurPartialDetails = aurPartialDetailsMapper.toEntity(aurPartialDetailsDTO);
        aurPartialDetails = aurPartialDetailsRepository.save(aurPartialDetails);
        return aurPartialDetailsMapper.toDto(aurPartialDetails);
    }

    /**
     * Partially update a aurPartialDetails.
     *
     * @param aurPartialDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AurPartialDetailsDTO> partialUpdate(AurPartialDetailsDTO aurPartialDetailsDTO) {
        log.debug("Request to partially update AurPartialDetails : {}", aurPartialDetailsDTO);

        return aurPartialDetailsRepository
            .findById(aurPartialDetailsDTO.getId())
            .map(
                existingAurPartialDetails -> {
                    aurPartialDetailsMapper.partialUpdate(existingAurPartialDetails, aurPartialDetailsDTO);

                    return existingAurPartialDetails;
                }
            )
            .map(aurPartialDetailsRepository::save)
            .map(aurPartialDetailsMapper::toDto);
    }

    /**
     * Get all the aurPartialDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AurPartialDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AurPartialDetails");
        return aurPartialDetailsRepository.findAll(pageable).map(aurPartialDetailsMapper::toDto);
    }

    /**
     * Get one aurPartialDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AurPartialDetailsDTO> findOne(Long id) {
        log.debug("Request to get AurPartialDetails : {}", id);
        return aurPartialDetailsRepository.findById(id).map(aurPartialDetailsMapper::toDto);
    }

    /**
     * Delete the aurPartialDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AurPartialDetails : {}", id);
        aurPartialDetailsRepository.deleteById(id);
    }

    /**
     * Delete the aurPartialDetails by  partial_item_id.
     *
     * @param partialItemId the of the partial item entity.
     */
    public void deleteBulk(Long partialItemId) {
        log.debug("Request to delete the aurPartialDetails by  partial_item_id : {}", partialItemId);
        List<AurPartialDetails> aurPartialDetailsList = aurPartialDetailsRepository.findByAurPartialItem_Id(partialItemId);
        aurPartialDetailsRepository.deleteAll(aurPartialDetailsList);
    }


    @Transactional(readOnly = true)
    public Optional<AurPartialDetails> findByBarcodeAndAurPartialItemId(String barcode,Long aurPartialItemId) {
        log.debug("Request to get AurPartialDetails by barcode and aurPartialItemId : {} {}", barcode,aurPartialItemId);
        return aurPartialDetailsRepository.findByBarcodeAndAurPartialItem_Id(barcode,aurPartialItemId);
    }

    @Transactional(readOnly = true)
    public List<AurPartialDetails> findByAurPartialItemId(Long aurPartialItemId) {
        log.debug("Request to get AurPartialDetails by aurPartialItemId : {}", aurPartialItemId);
        return aurPartialDetailsRepository.findByAurPartialItem_Id(aurPartialItemId);
    }

    public Set<AurPartialDetails> updatePartialDetailsFromMicro(AurPartialItem partialItem) throws Exception {
        Set<AurPartialDetails> response = new HashSet<>();
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        mikroServices.getPartialItemDetails(partialItem,token,aurCompanyDto.getApiEndPoint()).forEach(erpPartialItem -> {
           /*Optional<AurPartialDetails> searchPartialDetail = existPartialDetails.stream()
               .filter(existingPartialDetail -> existingPartialDetail.getStockCode().equals(erpPartialItem.getStockCode()))
               .findAny();
           if(searchPartialDetail.isPresent()) {
               searchPartialDetail.get().setBarcode(erpPartialItem.getBarcode());
               searchPartialDetail.get().setQuantity(erpPartialItem.getQuantity());
               aurPartialDetailsRepository.save(searchPartialDetail.get());
               response.add(searchPartialDetail.get());
           }
           else{
               AurPartialDetails addedPartialDetail = aurPartialDetailsRepository.save(erpPartialItem);
               response.add(addedPartialDetail);
           }*/
            AurPartialDetails addedPartialDetail = aurPartialDetailsRepository.save(erpPartialItem);
            aurOrderDetailService.updateOrderDetailsPartialItemsStockCode(addedPartialDetail);
            response.add(addedPartialDetail);
        });

        return response;
    }

}
