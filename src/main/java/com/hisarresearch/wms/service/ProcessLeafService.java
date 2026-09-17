package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.ProcessLeaf;
import com.hisarresearch.wms.domain.ProcessTree;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.repository.ProcessLeafRepository;
import com.hisarresearch.wms.repository.address.AurDepoAdresRepository;
import com.hisarresearch.wms.service.dto.process.ProcessLeafCreateDto;
import com.hisarresearch.wms.service.dto.process.ProcessLeafDto;
import com.hisarresearch.wms.service.mapper.ProcessLeafMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.CacheManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProcessLeafService {

    private final Logger log = LoggerFactory.getLogger(ProcessLeafService.class);

    @Autowired
    private  ProcessLeafRepository processLeafRepository;

    @Autowired
    private  ProcessTreeService processTreeService;

    @Autowired
    private  AurDepoAdresRepository aurDepoAdresRepository;

    @Autowired
    private  CacheManager cacheManager;

    @Autowired
    private  ProcessLeafMapper processLeafMapper;


    public List<ProcessLeaf> getAllProcessLeaves(){
        log.debug("Get all process leaves service method is running");
        return processLeafRepository.findAll();
    }

    @Transactional
    public Optional<ProcessLeaf> findById(Long id){
        return processLeafRepository.findById(id);
    }

    @Transactional
    public List<ProcessLeaf> findByProcessTreeIdAndStatus(Long processTreeId,Boolean status){
        return processLeafRepository.findByProcess_IdAndStatus(processTreeId, status);
    }

    public ProcessLeaf createProcessLeaf(ProcessLeafCreateDto processLeafCreateDto){
        processTreeService.findById(processLeafCreateDto.getProcessId())
            .orElseThrow(() -> new RuntimeException("There is no process tree related process id " + processLeafCreateDto.getProcessId()));

        Optional<ProcessLeaf> search = processLeafRepository.findByProcess_IdAndBarcodeAndStatus(processLeafCreateDto.getProcessId(), processLeafCreateDto.getBarcode(),true);
        if(search.isPresent()){
            throw new RuntimeException("There is a process leaf related barcode : " + search.get().getBarcode());
        }

        ProcessLeaf processLeaf= new ProcessLeaf();
        ProcessTree process = new ProcessTree();
        process.setId(processLeafCreateDto.getProcessId());
        processLeaf.setProcess(process);
        processLeaf.setStatus(processLeafCreateDto.getStatus());
        processLeaf.setBarcode(processLeafCreateDto.getBarcode());
        AurDepoUrunAdres aurDepoUrunAdres = new AurDepoUrunAdres();
        aurDepoUrunAdres.setUrunAdresId(processLeafCreateDto.getAddressId());
        processLeaf.setAddress(aurDepoUrunAdres);
        processLeaf.setAmount(processLeafCreateDto.getAmount());

        ProcessLeaf savedOne = processLeafRepository.save(processLeaf);
        savedOne.setAddress(aurDepoAdresRepository.findByUrunAdresId(savedOne.getAddress().getUrunAdresId()));
        cacheManager.getCache("com.hisarresearch.wms.domain.ProcessTree.processLeaves").clear();

        return savedOne;

    }

    public void saveBulkProcessTree(List<ProcessLeafCreateDto> processLeafCreateDtoList){
        processLeafCreateDtoList.forEach(this::createProcessLeaf);
    }

    /**
     * Partially update a process leaf.
     *
     * @param processLeafCreateDtO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProcessLeaf> partialUpdate(ProcessLeafCreateDto processLeafCreateDtO){
        log.debug("Request to partially update process leaf : {}", processLeafCreateDtO);

        return processLeafRepository.findById(processLeafCreateDtO.getId())
            .map(
                existingItem -> {
                    if(processLeafCreateDtO.getAddressId() != null){
                        AurDepoUrunAdres aurDepoUrunAdres = new AurDepoUrunAdres();
                        aurDepoUrunAdres.setUrunAdresId(processLeafCreateDtO.getAddressId());
                        existingItem.setAddress(aurDepoUrunAdres);
                    }
                    if(processLeafCreateDtO.getStatus() != null){
                        existingItem.setStatus(processLeafCreateDtO.getStatus());
                    }
                    if(processLeafCreateDtO.getAmount() != null){
                        existingItem.setAmount(processLeafCreateDtO.getAmount());
                    }
                    return existingItem;
                })
            .map(processLeafRepository::save);

    }

    public ProcessLeafDto getOneById(Long id){
       Optional<ProcessLeaf> processLeaf =  findById(id);
       if(processLeaf.isPresent()){
           return processLeafMapper.toDto(processLeaf.get());
       }
       throw new RuntimeException("There is no entity related id" + id);
    }

}
