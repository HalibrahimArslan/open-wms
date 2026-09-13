package com.hisarresearch.wms.repository;


import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AurOrderDetailRepository extends JpaRepository<AurOrderDetail, Long>, JpaSpecificationExecutor<AurOrderDetail> {
    List<AurOrderDetail> findByStokKoduAndOrderId(String stokKodu, Long aurOrderId);
    List<AurOrderDetail> findByOrderId(Long aurOrderId);
    List<AurOrderDetail> findByOrderIdAndStatus(Long aurOrderId, String status);
    List<AurOrderDetail> findBySipUidIn(List<String> sipUids);
    List<AurOrderDetail> findBySipUidInAndIsPiece(List<String> sipUids,boolean isPiece);
    List<AurOrderDetail> findBySipUidInAndIsPieceAndOrderIdAndStatus(List<String> sipUids,boolean isPiece,Long orderId,String status);
    @EntityGraph(attributePaths = {"uniqueBarcodes","aurTmpDetailSktList"})
    List<AurOrderDetail> findByOrderIdAndSiparisNoAndStatus(Long aurOrderId, String siparisNo, String status);
    List<AurOrderDetail> findByOrderIdAndStatusNot(Long aurOrderId, String status);
    Optional<AurOrderDetail> findBySipUidAndOrderId(String sipUid, Long aurOrderId);
    Optional<AurOrderDetail> findBySipUidAndOrderIdAndAurPartialItemId(String sipUid, Long aurOrderId, Long aurPartialItemId);
    Optional<AurOrderDetail> findBySipUidAndOrderIdAndAurPartialItemIdAndBarkod(String sipUid, Long aurOrderId, Long aurPartialItemId, String barcode);
    List<AurOrderDetail> findBySipUidAndStatus(String sipUid, String status);
    Optional<AurOrderDetail> findBySipUidAndOrderIdAndBarkod(String sipUid, Long aurOrderId, String barcode);
    Optional<AurOrderDetail> findBySipUidAndOrderIdAndBarkodAndAurPartialItemId(String sipUid, Long aurOrderId, String barcode, Long aurPartialItemId);
    List<AurOrderDetail> findBySiparisNoAndStatus(String orderNo, String status);
    List<AurOrderDetail> findBySiparisNoAndStatusIn(String orderNo, List<String> statusList);
    List<AurOrderDetail> findBySiparisNoInAndStatusIn(List<String> orderNos, List<String> statusList);
    List<AurOrderDetail> findBySipUidInAndStatusIn(List<String> sipUids, List<String> statusList);
    List<AurOrderDetail> findByBarkodAndStatusIn(String barcode, List<String> statusList);

}
