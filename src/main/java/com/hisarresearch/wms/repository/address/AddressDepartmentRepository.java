package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDepartmentRepository extends JpaRepository<AddressDepartment, Long> {
    List<AddressDepartment> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    @Query("SELECT ad FROM AddressDepartment ad WHERE ad.id >= :startId AND ad.id <= :endId")
    List<AddressDepartment> findByIdBetween(@Param("startId") Long startId, @Param("endId") Long endId);
}
