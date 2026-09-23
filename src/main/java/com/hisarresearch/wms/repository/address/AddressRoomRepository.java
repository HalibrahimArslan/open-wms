package com.hisarresearch.wms.repository.address;

import com.hisarresearch.wms.domain.address.AddressRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AddressRoomRepository extends JpaRepository<AddressRoom, Long> {
    List<AddressRoom> findByCompanyCodeAndDepoCode(String companyCode,String depoCode);
    List<AddressRoom> findByIdInAndCompanyCodeAndDepoCode(Collection<Long> ids, String companyCode, String depoCode);
}
