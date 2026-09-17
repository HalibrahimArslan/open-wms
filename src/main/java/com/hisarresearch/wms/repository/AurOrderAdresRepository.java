package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurOrderAdres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AurOrderAdresRepository extends JpaRepository<AurOrderAdres, Long> {

    AurOrderAdres findByErpOrderInfo(Long aurOrderId);

    AurOrderAdres findByMagentoOrderId(Long magentoOrderId);

    AurOrderAdres save(AurOrderAdres orderAdres);

}
