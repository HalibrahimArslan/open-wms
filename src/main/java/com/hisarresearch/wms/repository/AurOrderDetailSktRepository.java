package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurOrderDetailSkt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AurOrderDetailSktRepository extends JpaRepository<AurOrderDetailSkt, Long> {
    Optional<AurOrderDetailSkt> findByAurTmpDetail_IdAndSktDate(Long aurTmpDetailId, Instant sktDate);
    List<AurOrderDetailSkt> findByAurTmpDetail_Id(Long aurTmpDetailId);
}
