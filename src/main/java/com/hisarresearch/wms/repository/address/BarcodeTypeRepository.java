package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.BarcodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeTypeRepository extends JpaRepository<BarcodeType, Long> {
    List<BarcodeType> findByCompanyCodeAndDepoCode(String depoCode, String companyCode);
    @Query("SELECT ad FROM BarcodeType ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<BarcodeType> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
