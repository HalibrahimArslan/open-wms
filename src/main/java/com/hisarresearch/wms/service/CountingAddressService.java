package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.CountingAddressException;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CountingAddressService {
    private final Logger log = LoggerFactory.getLogger(CountingAddressService.class);

    private final AddressService addressService;

    private final CountingAddressExceptionService exceptionService;

    public CountingAddressService(AddressService addressService, CountingAddressExceptionService exceptionService) {
        this.addressService = addressService;
        this.exceptionService = exceptionService;
    }

    public Long checkCountableAddress(AurDepoUrunAdres aurDepoUrunAdres,long countingDefinitionId) {
        AurDepoUrunAdres address = addressService.findByAddressAndDepoCodeAndCompanyCodeAndStatus(aurDepoUrunAdres.getAdres(),aurDepoUrunAdres.getDepoNo(),true)
            .orElseThrow(InvalidAddressException::new);
        Optional<CountingAddressException> exceptionAddress = exceptionService.findByAddressAndCountingDefinitionAndStatusTrue(address.getUrunAdresId(),countingDefinitionId);
        if(exceptionAddress.isPresent()){
            throw new RuntimeException("Related address: " + aurDepoUrunAdres.getAdres() + " is not countable");
        }
        return address.getUrunAdresId();
    }

}
