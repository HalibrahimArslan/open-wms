package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.CountingUserAddressRel;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.repository.CountingUserAddressRelRepository;
import com.hisarresearch.wms.service.dto.address.AddressCountingResponseDto;
import com.hisarresearch.wms.service.dto.counting.CountingUserAddressRelDTO;
import com.hisarresearch.wms.service.dto.counting.CountingUserAddressSearchDTO;
import com.hisarresearch.wms.service.mapper.CountingUserAddressRelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CountingUserAddressRelService {
    private final Logger log = LoggerFactory.getLogger(CountingUserAddressRelService.class);

    private static final String ENTITY_NAME = "countingUserAddressRel";

    private final CountingUserAddressRelRepository countingUserAddressRelRepository;

    private final CountingUserAddressRelMapper countingUserAddressRelMapper;

    private final AddressService addressService;

    private final TranslationService translationService;

    public CountingUserAddressRelService(
        CountingUserAddressRelRepository countingUserAddressRelRepository, CountingUserAddressRelMapper countingUserAddressRelMapper, AddressService addressService, TranslationService translationService) {
        this.countingUserAddressRelRepository = countingUserAddressRelRepository;
        this.countingUserAddressRelMapper = countingUserAddressRelMapper ;
        this.addressService = addressService;
        this.translationService = translationService;
    }


    public List<CountingUserAddressRelDTO> getCountingUserAddressRelations(CountingUserAddressSearchDTO countingUserAddressSearchDTO) {
        log.debug("Request to get Counting User Address Relations by : {}", countingUserAddressSearchDTO);
        List<Long> addressIds = countingUserAddressSearchDTO.getAddresses().stream().map(AurDepoUrunAdres::getUrunAdresId).collect(Collectors.toList());
        List<CountingUserAddressRel> countingUserAddressRelations = countingUserAddressRelRepository.findByCountingDefinition_IdAndAddress_UrunAdresIdIn(countingUserAddressSearchDTO.getCountingDefinitionId(),addressIds);
        List<AddressCountingResponseDto> countingAddresses =
            addressService.getCountedAddressList(
                countingUserAddressSearchDTO.getCountingDefinitionId(),
                addressIds
            );
        Set<Long> countedAddressIds = countingAddresses.stream()
            .map(AddressCountingResponseDto::getId)
            .collect(Collectors.toSet());

        return countingUserAddressRelations.stream()
            .map(rel -> countingUserAddressRelMapper.toDto(rel, countedAddressIds))
            .collect(Collectors.toList());
    }



    public List<CountingUserAddressRel> saveCountingUserAddresses(List<CountingUserAddressRelDTO> countingUserAddressRelDTOS) {
        log.debug("Request to save Counting User Address Relations : {}", countingUserAddressRelDTOS);
        validateSingleCountingDefinition(countingUserAddressRelDTOS);

        Long countingDefinitionId = countingUserAddressRelDTOS.get(0).getCountingDefinitionId();

        List<Long> addressIds = countingUserAddressRelDTOS.stream()
            .map(CountingUserAddressRelDTO::getAddressId)
            .collect(Collectors.toList());

        Map<Long, String> countedAddressMap = getCountedAddressMap(countingDefinitionId, addressIds);
        validateNotAlreadyCounted(addressIds, countedAddressMap,"countingUserAddressRel.alreadyCounted");
        return countingUserAddressRelRepository.saveAll(countingUserAddressRelMapper.toEntity(countingUserAddressRelDTOS));
    }

    public List<CountingUserAddressRelDTO> findAllByIdIn(List<Long> ids) {
        log.debug("Request to get Counting User Address Relations : {}", ids);
        return countingUserAddressRelMapper.toDto(countingUserAddressRelRepository.findAllByIdIn(ids));
    }


    public void deleteCountingUserAddressRel(List<CountingUserAddressRelDTO> countingUserAddressRelDTOS) {
        log.debug("Request to delete Counting User Address Relations : {}", countingUserAddressRelDTOS);

        validateSingleCountingDefinition(countingUserAddressRelDTOS);

        Long countingDefinitionId = countingUserAddressRelDTOS.get(0).getCountingDefinitionId();

        List<Long> addressIds = countingUserAddressRelDTOS.stream()
            .map(CountingUserAddressRelDTO::getAddressId)
            .collect(Collectors.toList());

        Map<Long, String> countedAddressMap = getCountedAddressMap(countingDefinitionId, addressIds);
        validateNotAlreadyCounted(addressIds, countedAddressMap,"countingUserAddressRel.cannotDeleteCounted");

        List<CountingUserAddressRel> countingUserAddressRels = countingUserAddressRelMapper.toEntity(countingUserAddressRelDTOS);
        countingUserAddressRelRepository.deleteAll(countingUserAddressRels);
    }

    public Optional<CountingUserAddressRel> findByCountingDefinitionIdAndAddressId(Long countingDefinitionId, Long addressId){
        log.debug("Request to get one user counting address rel by countingDefinitionId {} and addresId {}", countingDefinitionId, addressId);
        return countingUserAddressRelRepository.findByCountingDefinition_IdAndAddress_UrunAdresId(countingDefinitionId,addressId);

    }

    private Map<Long, String> getCountedAddressMap(Long countingDefinitionId, List<Long> addressIds) {
        List<AddressCountingResponseDto> countingAddresses =
            addressService.getCountedAddressList(countingDefinitionId, addressIds);
        return countingAddresses.stream()
            .collect(Collectors.toMap(
                AddressCountingResponseDto::getId,
                AddressCountingResponseDto::getAdres
            ));
    }

    private void validateNotAlreadyCounted(
        List<Long> addressIds,
        Map<Long, String> countedAddressMap,
        String messageKey
    ) {
        List<String> conflictAddresses = addressIds.stream()
            .filter(countedAddressMap::containsKey)
            .map(countedAddressMap::get)
            .collect(Collectors.toList());

        if (!conflictAddresses.isEmpty()) {
            throw new BusinessException(
                translationService.getErrorMessage(messageKey, String.join(", ", conflictAddresses)),
                ENTITY_NAME
            );
        }
    }

    private void validateSingleCountingDefinition(List<CountingUserAddressRelDTO> dtos) {
        Long firstId = dtos.get(0).getCountingDefinitionId();
        boolean hasDifferent = dtos.stream()
            .anyMatch(dto -> !Objects.equals(firstId, dto.getCountingDefinitionId()));

        if (hasDifferent) {
            throw new BusinessException(translationService.getErrorMessage("countingUserAddressRel.singleCountingDefinition"), ENTITY_NAME);
        }
    }

}
