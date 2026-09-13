package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.ErpJwtData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ErpJwtDataRepository extends JpaRepository<ErpJwtData,Long> {
    Optional<ErpJwtData> findByErpTipi(String erpTipi);

}
