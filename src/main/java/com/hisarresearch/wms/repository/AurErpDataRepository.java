package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurErpData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AurErpDataRepository extends JpaRepository<AurErpData,Long> {
}
