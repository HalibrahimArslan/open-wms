package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AurReserveRepository extends JpaRepository<AurReserve, Long> {
    List<AurReserve> findByProduct_Id_CompanyCode(String companyCode);
    List<AurReserve> findByProduct_Id_BarkodInAndOrderNoIn(List<String> barcode, List<String> orderNo);
}
