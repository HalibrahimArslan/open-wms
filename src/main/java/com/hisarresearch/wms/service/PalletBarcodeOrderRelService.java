package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.barcode.PalletBarcode;
import com.hisarresearch.wms.domain.barcode.PalletBarcodeOrderRel;
import com.hisarresearch.wms.domain.enumeration.PalletBarcodeStatus;
import com.hisarresearch.wms.repository.barcode.PalletBarcodeOrderRelRepository;
import com.hisarresearch.wms.service.dto.PalletOrderRelIdDto;
import com.hisarresearch.wms.service.dto.barcode.*;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PalletBarcodeOrderRelService {
    private final Logger log = LoggerFactory.getLogger(PalletBarcodeOrderRelService.class);

    private final String ENTITY_NAME = "palletBarcodeOrderRel";


    @Autowired
    private  PalletBarcodeOrderRelRepository palletBarcodeOrderRelRepository;

    @Autowired
    private  PalletBarcodeService palletBarcodeService;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @Autowired
    private AurOrderDetailService aurOrderDetailService;


    /**
     * Save a productBarcodeOrderRel.
     *
     * @param palletBarcodeOrderRel the entity to save.
     * @return the persisted entity.
     */
    public PalletBarcodeOrderRel save(PalletBarcodeOrderRel palletBarcodeOrderRel) {
        log.debug("Request to save PalletBarcodeOrderRel : {}", palletBarcodeOrderRel);
        return palletBarcodeOrderRelRepository.save(palletBarcodeOrderRel);
    }


    public PalletBarcodeSaveDto savePalletBarcode(PalletBarcodeRequestDto palletBarcodeRequestDto){
        Optional<AurOrderMaster> orderMaster = aurOrderMasterService.findByOrderInfo(palletBarcodeRequestDto.getAurOrderId());
        Long palletBarcodeId;
        String palletBarcode;

        if(orderMaster.isPresent()){
            List<PalletOrderRelIdDto> aurTmpDetailIds = aurOrderMasterService.getIdFromStockCodeList(palletBarcodeRequestDto.getPalletBarcodeList(),orderMaster.get().getId());

            if(palletBarcodeRequestDto.getPalletBarcodeId() == null){
                List<String> palletBarcodes = palletBarcodeService.createPalletBarcode(1);
                palletBarcodeId = palletBarcodeService.findByBarcode(palletBarcodes.get(0)).get().getId();
                palletBarcode = palletBarcodes.get(0);
            }
            else{
                palletBarcodeId = palletBarcodeRequestDto.getPalletBarcodeId();
                palletBarcode = palletBarcodeService.findOne(palletBarcodeId).get().getBarcode();
            }

            List<PalletDetailDto> palletDetailDtos = new ArrayList<>();
            aurTmpDetailIds.forEach(tmpItem -> {
                PalletDetailDto palletDetailDto = new PalletDetailDto();
                List<Long> idList = new ArrayList<>();
                palletDetailDto.setStockCode(tmpItem.getStockCode());
                AurOrderDetail relatedOne = aurOrderDetailService.findById(tmpItem.getAurTmpDetailIds().get(0)).get();
                palletDetailDto.setStockName(relatedOne.getStokAdi());
                palletDetailDto.setQuantity(relatedOne.getTeslimMiktar());
                tmpItem.getAurTmpDetailIds().forEach(saveItem -> {
                    isExistsOrder(orderMaster.get().getId(), saveItem);
                    PalletBarcodeOrderRel palletBarcodeOrderRel = new PalletBarcodeOrderRel();
                    palletBarcodeOrderRel.setAurOrder(orderMaster.get());
                    AurOrderDetail aurOrderDetail = new AurOrderDetail();
                    aurOrderDetail.setId(saveItem);
                    palletBarcodeOrderRel.setAurTmpDetail(aurOrderDetail);
                    PalletBarcode palletBarcode1 = new PalletBarcode();
                    palletBarcode1.setId(palletBarcodeId);
                    palletBarcodeOrderRel.setPalletBarcode(palletBarcode1);
                    palletBarcodeOrderRel.setStatus(true);

                    PalletBarcodeOrderRel savedOne = save(palletBarcodeOrderRel);
                    idList.add(savedOne.getId());
                });

                palletDetailDto.setPalletBarcodeOrderRelId(idList);
                palletDetailDtos.add(palletDetailDto);
            });
            palletBarcodeService.updateOneById(palletBarcodeId);
            return palletBarcodeRequestDtoToSaveDto(palletDetailDtos,palletBarcode,palletBarcodeId);
        }
        else{
            throw new InvalidOrderException();
        }
    }

    public PalletBarcodeSaveDto palletBarcodeRequestDtoToSaveDto(List<PalletDetailDto> dto,String palletBarcode,Long palletBarcodeId){
        PalletBarcodeSaveDto palletBarcodeSaveDto = new PalletBarcodeSaveDto();
        palletBarcodeSaveDto.setPalletBarcodeId(palletBarcodeId);
        palletBarcodeSaveDto.setPalletBarcodeList(dto);
        palletBarcodeSaveDto.setPalletBarcode(palletBarcode);
        return  palletBarcodeSaveDto;
    }

    @Transactional
    public void isExistsOrder(Long aurOrderId,Long aurTmpDetailId){
        Optional<PalletBarcodeOrderRel> response = palletBarcodeOrderRelRepository.findByAurOrderIdAndAurTmpDetailIdAndStatus(aurOrderId,aurTmpDetailId,true);
        if(response.isPresent()){
            throw new BadRequestAlertException("Order is exist at enable barcode",ENTITY_NAME,"palletBarcodeRel");
        }
    }

    public List<PalletBarcodeSaveDto> getPalletBarcodeList(String orderInfo){
        Optional<AurOrderMaster> orderMaster = aurOrderMasterService.findByOrderInfo(orderInfo);
        List<PalletBarcodeSaveDto> response = new ArrayList<>();

        if(orderMaster.isPresent()) {
            List<Long> distinctPalletBarcodeIds = palletBarcodeOrderRelRepository.findByAurOrderIdAndStatus(orderMaster.get().getId(), true)
                .stream()
                .map(PalletBarcodeOrderRel::getPalletBarcode)
                .map(PalletBarcode::getId)
                .distinct()
                .collect(Collectors.toList());

            for (Long id : distinctPalletBarcodeIds) {
                Optional<PalletBarcode> palletBarcode = palletBarcodeService.findOne(id);
                List<PalletDetailDto> stockCodeList = new ArrayList<>();
                if (palletBarcode.isPresent()) {
                    PalletBarcodeSaveDto dto = new PalletBarcodeSaveDto();
                    dto.setPalletBarcodeId(id);
                    dto.setPalletBarcode(palletBarcode.get().getBarcode());
                    palletBarcodeOrderRelRepository.findByAurOrderIdAndPalletBarcodeIdAndStatus(orderMaster.get().getId(), palletBarcode.get().getId(), true)
                        .forEach(tmpIds -> aurOrderDetailService.findById(tmpIds.getAurTmpDetail().getId()).ifPresent(tmpItem -> {
                            if(stockCodeList.stream().anyMatch(q -> q.getStockCode().equals(tmpItem.getStokKodu()))){
                                List<PalletDetailDto> searchOne = stockCodeList.stream().filter(q->q.getStockCode().equals(tmpItem.getStokKodu())).collect(Collectors.toList());
                                searchOne.get(0).getPalletBarcodeOrderRelId().add(tmpIds.getId());
                            }
                            else{
                                PalletDetailDto palletDetailDto = new PalletDetailDto();
                                palletDetailDto.setStockCode(tmpItem.getStokKodu());
                                palletDetailDto.setStockName(tmpItem.getStokAdi());
                                palletDetailDto.setQuantity(tmpItem.getTeslimMiktar());
                                palletDetailDto.setPalletBarcodeOrderRelId(new ArrayList<Long>(){{add(tmpIds.getId());}});
                                stockCodeList.add(palletDetailDto);
                            }

                        }));
                    dto.setPalletBarcodeList(stockCodeList);
                    response.add(dto);
                } else {
                    throw new BadRequestAlertException("Invalid pallet barcode id", ENTITY_NAME, "idError");
                }
            }
            return response;
        }
        else
        {
            throw new InvalidOrderException();
        }
    }

    public void deletePalletBarcode(Long palletBarcodeId){
       List<PalletBarcodeOrderRel> searchPallet = palletBarcodeOrderRelRepository.findByPalletBarcodeId(palletBarcodeId);
       searchPallet.forEach(item -> item.setStatus(false));
       palletBarcodeService.findOne(palletBarcodeId).get().setPalletBarcodeStatus(PalletBarcodeStatus.CREATED);
    }

    public void deletePalletBarcodeDetail(List<Long> palletBarcodeOrderRelIds){
        if(palletBarcodeOrderRelIds.isEmpty()){
            throw new RuntimeException();
        }
        else{
            PalletBarcodeOrderRel processItem = palletBarcodeOrderRelRepository.findById(palletBarcodeOrderRelIds.get(0)).get();
            Long aurOrderId = processItem.getAurOrder().getId();
            Long palletBarcodeId = processItem.getPalletBarcode().getId();
            palletBarcodeOrderRelIds.forEach(id -> palletBarcodeOrderRelRepository.findById(id).get().setStatus(false));

            checkPallet(aurOrderId,palletBarcodeId);
        }
    }

    public void checkPallet(Long aurOrderId,Long palletBarcodeId){
        List<PalletBarcodeOrderRel> palletBarcodeOrderRels = palletBarcodeOrderRelRepository.findByAurOrderIdAndPalletBarcodeIdAndStatus(aurOrderId,palletBarcodeId,true);
        if(palletBarcodeOrderRels.isEmpty()){
            palletBarcodeService.findOne(palletBarcodeId).ifPresent(palletBarcode -> palletBarcode.setPalletBarcodeStatus(PalletBarcodeStatus.CREATED));
        }
    }

    public void completePalletBarcodes(Long aurOrderId){
        List<Long> palletBarcodeIds = palletBarcodeOrderRelRepository.findByAurOrderIdAndStatus(aurOrderId,true)
            .stream()
            .map(PalletBarcodeOrderRel::getPalletBarcode)
            .map(PalletBarcode::getId)
            .distinct().collect(Collectors.toList());

        palletBarcodeIds.forEach(id -> palletBarcodeService.findOne(id).ifPresent(palletBarcode -> palletBarcode.setPalletBarcodeStatus(PalletBarcodeStatus.COMPLETED)));
    }


    public List<PalletBarcodeSaveDto> getPalletDetail(String palletBarcode){
        Optional<PalletBarcode> searchPallet = palletBarcodeService.findByBarcode(palletBarcode);
        List<PalletBarcodeSaveDto> response = new ArrayList<>();

        if(searchPallet.isPresent()){
            List<PalletBarcodeOrderRel> palletBarcodeOrderRels =  palletBarcodeOrderRelRepository.findByPalletBarcodeId(searchPallet.get().getId());
            List<PalletDetailDto> stockCodeList = new ArrayList<>();
            PalletBarcodeSaveDto dto = new PalletBarcodeSaveDto();
                dto.setPalletBarcodeId(searchPallet.get().getId());
                dto.setPalletBarcode(palletBarcode);
                palletBarcodeOrderRelRepository.findByAurOrderIdAndPalletBarcodeIdAndStatus(palletBarcodeOrderRels.get(0).getAurOrder().getId(), searchPallet.get().getId(), true)
                    .forEach(tmpIds -> aurOrderDetailService.findById(tmpIds.getAurTmpDetail().getId()).ifPresent(tmpItem -> {
                        if(stockCodeList.stream().anyMatch(q -> q.getStockCode().equals(tmpItem.getStokKodu()))){
                            List<PalletDetailDto> searchOne = stockCodeList.stream().filter(q->q.getStockCode().equals(tmpItem.getStokKodu())).collect(Collectors.toList());
                            searchOne.get(0).getPalletBarcodeOrderRelId().add(tmpIds.getId());
                        }
                        else{
                            PalletDetailDto palletDetailDto = new PalletDetailDto();
                            palletDetailDto.setStockCode(tmpItem.getStokKodu());
                            palletDetailDto.setStockName(tmpItem.getStokAdi());
                            palletDetailDto.setQuantity(tmpItem.getTeslimMiktar());
                            palletDetailDto.setPalletBarcodeOrderRelId(new ArrayList<>() {{
                                add(tmpIds.getId());
                            }});
                            stockCodeList.add(palletDetailDto);
                        }

                    }));
                dto.setPalletBarcodeList(stockCodeList);
                response.add(dto);
        }
        else{
            throw new BadRequestAlertException("Invalid pallet barcode",ENTITY_NAME,"invalid key");
        }
        return response;
    }


    public List<PalletInfoDTO> getPalletInfo(String palletBarcode){
        Optional<PalletBarcode> palletBarcodeItem = palletBarcodeService.findByBarcode(palletBarcode);
        List<PalletInfoDTO> response = new ArrayList<>();

        if(palletBarcodeItem.isPresent()){
            List<PalletBarcodeOrderRel> palletBarcodeOrderRelList = palletBarcodeOrderRelRepository.findByPalletBarcodeIdAndStatus(palletBarcodeItem.get().getId(),true);
            palletBarcodeOrderRelList.forEach(item -> {
                PalletInfoDTO dto = new PalletInfoDTO();
                Optional<AurOrderDetail> tmpDetail = aurOrderDetailService.findById(item.getAurTmpDetail().getId());
                Optional<AurOrderMaster> master = aurOrderMasterService.findById(item.getAurOrder().getId());
                if(tmpDetail.isPresent()){
                    dto.setBarcode(tmpDetail.get().getBarkod());
                    dto.setStockCode(tmpDetail.get().getStokKodu());
                    dto.setAmount(tmpDetail.get().getTeslimMiktar());
                    dto.setOrderNo(tmpDetail.get().getSiparisNo());
                    dto.setCariName(master.get().getFirmName());
                    dto.setPalletBarcodeOrderRelId(item.getId());
                    dto.setAurTmpDetailId(tmpDetail.get().getId());
                    dto.setStockName(tmpDetail.get().getStokAdi());

                    response.add(dto);
                }
            });

            return response;
        }
        else{
            throw new BadRequestAlertException("Invalid pallet-barcode",ENTITY_NAME,"invalid key");
        }
    }









}
