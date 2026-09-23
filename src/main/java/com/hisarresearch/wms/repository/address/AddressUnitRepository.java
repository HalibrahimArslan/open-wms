package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressUnitRepository extends JpaRepository<AddressUnit, Long> {
    List<AddressUnit> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    List<AddressUnit> findByIdInAndCompanyCodeAndDepoCode(Collection<Long> ids, String companyCode, String depoCode);
}
