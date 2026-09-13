package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRoomRepository extends JpaRepository<AddressRoom, Long> {
    List<AddressRoom> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    @Query("SELECT ad FROM AddressRoom ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<AddressRoom> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
