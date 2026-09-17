package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurOrderMaster;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AurOrderMasterRepository extends JpaRepository<AurOrderMaster, Long>, JpaSpecificationExecutor<AurOrderMaster> {
    Optional<AurOrderMaster> findByOrderInfo(String orderInfo);
    Optional<AurOrderMaster> findByOrderInfoAndAurUser_IdAndOpTypeAndStatusIn(String orderInfo,Long userId,String operationType,List<String> statuses);
    List<AurOrderMaster> findByOrderInfoIn(List<String> orderInfos);

    List<AurOrderMaster> findByFirmCodeAndStatusNotAndOpTypeAndDepoNo(String firmCode, String status, String opType, Integer depoNo);

    List<AurOrderMaster> findByFirmCodeAndStatusAndOpTypeAndDepoNoAndAurUserId(String firmCode, String status, String opType, Integer depoNo, Long aurUserId);

    List<AurOrderMaster> findByFirmCodeAndStatusAndOpTypeAndDepoNo(String firmCode, String status, String opType, Integer depoNo);

    List<AurOrderMaster> findByFirmCodeAndOpTypeAndDepoNoOrderByLastModifiedDateDesc(String firmCode,String opType, Integer depoNo);

    List<AurOrderMaster> findByStatusAndOpTypeAndControlAddressAndOrderDepoCode(String status, String opType, AurDepoUrunAdres controlAddress,Integer depoCode);

    List<AurOrderMaster> findByOpTypeAndDepoNoAndAurUserIdAndStatusIn(String opType, Integer depoNo, Long aurUserId, Collection<String> status);

    Optional<AurOrderMaster> findTopByOrderByIdDesc();

    List<AurOrderMaster> findByCreatedDateBetween(Instant startDate, Instant endDate);

    Optional<AurOrderMaster> findByBelgeNo(String documentNo);

    List<AurOrderMaster> findByStatusAndFirmCodeAndBaglantiTipiAndCariCodeAndDepoNo(String status,String firmCode,String connectionType,String cariCode,Integer depoNo);

    @EntityGraph(attributePaths = {"details"})
    List<AurOrderMaster> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = "details")
    Optional<AurOrderMaster> findWithDetailsById(Long id);


}
