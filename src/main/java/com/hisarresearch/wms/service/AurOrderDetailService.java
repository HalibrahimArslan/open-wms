package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.enumeration.OrderStatus;
import com.hisarresearch.wms.domain.AurPartialDetails;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.repository.AurOrderDetailRepository;
import com.hisarresearch.wms.service.dto.AurOrderDetailDTO;
import com.hisarresearch.wms.service.mapper.AurOrderDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AurOrderDetailService {
    private static final String ENTITY_NAME = "AurOrderDetail";

    private final Logger log = LoggerFactory.getLogger(AurOrderDetailService.class);

    private final AurOrderDetailRepository aurOrderDetailRepository;

    private final AurOrderDetailMapper  aurOrderDetailMapper;

    private final AurPartialItemService aurPartialItemService;

    public AurOrderDetailService(AurOrderDetailRepository aurOrderDetailRepository, AurOrderDetailMapper aurOrderDetailMapper, AurPartialItemService aurPartialItemService) {
        this.aurOrderDetailRepository = aurOrderDetailRepository;
        this.aurOrderDetailMapper = aurOrderDetailMapper;
        this.aurPartialItemService = aurPartialItemService;
    }

    @Transactional
    public List<AurOrderDetail> findByAurOrderId(Long aurOrderId) {
        return aurOrderDetailRepository.findByOrderId(aurOrderId);
    }

    @Transactional
    public Optional<AurOrderDetail> findById(Long id) {
        return aurOrderDetailRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public List<AurOrderDetail> findBySipUidInAndIsPiece(List<String> sipUids,boolean isPiece) {
        return aurOrderDetailRepository.findBySipUidInAndIsPiece(sipUids,isPiece);
    }

    @Transactional(readOnly = true)
    public List<AurOrderDetail> findBySipUidInAndIsPieceAndOrderIdAndStatus(List<String> sipUids,boolean isPiece, Long orderId,String status) {
        return aurOrderDetailRepository.findBySipUidInAndIsPieceAndOrderIdAndStatus(sipUids,isPiece,orderId,status);
    }

    @Transactional
    public List<AurOrderDetail> findBySipUidAndStatus(String sipUid, String status) {
        return aurOrderDetailRepository.findBySipUidAndStatus(sipUid, status);
    }

    @Transactional
    public List<AurOrderDetail> findBySipUidIn(List<String> sipUids) {
        return aurOrderDetailRepository.findBySipUidIn(sipUids);
    }

    @Transactional
    public List<AurOrderDetail> findByOrderNoAndStatus(String orderNo, String status) {
        return aurOrderDetailRepository.findBySiparisNoAndStatus(orderNo, status);
    }

    @Transactional
    public List<AurOrderDetail> findByOrderNoAndStatusList(String orderNo, List<String> statusList) {
        return aurOrderDetailRepository.findBySiparisNoAndStatusIn(orderNo, statusList);
    }

    @Transactional
    public List<AurOrderDetail> findByOrderIdAndStatus(Long aurOrderId, String status) {
        return aurOrderDetailRepository.findByOrderIdAndStatus(aurOrderId,status);
    }


    @Transactional
    public List<AurOrderDetail> findByStokKoduAndOrderId(String stokKodu, Long orderId) {
        return aurOrderDetailRepository.findByStokKoduAndOrderId(stokKodu,orderId);
    }

    @Transactional
    public Optional<AurOrderDetail> findBySipUidAndOrderId(String sipUid, Long aurOrderId) {
        return aurOrderDetailRepository.findBySipUidAndOrderId(sipUid,aurOrderId);
    }

    @Transactional
    public Optional<AurOrderDetail> findBySipUidAndOrderIdAndAurPartialItemIdAndBarkod(String sipUid, Long aurOrderId, Long aurPartialItemId, String barcode) {
        return aurOrderDetailRepository.findBySipUidAndOrderIdAndAurPartialItemIdAndBarkod(sipUid, aurOrderId,  aurPartialItemId,  barcode);
    }

    @Transactional
    public List<AurOrderDetail> findByOrderIdAndStatusNot(Long aurOrderId, String status) {
        return aurOrderDetailRepository.findByOrderIdAndStatusNot(aurOrderId,status);
    }

    @Transactional
    public Optional<AurOrderDetail> findBySipUidAndOrderIdAndBarkod(String sipUid, Long aurOrderId, String barcode) {
        return aurOrderDetailRepository.findBySipUidAndOrderIdAndBarkod(sipUid,aurOrderId,barcode);
    }

    @Transactional
    public List<AurOrderDetail> findByOrderIdAndSiparisNoAndStatus(Long aurOrderId, String siparisNo, String status) {
        return aurOrderDetailRepository.findByOrderIdAndSiparisNoAndStatus(aurOrderId,siparisNo,status);
    }


    @Transactional
    public List<AurOrderDetail> findByOrderId(Long orderId) {
        return aurOrderDetailRepository.findByOrderId(orderId);
    }

    @Transactional
    public List<AurOrderDetail> findActiveByOrderId(Long orderId) {
        return aurOrderDetailRepository.findByOrderId(orderId).stream()
            .filter(detail -> !OrderStatus.SUSPENDED.name().equals(detail.getStatus()))
            .collect(Collectors.toList());
    }

    @Transactional
    public List<AurOrderDetail> findBySiparisNoInAndStatusIn(List<String> orderNos, List<String> statusList) {
        return aurOrderDetailRepository.findBySiparisNoInAndStatusIn(orderNos,statusList);
    }

    @Transactional
    public Optional<AurOrderDetail> findBySipUidAndOrderIdAndAurPartialItemId(String sipUid, Long aurOrderId, Long aurPartialItemId) {
        return aurOrderDetailRepository.findBySipUidAndOrderIdAndAurPartialItemId(sipUid,aurOrderId,aurPartialItemId);
    }

    @Transactional
    public List<AurOrderDetail> findBySipUidInAndStatusIn(List<String> sipUids, List<String> statuses) {
        return aurOrderDetailRepository.findBySipUidInAndStatusIn(sipUids, statuses);
    }


    public AurOrderDetail save(AurOrderDetail detail){
        return aurOrderDetailRepository.save(detail);
    }

    public AurOrderDetail save(AurOrderDetailDTO detail){
        return aurOrderDetailRepository.save(aurOrderDetailMapper.toEntity(detail));
    }

    public List<AurOrderDetail> saveAll(List<AurOrderDetail> details){
        return aurOrderDetailRepository.saveAll(details);
    }


    public Optional<AurOrderDetail> partialUpdate(AurOrderDetail aurOrderDetail) {
        log.debug("Request to partially update order detail : {}", aurOrderDetail);
        return aurOrderDetailRepository.findById(aurOrderDetail.getId()).map(existingAurTmpDetail -> {
            if (aurOrderDetail.getOrder() != null) {
                existingAurTmpDetail.setOrder(aurOrderDetail.getOrder());
            }
            if (aurOrderDetail.getStatus() != null) {
                existingAurTmpDetail.setStatus(aurOrderDetail.getStatus());
            }
            if (aurOrderDetail.getStokKodu() != null) {
                existingAurTmpDetail.setStokKodu(aurOrderDetail.getStokKodu());
            }
            if (aurOrderDetail.getBarkod() != null && !aurOrderDetail.getBarkod().isEmpty()) {
                existingAurTmpDetail.setBarkod(aurOrderDetail.getBarkod());
            }
            if (aurOrderDetail.getStokBirimi() != null) {
                existingAurTmpDetail.setStokBirimi(aurOrderDetail.getStokBirimi());
            }
            if (aurOrderDetail.getSiparisMiktar() != null) {
                existingAurTmpDetail.setSiparisMiktar(aurOrderDetail.getSiparisMiktar());
            }
            if (aurOrderDetail.getTeslimMiktar() != null) {
                existingAurTmpDetail.setTeslimMiktar(aurOrderDetail.getTeslimMiktar());
            }
            if (aurOrderDetail.getStokAdi() != null) {
                existingAurTmpDetail.setStokAdi(aurOrderDetail.getStokAdi());
            }
            if (aurOrderDetail.getSipUid() != null) {
                existingAurTmpDetail.setSipUid(aurOrderDetail.getSipUid());
            }
            if (aurOrderDetail.getSiparisNo() != null) {
                existingAurTmpDetail.setSiparisNo(aurOrderDetail.getSiparisNo());
            }
            if (aurOrderDetail.getObserverAmount() != null) {
                existingAurTmpDetail.setObserverAmount(aurOrderDetail.getObserverAmount());
            }
            if (aurOrderDetail.getPiece() != null) {
                existingAurTmpDetail.setPiece(aurOrderDetail.getPiece());
            }
            if (aurOrderDetail.getAurPartialItemId() != null) {
                existingAurTmpDetail.setAurPartialItemId(aurOrderDetail.getAurPartialItemId());
            }

            return existingAurTmpDetail;
        }).map(aurOrderDetailRepository::save);
    }

    @Transactional(readOnly = true)
    public void validatePartialItemsOnClose(Long orderMasterId) {
        List<AurOrderDetail> allDetails = aurOrderDetailRepository
            .findByOrderId(orderMasterId);

        Map<List<Object>, List<AurOrderDetail>> partialGroups = allDetails.stream()
            .filter(d -> Boolean.TRUE.equals(d.getPiece()))
            .filter(d -> d.getAurPartialItemId() != null)
            .filter(d -> d.getSipUid() != null)
            .collect(Collectors.groupingBy(
                d -> Arrays.asList(d.getSipUid(), d.getAurPartialItemId())
            ));

        List<String> errors = new ArrayList<>();
        for (Map.Entry<List<Object>, List<AurOrderDetail>> entry : partialGroups.entrySet()) {
            Object sipUid = entry.getKey().get(0);
            Long aurPartialItemId = (Long) entry.getKey().get(1);
            aurPartialItemService.validatePartialGroup(
                aurPartialItemId, entry.getValue(), errors
            );
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(
                "Parçalı ürün doğrulaması başarısız: " + String.join(" | ", errors),
                ENTITY_NAME
            );
        }
    }

    @Transactional
    public void updateOrderDetailsPartialItemsStockCode(AurPartialDetails aurPartialDetails) {
        List<AurOrderDetail> allDetails = aurOrderDetailRepository.findByBarkodAndStatusIn(aurPartialDetails.getBarcode(), Arrays.asList("OPEN", "IN_PROGRESS", "OUT_PROGRESS"));
        allDetails.forEach(item -> item.setStokKodu(aurPartialDetails.getStockCode()));
        aurOrderDetailRepository.saveAll(allDetails);
    }

}
