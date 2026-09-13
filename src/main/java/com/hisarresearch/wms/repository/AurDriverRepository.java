package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurDriver;
import com.hisarresearch.wms.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AurDriverRepository extends JpaRepository<AurDriver, Long>, JpaSpecificationExecutor<AurDriver> {
    Optional<AurDriver> findByPhoneNumber(String phoneNumber);
    List<AurDriver> findByDriverNameContainingIgnoreCase(String driverName);
    List<AurDriver> findTop5ByOrderByLastModifiedDateDesc();
}
