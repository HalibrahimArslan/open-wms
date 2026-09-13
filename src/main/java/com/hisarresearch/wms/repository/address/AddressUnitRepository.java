package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressUnitRepository extends JpaRepository<AddressUnit, Long> {
    List<AddressUnit> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    @Query("SELECT ad FROM AddressUnit ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<AddressUnit> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
