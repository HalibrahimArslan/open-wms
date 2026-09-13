package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.address.*;
import com.hisarresearch.wms.repository.address.*;
import com.hisarresearch.wms.repository.address.AddressDepartmentRepository;
import com.hisarresearch.wms.service.dto.address.components.*;
import com.hisarresearch.wms.service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressComponentService {
    private final Logger log = LoggerFactory.getLogger(AddressComponentService.class);

    @Autowired
    private AddressDepartmentRepository addressDepartmentRepository;
    @Autowired
    private AddressHallRepository addressHallRepository;
    @Autowired
    private AddressUnitRepository addressUnitRepository;
    @Autowired
    private AddressFlatRepository addressFlatRepository;
    @Autowired
    private AddressRoomRepository addressRoomRepository;
    @Autowired
    private AddressTypeRepository addressTypeRepository;
    @Autowired
    private AddressDepartmentMapper addressDepartmentMapper;
    @Autowired
    private AddressHallMapper addressHallMapper;
    @Autowired
    private AddressUnitMapper addressUnitMapper;
    @Autowired
    private AddressFlatMapper addressFlatMapper;
    @Autowired
    private AddressRoomMapper addressRoomMapper;
    @Autowired
    private AddressTypeMapper addressTypeMapper;


    public List<AddressDepartment> getAddressDepartment(String companyCode,String depoCode) {
        log.debug("Request to get AddressDepartment");
        return addressDepartmentRepository.findByCompanyCodeAndDepoCode(companyCode,depoCode);
    }

    public List<AddressHall> getAddressHalls(String companyCode,String depoCode ) {
        log.debug("Request to get AddressHalls");
        return addressHallRepository.findByCompanyCodeAndDepoCode(companyCode,depoCode);
    }

    public List<AddressUnit> getAddressUnits(String companyCode,String depoCode) {
        log.debug("Request to get AddressUnits");
        return addressUnitRepository.findByCompanyCodeAndDepoCode(companyCode,depoCode);
    }

    public List<AddressFlat> getAddressFlat(String companyCode,String depoCode) {
        log.debug("Request to get AddressFlat");
        return addressFlatRepository.findByCompanyCodeAndDepoCode(companyCode,depoCode);
    }

    public List<AddressRoom> getAddressRooms(String companyCode,String depoCode) {
        log.debug("Request to get AddressRooms");
        return addressRoomRepository.findByCompanyCodeAndDepoCode(companyCode,depoCode);
    }

    public List<AddressType> getAddressTypes(String companyCode,String depoCode) {
        log.debug("Request to get AddressType");
        return addressTypeRepository.findByCompanyCodeAndDepoCode(companyCode,depoCode);
    }

    public AddressDepartmentDTO saveAddressDepartment(AddressDepartmentDTO addressDepartmentDTO) {
        log.debug("Request to save AddressDepartment");
        AddressDepartment newEntity = addressDepartmentMapper.toEntity(addressDepartmentDTO);
        AddressDepartment createdOne = addressDepartmentRepository.save(newEntity);
        return addressDepartmentMapper.toDto(createdOne);
    }

    public List<AddressDepartmentDTO> saveBulkAddressDepartment(List<AddressDepartmentDTO> addressDepartmentDTOS) {
        log.debug("Request to save addressDepartmentDTOS");
        List<AddressDepartment> newEntity = addressDepartmentMapper.toEntity(addressDepartmentDTOS);
        List<AddressDepartment> createdList= addressDepartmentRepository.saveAll(newEntity);
        return addressDepartmentMapper.toDto(createdList);
    }

    public List<AddressHallDTO> saveBulkAddressHall(List<AddressHallDTO> addressHallDTOS) {
        log.debug("Request to save addressHallDTOS");
        List<AddressHall> newEntity = addressHallMapper.toEntity(addressHallDTOS);
        List<AddressHall> createdList= addressHallRepository.saveAll(newEntity);
        return addressHallMapper.toDto(createdList);
    }

    public List<AddressUnitDTO> saveBulkAddressUnit(List<AddressUnitDTO> addressUnitDTOS) {
        log.debug("Request to save addressUnitDTOS");
        List<AddressUnit> newEntity = addressUnitMapper.toEntity(addressUnitDTOS);
        List<AddressUnit> createdList= addressUnitRepository.saveAll(newEntity);
        return addressUnitMapper.toDto(createdList);
    }

    public List<AddressRoomDTO> saveBulkAddressRoom(List<AddressRoomDTO> addressRoomDTOS) {
        log.debug("Request to save addressRoomDTOS");
        List<AddressRoom> newEntity = addressRoomMapper.toEntity(addressRoomDTOS);
        List<AddressRoom> createdList= addressRoomRepository.saveAll(newEntity);
        return addressRoomMapper.toDto(createdList);
    }

    public List<AddressTypeDTO> saveBulkAddressType(List<AddressTypeDTO> addressTypeDTOS) {
        log.debug("Request to save addressTypeDTOS");
        List<AddressType> newEntity = addressTypeMapper.toEntity(addressTypeDTOS);
        List<AddressType> createdList= addressTypeRepository.saveAll(newEntity);
        return addressTypeMapper.toDto(createdList);
    }

    public AddressHallDTO saveAddressHall(AddressHallDTO addressHallDTO) {
        log.debug("Request to save AddressHall");
        AddressHall newEntity = addressHallMapper.toEntity(addressHallDTO);
        AddressHall createdOne = addressHallRepository.save(newEntity);
        return addressHallMapper.toDto(createdOne);
    }

    public AddressUnitDTO saveAddressUnit(AddressUnitDTO addressUnitDTO) {
        log.debug("Request to save AddressUnit");
        AddressUnit newEntity = addressUnitMapper.toEntity(addressUnitDTO);
        AddressUnit createdOne = addressUnitRepository.save(newEntity);
        return addressUnitMapper.toDto(createdOne);
    }

    public AddressFlatDTO saveAddressFlat(AddressFlatDTO addressFlatDTO) {
        log.debug("Request to save AddressFlat");
        AddressFlat newEntity = addressFlatMapper.toEntity(addressFlatDTO);
        AddressFlat createdOne = addressFlatRepository.save(newEntity);
        return addressFlatMapper.toDto(createdOne);
    }

    public List<AddressFlatDTO> saveBulkAddressFlat(List<AddressFlatDTO> addressFlatDTOList) {
        log.debug("Request to save AddressFlatList");
        List<AddressFlat> newEntity = addressFlatMapper.toEntity(addressFlatDTOList);
        List<AddressFlat> createdList= addressFlatRepository.saveAll(newEntity);
        return addressFlatMapper.toDto(createdList);
    }

    public AddressRoomDTO saveAddressRoom(AddressRoomDTO addressRoomDTO) {
        log.debug("Request to save AddressRoom");
        AddressRoom newEntity = addressRoomMapper.toEntity(addressRoomDTO);
        AddressRoom createdOne = addressRoomRepository.save(newEntity);
        return addressRoomMapper.toDto(createdOne);
    }

    public AddressTypeDTO saveAddressType(AddressTypeDTO addressTypeDTO) {
        log.debug("Request to save AddressType");
        AddressType newEntity = addressTypeMapper.toEntity(addressTypeDTO);
        AddressType createdOne = addressTypeRepository.save(newEntity);
        return addressTypeMapper.toDto(createdOne);
    }

    public Optional<AddressDepartmentDTO> updateAddressDepartment(AddressDepartmentDTO addressDepartmentDTO) {
        log.debug("Request to update AddressDepartment");
        return Optional.of(addressDepartmentRepository.findById(addressDepartmentDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(addressDepartment -> {
                addressDepartment.setCompanyCode(addressDepartmentDTO.getCompanyCode());
                addressDepartment.setDepoCode(addressDepartmentDTO.getDepoCode());
                addressDepartment.setStatus(addressDepartmentDTO.getStatus());
                addressDepartment.setDescription(addressDepartmentDTO.getDescription());
                addressDepartment.setCode(addressDepartmentDTO.getCode());
                return addressDepartmentMapper.toDto(addressDepartment);
            });
    }

    public Optional<AddressHallDTO> updateAddressHall(AddressHallDTO addressHallDTO) {
        log.debug("Request to update AddressHall");
        return Optional.of(addressHallRepository.findById(addressHallDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(addressHall -> {
                addressHall.setCompanyCode(addressHallDTO.getCompanyCode());
                addressHall.setDepoCode(addressHallDTO.getDepoCode());
                addressHall.setStatus(addressHallDTO.getStatus());
                addressHall.setDescription(addressHallDTO.getDescription());
                addressHall.setCode(addressHallDTO.getCode());
                return addressHallMapper.toDto(addressHall);
            });
    }

    public Optional<AddressUnitDTO> updateAddressUnit(AddressUnitDTO addressUnitDTO) {
        log.debug("Request to update AddressUnit");
        return Optional.of(addressUnitRepository.findById(addressUnitDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(addressUnit -> {
                addressUnit.setCompanyCode(addressUnitDTO.getCompanyCode());
                addressUnit.setDepoCode(addressUnitDTO.getDepoCode());
                addressUnit.setStatus(addressUnitDTO.getStatus());
                addressUnit.setDescription(addressUnitDTO.getDescription());
                addressUnit.setCode(addressUnitDTO.getCode());
                return addressUnitMapper.toDto(addressUnit);
            });
    }

    public Optional<AddressFlatDTO> updateAddressFlat(AddressFlatDTO addressFlatDTO) {
        log.debug("Request to update AddressFlat");
        return Optional.of(addressFlatRepository.findById(addressFlatDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(addressFlat -> {
                addressFlat.setCompanyCode(addressFlatDTO.getCompanyCode());
                addressFlat.setDepoCode(addressFlatDTO.getDepoCode());
                addressFlat.setStatus(addressFlatDTO.getStatus());
                addressFlat.setDescription(addressFlatDTO.getDescription());
                addressFlat.setCode(addressFlatDTO.getCode());
                return addressFlatMapper.toDto(addressFlat);
            });
    }

    public Optional<AddressRoomDTO> updateAddressRoom(AddressRoomDTO addressRoomDTO) {
        log.debug("Request to update AddressRoom");
        return Optional.of(addressRoomRepository.findById(addressRoomDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(addressRoom -> {
                addressRoom.setCompanyCode(addressRoomDTO.getCompanyCode());
                addressRoom.setDepoCode(addressRoomDTO.getDepoCode());
                addressRoom.setStatus(addressRoomDTO.getStatus());
                addressRoom.setDescription(addressRoomDTO.getDescription());
                addressRoom.setCode(addressRoomDTO.getCode());
                return addressRoomMapper.toDto(addressRoom);
            });
    }

    public Optional<AddressTypeDTO> updateAddressType(AddressTypeDTO addressTypeDTO) {
        log.debug("Request to update AddressType");
        return Optional.of(addressTypeRepository.findById(addressTypeDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(addressType -> {
                addressType.setCompanyCode(addressTypeDTO.getCompanyCode());
                addressType.setDepoCode(addressTypeDTO.getDepoCode());
                addressType.setStatus(addressTypeDTO.getStatus());
                addressType.setDescription(addressTypeDTO.getDescription());
                addressType.setCode(addressTypeDTO.getCode());
                return addressTypeMapper.toDto(addressType);
            });
    }

    public void deleteAddressDepartment(Long id) {
        log.debug("Request to delete AddressDepartment {}", id);
        addressDepartmentRepository.deleteById(id);
    }

    public void deleteAddressHall(Long id) {
        log.debug("Request to delete AddressHall {}", id);
        addressHallRepository.deleteById(id);
    }

    public void deleteAddressUnit(Long id) {
        log.debug("Request to delete AddressUnit {}", id);
        addressUnitRepository.deleteById(id);
    }

    public void deleteAddressFlat(Long id) {
        log.debug("Request to delete AddressFlat {}", id);
        addressFlatRepository.deleteById(id);
    }

    public void deleteAddressRoom(Long id) {
        log.debug("Request to delete AddressRoom {}", id);
        addressRoomRepository.deleteById(id);
    }

    public void deleteAddressType(Long id) {
        log.debug("Request to delete AddressType {}", id);
        addressTypeRepository.deleteById(id);
    }

    public List<AddressDepartment> getAddressDepartmentsByIdRange(Long startId, Long endId) {
        return addressDepartmentRepository.findByIdBetween(startId, endId);
    }

    public List<AddressHall> getAddressHallsByIdRange(Long startId, Long endId) {
        return addressHallRepository.findByIdBetween(startId, endId);
    }

    public List<AddressUnit> getAddressUnitsByIdRange(Long startId, Long endId) {
        return addressUnitRepository.findByIdBetween(startId, endId);
    }

    public List<AddressFlat> getAddressFlatsByIdRange(Long startId, Long endId) {
        return addressFlatRepository.findByIdBetween(startId, endId);
    }

    public List<AddressRoom> getAddressRoomsByIdRange(Long startId, Long endId) {
        return addressRoomRepository.findByIdBetween(startId, endId);
    }


}
