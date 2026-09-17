package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.sevkplani.AurSevkPlani;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AurSevkPlaniRepository extends JpaRepository<AurSevkPlani, Long> {
}

