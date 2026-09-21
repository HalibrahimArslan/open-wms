package com.hisarresearch.wms.service;

import org.springframework.context.annotation.Lazy;

import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.domain.CustomerAddress;
import com.hisarresearch.wms.repository.CustomerAddressRepository;
import com.hisarresearch.wms.service.dto.AurOrderMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CustomerAddressService {
    private final Logger log = LoggerFactory.getLogger(CustomerAddressService.class);

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Lazy
    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    public CustomerAddress saveCustomerAddress(AurOrderMasterDTO dto){
        log.debug("Customer address save service is started");
        Optional<CustomerAddress> searchAddress =  customerAddressRepository.findByCariCodeAndAddressId(dto.getFirmCode(), dto.getSevkAddressId());

        if(searchAddress.isPresent()){
            CustomerAddress customerAddress = searchAddress.get();
            customerAddress.setSevkAddress(dto.getSevkAddress());
            customerAddress.setSevkTel(dto.getSevkTel());
            customerAddress.setSevkMuhatap(dto.getSevkMuhatap());
            customerAddress.setSevkAcikAdress(dto.getSevkAcikAdres());
            return customerAddress;
        }
        else{
            CustomerAddress customerAddress = new CustomerAddress();
            customerAddress.setAddressId(dto.getSevkAddressId());
            customerAddress.setCariCode(dto.getFirmCode());
            customerAddress.setSevkAddress(dto.getSevkAddress());
            customerAddress.setSevkTel(dto.getSevkTel());
            customerAddress.setSevkMuhatap(dto.getSevkMuhatap());
            customerAddress.setSevkAcikAdress(dto.getSevkAcikAdres());

            return customerAddressRepository.save(customerAddress);

        }
    }

    @Transactional
    public Optional<CustomerAddress> findByCariCodeAndAddressId(String cariCode,Long addressId){
        return customerAddressRepository.findByCariCodeAndAddressId(cariCode,addressId);
    }

    public String getSevkAddressFromOrder(Long aurOrderId){
        String sevkAddress = "";
        Optional<AurOrderMaster> aom = aurOrderMasterService.findById(aurOrderId);
        if(aom.isPresent()){
            Optional<CustomerAddress> customerAddress = findByCariCodeAndAddressId(aom.get().getFirmCode(),aom.get().getSevkAddressId());
            sevkAddress = customerAddress.isPresent() ? customerAddress.get().getSevkAddress() : "NotFound";
        }

        return sevkAddress;
    }

    public String getSevkMusteriTel(Long aurOrderId){
        String musteriTel = "";
        Optional<AurOrderMaster> aom = aurOrderMasterService.findById(aurOrderId);
        if(aom.isPresent()){
            Optional<CustomerAddress> customerAddress = findByCariCodeAndAddressId(aom.get().getFirmCode(),aom.get().getSevkAddressId());
            musteriTel = customerAddress.isPresent() ? customerAddress.get().getSevkTel() : "NotFound";
        }

        return musteriTel;
    }

    public Optional<CustomerAddress> getCustomerAddress(Long aurOrderId){
        AurOrderMaster aom = aurOrderMasterService.findById(aurOrderId).orElse(null);
        if(aom == null){
            return Optional.empty();
        }
        return findByCariCodeAndAddressId(aom.getFirmCode(),aom.getSevkAddressId());


    }
}
