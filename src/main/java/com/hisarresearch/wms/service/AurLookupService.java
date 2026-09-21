package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.repository.AurLookupTableRepository;
import com.hisarresearch.wms.service.dto.LookupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AurLookupService {
    private final Logger log = LoggerFactory.getLogger(AurLookupService.class);

    public AurLookupService(AurLookupTableRepository aurLookupTableRepository) {
        this.aurLookupTableRepository = aurLookupTableRepository;
    }

    private final AurLookupTableRepository aurLookupTableRepository;

    @Transactional
    public List<AurLookupTable> getByLookupName(String lookupName){
        log.debug("Request getByLookupName : {}", lookupName);
        return aurLookupTableRepository.findByLookupName(lookupName);

    }

    @Transactional
    public List<AurLookupTable> getByLookupNames(List<String> lookupNames){
        log.debug("Request getByLookupNames : {}", lookupNames);
        return aurLookupTableRepository.findByLookupNameIn(lookupNames);
    }

    @Transactional
    public List<AurLookupTable> getByLookupCode(String lookupCode){
        log.debug("Request getByLookupCode : {}", lookupCode);
        return aurLookupTableRepository.findByLookupCode(lookupCode);

    }

    public AurLookupTable createOrUpdateLookup(LookupDTO lookupDTO){
        if(lookupDTO.getId() == null){
            AurLookupTable aurLookupTable = new AurLookupTable();
            aurLookupTable.setLookupCode(lookupDTO.getLookupCode());
            aurLookupTable.setLookupName(lookupDTO.getLookupName());
            aurLookupTable.setLookupDescription(lookupDTO.getLookupDescription());
            return aurLookupTableRepository.save(aurLookupTable);
        }

        AurLookupTable lookupTable = aurLookupTableRepository.findById(lookupDTO.getId()).orElseThrow(()-> new EntityNotFoundException("AurLookupTable not found"));
        lookupTable.setLookupCode(lookupDTO.getLookupCode());
        lookupTable.setLookupDescription(lookupDTO.getLookupDescription());
        return lookupTable;
    }

    public void deleteLookupCode(Long id){
        aurLookupTableRepository.findById(id).ifPresent(lookup -> {
            aurLookupTableRepository.deleteById(id);
            log.debug("Deleted lookup : {}",lookup);
        });
    }
}
