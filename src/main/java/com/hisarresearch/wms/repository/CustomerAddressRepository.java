package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {
    Optional<CustomerAddress> findByCariCodeAndAddressId(String cariCode,Long addressId);
}
