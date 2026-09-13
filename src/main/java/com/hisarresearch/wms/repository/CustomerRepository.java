package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCustomerCode(String customerCode);
    List<Customer> findByDistrictCode(int districtCode);
    List<Customer> findByCompanyCode(String companyCode);
}
