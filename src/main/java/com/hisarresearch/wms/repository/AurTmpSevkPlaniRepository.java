package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.sevkplani.AurTmpSevkPlani;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AurTmpSevkPlaniRepository extends JpaRepository<AurTmpSevkPlani, Long> {
    List<AurTmpSevkPlani> findAll();

    boolean existsBySipUidAndWeek(String sipUid, int week);
}
