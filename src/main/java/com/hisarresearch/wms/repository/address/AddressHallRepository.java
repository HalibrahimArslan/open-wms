package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressHallRepository extends JpaRepository<AddressHall, Long> {
    List<AddressHall> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    List<AddressHall> findByIdInAndCompanyCodeAndDepoCode(Collection<Long> ids, String companyCode, String depoCode);
}
