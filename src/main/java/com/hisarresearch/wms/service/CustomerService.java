package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Customer;
import com.hisarresearch.wms.repository.CustomerRepository;
import com.hisarresearch.wms.service.dto.CustomerDTO;
import com.hisarresearch.wms.service.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hisarresearch.wms.domain.Customer_.customerCode;

@Service
@Transactional
public class CustomerService {
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<Customer> getAll(){
        log.debug("Request to getAll customers");
        return customerRepository.findAll();
    }

    @Transactional
    public List<Customer> getByCustomerCode(String customerCode) {
        log.debug("Request to getByCustomerCode customer");
        return customerRepository.findByCustomerCode(customerCode);
    }

    @Transactional
    public List<Customer> getByCompanyCode(String companyCode) {
        log.debug("Request to getByCompanyCode customer");
        return customerRepository.findByCompanyCode(companyCode);
    }

    @Transactional
    public List<Customer> getByDistrictCode(int districtCode) {
        log.debug("Request to getByDistrictCode VendorMailAddresses");
        return customerRepository.findByDistrictCode(districtCode);
    }

    public Customer save(CustomerDTO customerDTO){
        log.debug("Request to save VendorMailAddress");
        Customer createOne = customerMapper.toEntity(customerDTO);
        return customerRepository.save(createOne);
    }

    public void deleteById(Long id){
        log.debug("Request to delete VendorMailAddress");
        customerRepository.deleteById(id);
    }
}
