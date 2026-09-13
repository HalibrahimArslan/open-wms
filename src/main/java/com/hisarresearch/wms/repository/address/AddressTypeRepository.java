package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, Long> {
    List<AddressType> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    @Query("SELECT ad FROM AddressType ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<AddressType> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
