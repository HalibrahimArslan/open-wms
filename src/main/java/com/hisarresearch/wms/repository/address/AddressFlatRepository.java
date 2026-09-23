package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressFlat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressFlatRepository extends JpaRepository<AddressFlat, Long> {
    List<AddressFlat> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    List<AddressFlat> findByIdInAndCompanyCodeAndDepoCode(Collection<Long> ids, String companyCode, String depoCode);
}

