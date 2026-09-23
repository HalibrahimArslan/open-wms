package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderAdres;
import com.hisarresearch.wms.domain.address.*;
import com.hisarresearch.wms.domain.enumeration.AddressFieldType;
import com.hisarresearch.wms.repository.AurOrderAdresRepository;
import com.hisarresearch.wms.repository.address.AurDepoAdresRepository;
import com.hisarresearch.wms.service.dto.address.*;
import com.hisarresearch.wms.service.dto.address.components.*;
import com.hisarresearch.wms.service.mapper.AddressMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressService {
    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private static final String ENTITY_NAME = "address";

    @Autowired
    private AurDepoAdresRepository aurDepoAdresRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductAddressService productAddressService;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private TranslationService translationService;

    @Autowired
    AddressComponentService addressComponentService;

    @Autowired
    private AurOrderAdresRepository aurOrderAdresRepository;


    @Transactional
    public List<AurDepoUrunAdres> getAddressList() {
        return aurDepoAdresRepository.findAll();
    }

    @Transactional
    public Page<AurDepoUrunAdres> getAddressList(Pageable pageable) {
        return aurDepoAdresRepository.findAll(pageable).map(AurDepoUrunAdres::new);
    }

    public void flowDepartments() {
        List<AddressParams> addressParams = aurDepoAdresRepository.findGroupByDepoNoAndCompanyCodeAndBolum();
        List<AddressDepartmentDTO> addressDepartmentDTOS = addressParams.stream().map(address -> {
            AddressDepartmentDTO addressDepartmentDTO = new AddressDepartmentDTO();
            addressDepartmentDTO.setCode(address.getParam());
            addressDepartmentDTO.setStatus(true);
            addressDepartmentDTO.setCompanyCode(address.getCompanyCode());
            addressDepartmentDTO.setDepoCode(address.getDepoNo());
            return addressDepartmentDTO;
        }).collect(Collectors.toList());
        addressComponentService.saveBulkAddressDepartment(addressDepartmentDTOS);
    }

    public void flowHalls() {
        List<AddressParams> addressParams = aurDepoAdresRepository.findGroupByDepoNoAndCompanyCodeAndReyon();
        List<AddressHallDTO> addressHallDTOS = addressParams.stream().map(address -> {
            AddressHallDTO addressHallDTO = new AddressHallDTO();
            addressHallDTO.setCode(address.getParam());
            addressHallDTO.setStatus(true);
            addressHallDTO.setCompanyCode(address.getCompanyCode());
            addressHallDTO.setDepoCode(address.getDepoNo());
            return addressHallDTO;
        }).collect(Collectors.toList());
        addressComponentService.saveBulkAddressHall(addressHallDTOS);
    }

    public void flowUnits() {
        List<AddressParams> addressParams = aurDepoAdresRepository.findGroupByDepoNoAndCompanyCodeAndUnite();
        List<AddressUnitDTO> addressUnitDTOS = addressParams.stream().map(address -> {
            AddressUnitDTO addressUnitDTO = new AddressUnitDTO();
            addressUnitDTO.setCode(address.getParam());
            addressUnitDTO.setStatus(true);
            addressUnitDTO.setCompanyCode(address.getCompanyCode());
            addressUnitDTO.setDepoCode(address.getDepoNo());
            return addressUnitDTO;
        }).collect(Collectors.toList());
        addressComponentService.saveBulkAddressUnit(addressUnitDTOS);
    }

    public void flowRooms() {
        List<AddressParams> addressParams = aurDepoAdresRepository.findGroupByDepoNoAndCompanyCodeAndOda();
        List<AddressRoomDTO> addressRoomDTOS = addressParams.stream().map(address -> {
            AddressRoomDTO addressRoomDTO = new AddressRoomDTO();
            addressRoomDTO.setCode(address.getParam());
            addressRoomDTO.setStatus(true);
            addressRoomDTO.setCompanyCode(address.getCompanyCode());
            addressRoomDTO.setDepoCode(address.getDepoNo());
            return addressRoomDTO;
        }).collect(Collectors.toList());
        addressComponentService.saveBulkAddressRoom(addressRoomDTOS);
    }

    public void flowAddressTypes() {
        List<AddressParams> addressParams = aurDepoAdresRepository.findGroupByDepoNoAndCompanyCodeAndAdresTipi();
        List<AddressTypeDTO> addressTypeDTOS = addressParams.stream().map(address -> {
            AddressTypeDTO addressTypeDTO = new AddressTypeDTO();
            addressTypeDTO.setCode(address.getParam());
            addressTypeDTO.setStatus(true);
            addressTypeDTO.setCompanyCode(address.getCompanyCode());
            addressTypeDTO.setDepoCode(address.getDepoNo());
            return addressTypeDTO;
        }).collect(Collectors.toList());
        addressComponentService.saveBulkAddressType(addressTypeDTOS);
    }

    public void flowFlats() {
        List<AddressParams> addressParams = aurDepoAdresRepository.findGroupByDepoNoAndCompanyCodeAndKat();
        List<AddressFlatDTO> addressFlatDTOS = addressParams.stream().map(address -> {
            AddressFlatDTO addressFlatDTO = new AddressFlatDTO();
            addressFlatDTO.setCode(address.getParam());
            addressFlatDTO.setStatus(true);
            addressFlatDTO.setCompanyCode(address.getCompanyCode());
            addressFlatDTO.setDepoCode(address.getDepoNo());
            return addressFlatDTO;
        }).collect(Collectors.toList());
        addressComponentService.saveBulkAddressFlat(addressFlatDTOS);
    }


    @Transactional
    public AurDepoUrunAdres findByAddress(String address) {
        return aurDepoAdresRepository.findByAdres(address);
    }

    @Transactional
    public Optional<AurDepoUrunAdres> findByAddressAndDepoCode(String address, String depoCode) {
        Integer companyCode = userService.getUserCompanyCode();
        return aurDepoAdresRepository.findByAdresAndDepoNoAndCompanyCode(address, depoCode, String.valueOf(companyCode));
    }

    @Transactional
    public Optional<AurDepoUrunAdres> findByAddressAndDepoCodeAndCompanyCodeAndStatus(String address, String depoCode, boolean status) {
        Integer companyCode = userService.getUserCompanyCode();
        return aurDepoAdresRepository.findByAdresAndDepoNoAndCompanyCodeAndStatus(address, depoCode, String.valueOf(companyCode), status);
    }

    @Transactional
    public List<AurDepoUrunAdres> findByDepoNoAndCompanyCodeAndGeciciAdres(String depoCode, String companyCode, Boolean geciciAddress) {
        return aurDepoAdresRepository.findByDepoNoAndCompanyCodeAndGeciciAdresAndStatusTrue(depoCode, companyCode, geciciAddress);
    }

    @Transactional
    public List<AurDepoUrunAdres> findByDepoNoAndCompanyCodeAndKontrolAdres(String depoCode, String companyCode, Boolean kontrolAddress) {
        return aurDepoAdresRepository.findByDepoNoAndCompanyCodeAndStatusAndKontrolAdres(depoCode, companyCode, true,kontrolAddress);
    }

    @Transactional
    public Optional<AurDepoUrunAdres> findById(Long addressId) {
        return aurDepoAdresRepository.findById(addressId);
    }


    public Long getUrunAddressId(String address) {
        AurDepoUrunAdres aurDepoUrunAdres = aurDepoAdresRepository.findByAdres(address);
        if (aurDepoUrunAdres == null) {
            throw new BadRequestAlertException("Geçersiz Adres Girdiniz", ENTITY_NAME, "invalidAddress");
        } else {
            return aurDepoUrunAdres.getUrunAdresId();
        }
    }

    public Optional<AurDepoUrunAdres> partialUpdate(AurDepoUrunAdres aurDepoUrunAdres) {
        log.debug("Request to partially update address : {}", aurDepoUrunAdres);

        return aurDepoAdresRepository
            .findById(aurDepoUrunAdres.getUrunAdresId())
            .map(
                existingAurDepoUrunAdres -> {
                    if (aurDepoUrunAdres.getToplamaGozu() != null) {
                        existingAurDepoUrunAdres.setToplamaGozu(aurDepoUrunAdres.getToplamaGozu());
                    }
                    if (aurDepoUrunAdres.getGeciciAdres() != null) {
                        existingAurDepoUrunAdres.setGeciciAdres(aurDepoUrunAdres.getGeciciAdres());
                    }
                    if (aurDepoUrunAdres.getCountable() != null) {
                        existingAurDepoUrunAdres.setCountable(aurDepoUrunAdres.getCountable());
                    }
                    if (aurDepoUrunAdres.getStatus() != null) {
                        existingAurDepoUrunAdres.setStatus(aurDepoUrunAdres.getStatus());
                    }
                    if(aurDepoUrunAdres.getKontrolAdres() != null){
                        existingAurDepoUrunAdres.setKontrolAdres(aurDepoUrunAdres.getKontrolAdres());
                    }

                    return existingAurDepoUrunAdres;
                }
            )
            .map(aurDepoAdresRepository::save);
    }

    public void partialUpdateWithError(AurDepoUrunAdres address, AddressUpdateResponseDTO responseDTO) {
        try {
            AurDepoUrunAdres updatedAddress = partialUpdate(address).orElse(null);
            if (updatedAddress != null) {
                responseDTO.addSuccess(updatedAddress);
            } else {
                responseDTO.addError("Adres bulunamadı", address);
            }
        } catch (IllegalArgumentException e) {
            responseDTO.addError("Geçersiz giriş verisi: " + e.getMessage(), address);
        } catch (DataAccessException e) {
            responseDTO.addError("Veritabanı hatası: " + e.getMessage(), address);
        } catch (Exception e) {
            log.error("Adres güncellenirken beklenmeyen hata oluştu", e);
            responseDTO.addError("Bilinmeyen hata oluştu", address);
        }
    }

    public AddressUpdateResponseDTO partialBulkUpdate(List<AurDepoUrunAdres> addressList) {
        log.debug("Request to partially update addresses : {}", addressList);
        AddressUpdateResponseDTO responseDTO = new AddressUpdateResponseDTO();
        for (AurDepoUrunAdres address : addressList) {
            if (address.getUrunAdresId() != null && !aurDepoAdresRepository.existsById(address.getUrunAdresId())) {
                responseDTO.addError("Id bulunmadı", address);
                continue;
            }
            if (address.getGeciciAdres() && address.getToplamaGozu()) {
                responseDTO.addError("Toplama Gozu ile gecici adres aynı anda aktif olamaz", address);
                continue;
            }
            partialUpdateWithError(address, responseDTO);

        }
        return responseDTO;
    }

    public List<AddressCountingResponseDto> getCountedAddressList(Long countingDefinitionId,List<Long> addressIds) {
        return aurDepoAdresRepository.getCountedAddresses(countingDefinitionId,addressIds);
    }

    public List<AddressCountingResponseDto> getNotCountedAddressList(Long countingDefinitionId) {
        return aurDepoAdresRepository.getNotCountedAddresses(countingDefinitionId);
    }

    public long getCountOfCountableAddressList(Long sayimTanimId, String depoCode) {
        return aurDepoAdresRepository.getCountOfCountableAddress(sayimTanimId, depoCode).size();
    }

    public AurDepoUrunAdres isExistAddress(Long id) {
        return aurDepoAdresRepository
            .findById(id)
            .orElseThrow(InvalidAddressException::new);
    }

    @Transactional
    public List<AurDepoUrunAdres> getNoneCountableAddresses(String depoNo) {
        log.debug("Get the none countable address list");
        return aurDepoAdresRepository.findByCountableAndDepoNoAndStatus(false, depoNo, true);
    }

    public EmptyAddressDTO getEmptyAddressList(String depoNo) {
        String companyCode = String.valueOf(userService.getUserCompanyInfo().getCompanyCode());
        List<AurDepoUrunAdres> addressList = aurDepoAdresRepository.findByDepoNoAndStatusAndCompanyCode(depoNo, true,companyCode);
        List<AurDepoUrunAdres> activeProductAddressList = productAddressService.getActiveAddressList(String.valueOf(depoNo), companyCode);
        List<AurDepoUrunAdres> emptyAddresses =  addressList.stream().filter(address -> !activeProductAddressList.contains(address)).collect(Collectors.toList());
        EmptyAddressDTO emptyAddressDTO = new EmptyAddressDTO();
        int filledAddressCount = addressList.size() - emptyAddresses.size();
        emptyAddressDTO.setTotalAddressCount(addressList.size());
        emptyAddressDTO.setEmptyAddressCount(emptyAddresses.size());
        emptyAddressDTO.setEmptyAddresses(emptyAddresses);
        emptyAddressDTO.setFilledAddressCount(filledAddressCount);
        emptyAddressDTO.setFilledRatio(Math.round(((float) filledAddressCount / emptyAddressDTO.getTotalAddressCount()) * 100 ));
        return emptyAddressDTO;
    }

    public EmptyAddressDTO getEmptyAddressListByRayon(String depoNo,String rayon) {
        String companyCode = String.valueOf(userService.getUserCompanyInfo().getCompanyCode());
        List<AurDepoUrunAdres> addressList = aurDepoAdresRepository.findByDepoNoAndStatusAndCompanyCodeAndReyon(depoNo, true,companyCode,rayon);
        List<AurDepoUrunAdres> activeProductAddressList = productAddressService.getActiveAddressListByRayon(String.valueOf(depoNo), companyCode,rayon);
        List<AurDepoUrunAdres> emptyAddresses =  addressList.stream().filter(address -> !activeProductAddressList.contains(address)).collect(Collectors.toList());
        EmptyAddressDTO emptyAddressDTO = new EmptyAddressDTO();
        int filledAddressCount = addressList.size() - emptyAddresses.size();
        emptyAddressDTO.setTotalAddressCount(addressList.size());
        emptyAddressDTO.setEmptyAddressCount(emptyAddresses.size());
        emptyAddressDTO.setEmptyAddresses(emptyAddresses);
        emptyAddressDTO.setFilledAddressCount(addressList.size() - emptyAddresses.size());
        emptyAddressDTO.setFilledRatio(Math.round(((float) filledAddressCount / emptyAddressDTO.getTotalAddressCount()) * 100 ));
        return emptyAddressDTO;
    }

    public AurDepoUrunAdres createAddress(AddressDTO addressDTO) {
        log.debug("Save address : {}", addressDTO);
        AurDepoUrunAdres createOne = addressMapper.toEntity(addressDTO);
        return aurDepoAdresRepository.save(createOne);
    }

    public List<AddressDTO> createAddressBulk(AddressCreateBulkDTO addressCreateBulkDTO) {
        String companyCode = addressCreateBulkDTO.getCompanyCode();
        String depoCode = String.valueOf(addressCreateBulkDTO.getWarehouseCode());
        List<AddressFieldType> selectedFields = addressCreateBulkDTO.getSelectedFields();

        List<AddressDepartment> departments = resolveComponents(addressCreateBulkDTO.getDepartmentIds(),
            ids -> addressComponentService.getAddressDepartmentsByIds(ids, companyCode, depoCode), "invalidAddressDepartment");
        List<AddressHall> halls = resolveComponents(addressCreateBulkDTO.getHallIds(),
            ids -> addressComponentService.getAddressHallsByIds(ids, companyCode, depoCode), "invalidAddressHall");
        List<List<? extends AddressComponent>> selectedLevels = new ArrayList<>();
        if (selectedFields.contains(AddressFieldType.UNIT)) {
            selectedLevels.add(resolveComponents(addressCreateBulkDTO.getUnitIds(),
                ids -> addressComponentService.getAddressUnitsByIds(ids, companyCode, depoCode), "invalidAddressUnit"));
        }
        if (selectedFields.contains(AddressFieldType.FLAT)) {
            selectedLevels.add(resolveComponents(addressCreateBulkDTO.getFlatIds(),
                ids -> addressComponentService.getAddressFlatsByIds(ids, companyCode, depoCode), "invalidAddressFlat"));
        }
        if (selectedFields.contains(AddressFieldType.ROOM)) {
            selectedLevels.add(resolveComponents(addressCreateBulkDTO.getRoomIds(),
                ids -> addressComponentService.getAddressRoomsByIds(ids, companyCode, depoCode), "invalidAddressRoom"));
        }

        List<AddressDTO> addressDTOList = new ArrayList<>();

        for (AddressDepartment addressDepartment : departments) {
            for (AddressHall addressHall : halls) {
                collectAddresses(addressCreateBulkDTO, addressDepartment, addressHall, selectedLevels, new ArrayList<>(), addressDTOList);
            }
        }

        Set<String> existingAddresses = new HashSet<>(aurDepoAdresRepository.findAdresByDepoNoAndCompanyCode(depoCode, companyCode));
        addressDTOList.removeIf(addressDTO -> existingAddresses.contains(addressDTO.getAddress()));

        if (addressDTOList.isEmpty()) {
            throw new BadRequestAlertException(translationService.getErrorMessage("createBulkAddress.allAddressesExist"), ENTITY_NAME, "addressesAlreadyExist");
        }

        List<AurDepoUrunAdres> createdList = aurDepoAdresRepository.saveAll(addressMapper.toEntity(addressDTOList));

        return addressMapper.toDto(createdList);
    }

    private <T extends AddressComponent> List<T> resolveComponents(List<Long> ids, Function<Collection<Long>, List<T>> finder, String errorKey) {
        if (ids == null || ids.isEmpty()) {
            throw new BadRequestAlertException(translationService.getErrorMessage("createBulkAddress.emptySelection"), ENTITY_NAME, errorKey);
        }

        Set<Long> uniqueIds = new LinkedHashSet<>(ids);
        Map<Long, T> found = finder.apply(uniqueIds).stream()
            .collect(Collectors.toMap(AddressComponent::getId, Function.identity()));

        if (found.size() != uniqueIds.size()) {
            throw new BadRequestAlertException(translationService.getErrorMessage("createBulkAddress.componentNotFound"), ENTITY_NAME, errorKey);
        }

        return uniqueIds.stream().map(found::get).toList();
    }

    private void collectAddresses(AddressCreateBulkDTO dto, AddressDepartment department, AddressHall hall,
                                  List<List<? extends AddressComponent>> levels, List<AddressComponent> current, List<AddressDTO> dtoList) {
        if (current.size() == levels.size()) {
            addIfNotExists(generateAddress(dto, department, hall, current.toArray(new AddressComponent[0])), dtoList);
            return;
        }
        for (AddressComponent component : levels.get(current.size())) {
            current.add(component);
            collectAddresses(dto, department, hall, levels, current, dtoList);
            current.remove(current.size() - 1);
        }
    }

    private void addIfNotExists(AddressDTO addressDTO, List<AddressDTO> addressDTOList) {
        if (addressDTOList.stream().noneMatch(address -> address.getAddress().equals(addressDTO.getAddress()))) {
            addressDTOList.add(addressDTO);
        }
    }


    public AddressDTO generateAddress(AddressCreateBulkDTO dto, AddressDepartment department, AddressHall hall, AddressComponent... components) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStatus(true);
        addressDTO.setWarehouseCode(dto.getWarehouseCode());
        addressDTO.setCompanyCode(dto.getCompanyCode());
        addressDTO.setAddressType(dto.getAddressType().getCode());
        addressDTO.setToplamaGozu(dto.getToplamaGozu());
        addressDTO.setGeciciAdres(dto.getGeciciAdres());
        addressDTO.setKontrolAdres(dto.getKontrolAdres());
        addressDTO.setCountable(dto.getCountable());
        addressDTO.setBolum(department.getCode());
        addressDTO.setReyon(hall.getCode());


        StringBuilder address = new StringBuilder();

        Map<AddressFieldType, AddressComponent> componentMap = new HashMap<>();
        int index = 0;

        if (dto.getSelectedFields().contains(AddressFieldType.UNIT) && components.length > index) {
            addressDTO.setUnite(components[index].getCode());
            componentMap.put(AddressFieldType.UNIT, components[index++]);
        }
        if (dto.getSelectedFields().contains(AddressFieldType.FLAT) && components.length > index) {
            addressDTO.setKat(components[index].getCode());
            componentMap.put(AddressFieldType.FLAT, components[index++]);
        }
        if (dto.getSelectedFields().contains(AddressFieldType.ROOM) && components.length > index) {
            addressDTO.setOda(components[index].getCode());
            componentMap.put(AddressFieldType.ROOM, components[index]);
        }

        for (AddressFieldType field : dto.getSelectedFields()) {
            if (field == AddressFieldType.DEPARTMENT) {
                address.append(department.getCode());
            } else if (field == AddressFieldType.HALL) {
                address.append(hall.getCode());
            } else if (componentMap.containsKey(field)) {
                address.append(componentMap.get(field).getCode());
            }
        }

        addressDTO.setAddress(address.toString().trim());

        return addressDTO;
    }

    public void deleteAddress(long id) {
        aurDepoAdresRepository.deleteById(id);
    }

    public AurDepoUrunAdres checkTemporaryAddress(String depoCode){
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        List<AurDepoUrunAdres> temporaryAddresses = findByDepoNoAndCompanyCodeAndGeciciAdres(depoCode,companyCode,true);
        if(temporaryAddresses.isEmpty()){
            throw new BadRequestAlertException("Depoda geçici adres bulunamadı",ENTITY_NAME,"notfoundtemporaryaddress");
        }
        if(temporaryAddresses.size() > 1){
            throw new BusinessException("Birden fazla geçici adresini sistem desteklememektedir.",ENTITY_NAME,"multipletemporaryaddress");
        }
        return temporaryAddresses.get(0);
    }

    public List<AurOrderAdres> findAll(){
        return aurOrderAdresRepository.findAll();
    }

    public AurOrderAdres findByErpOrderInfo(Long aurOrderId){
        return aurOrderAdresRepository.findByErpOrderInfo(String.valueOf(aurOrderId));
    }

    public AurOrderAdres findByMagentoOrderId(Long magentoOrderId){
        return aurOrderAdresRepository.findByMagentoOrderId(String.valueOf(magentoOrderId));
    }

    public void orderAdres(List<AurOrderAdres> orderAdresList) {

        if (orderAdresList == null || orderAdresList.isEmpty()) {
            return;
        }

        for (AurOrderAdres adres : orderAdresList) {

            try {
                aurOrderAdresRepository.save(adres);
            } catch (Exception e) {
                System.out.println("Adres kayid edilirken HATA cıktı (AurOrderId="
                    + adres.getMagentoOrderId() + "): " + e.getMessage());
            }
        }
    }

}
