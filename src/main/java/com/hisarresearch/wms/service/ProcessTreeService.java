package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.ProcessType;
import com.hisarresearch.wms.repository.ProcessLeafRepository;
import com.hisarresearch.wms.repository.ProcessTreeRepository;
import com.hisarresearch.wms.repository.address.AurDepoAdresRepository;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.service.dto.ProductAddressSaveDTO;
import com.hisarresearch.wms.service.dto.process.ProcessTreeCreateDto;
import com.hisarresearch.wms.service.dto.process.ProcessTreeDto;
import com.hisarresearch.wms.service.mapper.ProcessTreeMapper;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessTreeService {
    private final Logger log = LoggerFactory.getLogger(ProcessTreeService.class);
    @Autowired
    private  ProcessTreeRepository processTreeRepository;

    @Autowired
    private  UserService userService;

    @Autowired
    private  AurPartialItemService aurPartialItemService;

    @Autowired
    private  ProcessLeafService processLeafService;

    @Autowired
    private  AurPartialDetailsService aurPartialDetailsService;

    @Autowired
    private  AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private  ProcessTreeMapper processTreeMapper;

    @Autowired
    private  ProcessLeafRepository processLeafRepository;

    @Autowired
    private  AurDepoAdresRepository aurDepoAdresRepository;

    @Autowired
    private  AddressService addressService;

    @Transactional
    public Optional<ProcessTree> findById(Long id){
        return processTreeRepository.findById(id);
    }

    public ProcessTreeDto findByProcessChildId(Long processChildId, Long depoCode){
        Integer companyCode = userService.getUserCompanyCode();
        Optional<ProcessTree> processTree =  processTreeRepository.findByProcessChildIdAndStatusAndDepoCodeAndCompanyCode(processChildId,true,depoCode, Long.valueOf(companyCode));
        return processTree.map(processTreeMapper::toDto).orElse(null);
    }


    public ProcessTreeDto createProcessTree(ProcessTreeDto processTreeDto){
        Integer companyCode = userService.getUserCompanyCode();
        ProcessTree processTree = processTreeMapper.toEntity(processTreeDto);
        Optional<ProcessTree> processOptional = processTreeRepository.findByProcessChildIdAndStatusAndDepoCodeAndCompanyCode(processTreeDto.getProcessChildId(), true, processTreeDto.getDepoCode(), Long.valueOf(companyCode));

        if(processOptional.isPresent()){
            throw new RuntimeException("There is a running process related to partial item id " + processTreeDto.getProcessChildId());
        }


        aurPartialItemService.findOne(processTreeDto.getProcessChildId())
            .orElseThrow(()-> new RuntimeException("There is no partial item related id " + processTreeDto.getProcessChildId()));

        if(processTree.getTargetAddress() != null){
            processTree.setTargetAddress(aurDepoAdresRepository.findByUrunAdresId(processTree.getTargetAddress().getUrunAdresId()));
        }

        processTree.setPublic(processTreeDto.getPublic() != null ? processTreeDto.getPublic() : false);
        processTree.setCompanyCode(Long.valueOf(companyCode));
        processTree.setStatus(true);

        processTree = processTreeRepository.save(processTree);
        ProcessTree finalProcess = processTree;
        processTree.getProcessLeaves().forEach(leaf ->{
            leaf.setProcess(finalProcess);
            leaf.setAddress(aurDepoAdresRepository.findByUrunAdresId(leaf.getAddress().getUrunAdresId()));
            processLeafRepository.save(leaf);
        });

        return processTreeMapper.toDto(finalProcess);
    }

    /**
     * Partially update a process tree.
     *
     * @param processTreeCreateDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProcessTree> partialUpdate(ProcessTreeCreateDto processTreeCreateDto){
        log.debug("Request to partially update process tree : {}", processTreeCreateDto);

        return processTreeRepository.findById(processTreeCreateDto.getId())
            .map(
                existingItem -> {
                    if(processTreeCreateDto.getTargetAddress() != null){
                        Optional<AurDepoUrunAdres> searchAdres= addressService.findById(processTreeCreateDto.getTargetAddress());
                        searchAdres.orElseThrow(InvalidAddressException::new);
                        existingItem.setTargetAddress(searchAdres.get());
                    }
                    if(processTreeCreateDto.getPublic() != null){
                        existingItem.setPublic(processTreeCreateDto.getPublic());
                    }
                    if(processTreeCreateDto.getStatus() != null){
                        existingItem.setStatus(processTreeCreateDto.getStatus());
                    }
                    if(processTreeCreateDto.getAmount() != null){
                        existingItem.setAmount(processTreeCreateDto.getAmount());
                    }
                    return existingItem;
                })
            .map(processTreeRepository::save);

    }

    public void completeProcessTree(Long processTreeId) throws CloneNotSupportedException {
        Optional<ProcessTree> processTree = processTreeRepository.findById(processTreeId);
        processTree.orElseThrow(() -> new RuntimeException("There is no related process" + processTreeId));
        Double processAmount = processTree.get().getAmount();
        String depoCode = String.valueOf(processTree.get().getDepoCode());
        Optional<AurPartialItemDTO> searchPartialItem = aurPartialItemService.findOne(processTree.get().getProcessChildId());
        List<AurPartialDetails> partialDetails = aurPartialDetailsService.findByAurPartialItemId(processTree.get().getProcessChildId());
        if(partialDetails.isEmpty() || searchPartialItem.isEmpty()){
            throw new RuntimeException("There is a error at partial item id at " + processTree.get().getProcessChildId() );
        }
        List<ProcessLeaf> processLeaves = processLeafService.findByProcessTreeIdAndStatus(processTreeId,true);

        partialDetails.forEach(partialItem -> {
            List<ProcessLeaf> filteredList = processLeaves.stream()
                .filter(q -> q.getBarcode().equals(partialItem.getBarcode())).collect(Collectors.toList());
            Double totalAmount = filteredList.stream().mapToDouble(ProcessLeaf::getAmount).sum();
            if(processAmount != (totalAmount * partialItem.getQuantity())){
                throw new RuntimeException("There is a limit execution");
            }
        });

        ProductAddressSaveDTO saveDto = new ProductAddressSaveDTO();
        saveDto.setStokKod(searchPartialItem.get().getPackageCode());
        saveDto.setBarcode(searchPartialItem.get().getPackageBarcode());
        saveDto.setUrunAdres(processTree.get().getTargetAddress().getAdres());
        saveDto.setOrderNo("");
        saveDto.setDepoCode(String.valueOf(processTree.get().getDepoCode()));
        saveDto.setCompanyCode(processTree.get().getTargetAddress().getCompanyCode());
        saveDto.setBarkodTipi("RAF");
        saveDto.setStatus(true);
        saveDto.setMiktar(processTree.get().getAmount());


        partialDetails.forEach(partialItem -> {
            List<ProcessLeaf> filteredList = processLeaves.stream()
                .filter(q -> q.getBarcode().equals(partialItem.getBarcode())).collect(Collectors.toList());
            filteredList.forEach(q -> aurDepoUrunAdresStokService.decreaseProductAmount(q.getBarcode(),depoCode,q.getAmount(),q.getAddress().getUrunAdresId()));
        });

        aurDepoUrunAdresStokService.assignProductToAddress(saveDto);
        processTree.get().setProcessType(ProcessType.PG_COMPELETED);
        processTree.get().setStatus(false);
    }


}
