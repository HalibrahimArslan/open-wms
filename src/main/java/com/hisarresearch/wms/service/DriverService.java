package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.repository.AurDriverRepository;
import com.hisarresearch.wms.repository.AurOrderMasterRepository;
import com.hisarresearch.wms.service.criteria.AurDriverCriteria;
import com.hisarresearch.wms.service.dto.AurDriverDTO;
import com.hisarresearch.wms.service.mapper.AurDriverMapper;
import com.hisarresearch.wms.utility.AurQueryService;
import com.hisarresearch.wms.exception.business.BusinessException;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverService {

    private static final String ENTITY_NAME = "driver";

    private final Logger log = LoggerFactory.getLogger(DriverService.class);

    private final AurOrderMasterRepository aurOrderMasterRepository;

    private final AurDriverRepository aurDriverRepository;

    private final AurDriverMapper aurDriverMapper;

    private final AurQueryService aurQueryService;

    public DriverService(AurDriverRepository aurDriverRepository, AurOrderMasterRepository aurOrderMasterRepository, AurDriverMapper aurDriverMapper, AurQueryService aurQueryService) {
        this.aurDriverRepository = aurDriverRepository;
        this.aurOrderMasterRepository = aurOrderMasterRepository;
        this.aurDriverMapper = aurDriverMapper;
        this.aurQueryService = aurQueryService;
    }

    public List<AurDriverDTO> findAllDrivers() {
        List<AurDriver> aurDrivers = aurDriverRepository.findAll();
        return aurDriverMapper.toDto(aurDrivers);
    }

    public AurDriverDTO createDriver(AurDriverDTO aurDriverDTO) {
        Optional<AurDriver> aurDriverOptional = aurDriverRepository.findByPhoneNumber(aurDriverDTO.getPhoneNumber());
        if (aurDriverOptional.isPresent()) {
            throw new BusinessException("Bu Telefon Numarası ile Sistemde Kayıtlı Şoför bulunmaktadır.", ENTITY_NAME, "driverIsExists");
        }
        AurDriver aurDriverSaved = aurDriverRepository.save(aurDriverMapper.toEntity(aurDriverDTO));
        return aurDriverMapper.toDto(aurDriverSaved);
    }

    public AurDriverDTO updateDriver(AurDriverDTO aurDriverDTO) {
        Optional<AurDriver> aurDriverOptional = aurDriverRepository.findById(aurDriverDTO.getId());
        if (aurDriverOptional.isPresent()) {
            Optional<AurDriver> samePhoneDriver = aurDriverRepository.findByPhoneNumber(aurDriverDTO.getPhoneNumber());
            if(samePhoneDriver.isPresent() && !samePhoneDriver.get().getId().equals(aurDriverDTO.getId())){
                throw new BusinessException("Bu Telefon Numarası Başka Bir Kullanıcıya Ait", ENTITY_NAME, "driverPhoneIsExists");
            }
            AurDriver aurDriverSaved = aurDriverOptional.get();
            aurDriverSaved.setDriverName(aurDriverDTO.getDriverName());
            aurDriverSaved.setLicensePlate(aurDriverDTO.getLicensePlate());
            aurDriverSaved.setTrailerPlate(aurDriverDTO.getTrailerPlate());
            aurDriverSaved.setIdentityNumber(aurDriverDTO.getIdentityNumber());
            aurDriverSaved.setOpType(aurDriverDTO.getOpType());
            aurDriverSaved.setPhoneNumber(aurDriverDTO.getPhoneNumber());
            aurDriverSaved.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            aurDriverSaved.setLastModifiedDate(Instant.now());
            return aurDriverMapper.toDto(aurDriverSaved);
        } else {
            throw new BusinessException("Güncellenecek Şoför Bulunamadı.", ENTITY_NAME, "driverIsNotExists");
        }
    }

    public void saveDriver(AurDriverDTO aurDriverDTO) {
        try {
            Optional<AurDriver> aurDriverOptional = aurDriverRepository.findByPhoneNumber(aurDriverDTO.getPhoneNumber());
            AurDriver aurDriverSaved;
            if (aurDriverOptional.isPresent()) {
                aurDriverSaved = aurDriverOptional.get();
                aurDriverSaved.setDriverName(aurDriverDTO.getDriverName());
                aurDriverSaved.setLicensePlate(aurDriverDTO.getLicensePlate());
                aurDriverSaved.setTrailerPlate(aurDriverDTO.getTrailerPlate());
                aurDriverSaved.setIdentityNumber(aurDriverDTO.getIdentityNumber());
                aurDriverSaved.setOpType(aurDriverDTO.getOpType());
                aurDriverSaved.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                aurDriverSaved.setLastModifiedDate(Instant.now());
            } else {
                aurDriverSaved = aurDriverMapper.toEntity(aurDriverDTO);
                aurDriverRepository.save(aurDriverSaved);
            }
        } catch (Exception e) {
            log.error("Şoför Günceleme Servisinde Hata Alındı: {}", e.getMessage());
        }
    }

    public void deleteDriver(Long id){
        if(!aurDriverRepository.existsById(id)){
            throw new EntityNotFoundException("Sürücü bulunamadı");
        }
        aurDriverRepository.deleteById(id);
    }


}
