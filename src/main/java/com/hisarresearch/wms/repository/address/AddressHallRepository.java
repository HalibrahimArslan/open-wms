package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressHallRepository extends JpaRepository<AddressHall, Long> {
    List<AddressHall> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    @Query("SELECT ad FROM AddressHall ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<AddressHall> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
