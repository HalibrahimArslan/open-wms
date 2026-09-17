package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.domain.CountingAddressException;
import com.hisarresearch.wms.repository.CountingAddressExceptionRepository;
import com.hisarresearch.wms.service.dto.CountingAddressExceptionDto;
import com.hisarresearch.wms.service.mapper.CountingAddressExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CountingAddressExceptionService {
    private final Logger log = LoggerFactory.getLogger(CountingAddressExceptionService.class);

    private final CountingAddressExceptionRepository countingAddressExceptionRepository;

    private final AddressService addressService;

    private final CountingAddressExceptionMapper countingAddressExceptionMapper;

    public CountingAddressExceptionService(CountingAddressExceptionRepository countingAddressExceptionRepository, AddressService addressService,
                                           CountingAddressExceptionMapper countingAddressExceptionMapper) {
        this.countingAddressExceptionRepository = countingAddressExceptionRepository;
        this.addressService = addressService;
        this.countingAddressExceptionMapper = countingAddressExceptionMapper;
    }

    public List<CountingAddressException> getCountingAddressExceptionListByCountingDefinitionId(long countingDefinitionId){
        log.debug("CountingAddressException list service is started");
        return countingAddressExceptionRepository.findByCountingDefinition_IdAndStatus(countingDefinitionId,true);
    }

    public CountingAddressException saveCountingAddressException(CountingAddressExceptionDto saveDto){
        log.debug("CountingAddressException save service is started");

        CountingAddressException countingAddressException = countingAddressExceptionMapper.toEntity(saveDto);
        countingAddressException.setStatus(saveDto.getStatus());
        countingAddressException.setAddress(addressService.isExistAddress(saveDto.getAddressId()));

        return countingAddressExceptionRepository.save(countingAddressException);

    }

    public List<CountingAddressException> saveCountingAddressExceptionBulk(List<CountingAddressExceptionDto> countingAddressExceptionDtoList){
        log.debug("CountingAddressException  bulk save service is started");
        List<CountingAddressException> savedList = new ArrayList<>();
        countingAddressExceptionDtoList.forEach(item -> {
            Optional<CountingAddressException> searchCountingAddressException = countingAddressExceptionRepository.findByAddress_UrunAdresIdAndCountingDefinition_IdAndStatusTrue(item.getAddressId(),item.getCountingDefinitionId());
            if(searchCountingAddressException.isEmpty()){
                CountingAddressException newEntity = countingAddressExceptionMapper.toEntity(item);
                CountingAddressException savedEntity = countingAddressExceptionRepository.save(newEntity);
                savedEntity.setAddress(addressService.isExistAddress(savedEntity.getAddress().getUrunAdresId()));
                savedList.add(savedEntity);
            }
        });

        return savedList;
    }

    public void saveCountingDefinition(AurSayimTanim countingDefinition, String depoNo){
        log.debug("Save addresses by counting  definition");
        List<CountingAddressException> countingAddressExceptionList = new ArrayList<>();

        addressService.getNoneCountableAddresses(depoNo).forEach(address -> {
            CountingAddressException countingAddressException = new CountingAddressException();
            countingAddressException.setStatus(true);
            countingAddressException.setAddress(address);
            countingAddressException.setCountingDefinition(countingDefinition);
            countingAddressExceptionList.add(countingAddressException);
        });

        countingAddressExceptionRepository.saveAll(countingAddressExceptionList);
    }
    @Transactional
    public Optional<CountingAddressException> findByAddressAndCountingDefinitionAndStatusTrue(long addressId, long countingDefinitionId){
        return countingAddressExceptionRepository.findByAddress_UrunAdresIdAndCountingDefinition_IdAndStatusTrue(addressId,countingDefinitionId);
    }

    public void deleteOne(Long id){
        CountingAddressException deleteOne = countingAddressExceptionMapper.fromId(id);
        countingAddressExceptionRepository.delete(deleteOne);
    }

}
