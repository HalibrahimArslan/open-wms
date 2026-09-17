package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurDispatchAreaControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AurDispatchAreaControlRepository extends JpaRepository<AurDispatchAreaControl, Long>{
    Optional<AurDispatchAreaControl> findByAurTmpDetail_IdAndStatus(Long aurTmpDetailId, Boolean status);
}
