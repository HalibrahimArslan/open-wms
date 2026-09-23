package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressDepartmentRepository extends JpaRepository<AddressDepartment, Long> {
    List<AddressDepartment> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    List<AddressDepartment> findByIdInAndCompanyCodeAndDepoCode(Collection<Long> ids, String companyCode, String depoCode);
}
