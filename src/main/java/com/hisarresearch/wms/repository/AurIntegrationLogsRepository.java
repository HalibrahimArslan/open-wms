package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurIntegrationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AurIntegration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurIntegrationLogsRepository extends JpaRepository<AurIntegrationLogs, Long>, JpaSpecificationExecutor<AurIntegrationLogs> {}
