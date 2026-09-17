package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.Warehouse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Warehouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>,JpaSpecificationExecutor<Warehouse> {
    Optional<Warehouse> findByCode(String code);
    Optional<Warehouse> findByName(String name);
    Optional<Warehouse> findByCodeAndCompanyCode(String code,String companyCode);
    Optional<Warehouse> findByAutoScan(boolean autoScan);


}
