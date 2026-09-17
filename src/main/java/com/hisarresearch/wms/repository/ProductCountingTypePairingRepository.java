package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.ProductCountingTypePairing;
import com.hisarresearch.wms.domain.enumeration.CountingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductCountingTypePairingRepository extends JpaRepository<ProductCountingTypePairing, Long>, JpaSpecificationExecutor<ProductCountingTypePairing> {
    List<ProductCountingTypePairing> findByCountingTypeAndWarehouseCodeAndProduct_Id_CompanyCode(CountingType countingType, int warehouseCode, String companyCode);
}
