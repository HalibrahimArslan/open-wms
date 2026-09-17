package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressFlat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressFlatRepository extends JpaRepository<AddressFlat, Long> {
    List<AddressFlat> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);

    @Query("SELECT ad FROM AddressFlat ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<AddressFlat> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);}

