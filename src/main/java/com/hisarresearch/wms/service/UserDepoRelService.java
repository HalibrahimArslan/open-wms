package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.UserDepoRel;
import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.enumeration.WarehousePickingRuleType;
import com.hisarresearch.wms.repository.UserDepoRelRepository;
import com.hisarresearch.wms.repository.UserRepository;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.dto.UserDepoRelDTO;
import com.hisarresearch.wms.service.dto.UserDepoRelSaveDto;
import com.hisarresearch.wms.service.mapper.UserDepoRelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.mapper.WarehouseMapper;
import com.hisarresearch.wms.exception.validation.InvalidIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserDepoRel}.
 */
@Service
@Transactional
public class UserDepoRelService {

    private final Logger log = LoggerFactory.getLogger(UserDepoRelService.class);

    private final UserDepoRelRepository userDepoRelRepository;

    private final WarehouseRepository warehouseRepository;

    private final UserDepoRelMapper userDepoRelMapper;

    private  final WarehouseMapper warehouseMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserDepoRelService(UserDepoRelRepository userDepoRelRepository, UserDepoRelMapper userDepoRelMapper,
                              WarehouseRepository warehouseRepository,WarehouseMapper warehouseMapper,
                              UserService userService, UserRepository userRepository) {
        this.userDepoRelRepository = userDepoRelRepository;
        this.userDepoRelMapper = userDepoRelMapper;
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Save a userDepoRel.
     *
     * @param userDepoRelDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDepoRelDTO save(UserDepoRelDTO userDepoRelDTO) {
        log.debug("Request to save UserDepoRel : {}", userDepoRelDTO);
        UserDepoRel userDepoRel = userDepoRelMapper.toEntity(userDepoRelDTO);
        userDepoRel = userDepoRelRepository.save(userDepoRel);
        return userDepoRelMapper.toDto(userDepoRel);
    }

    /**
     * Partially update a userDepoRel.
     *
     * @param userDepoRelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserDepoRelDTO> partialUpdate(UserDepoRelDTO userDepoRelDTO) {
        log.debug("Request to partially update UserDepoRel : {}", userDepoRelDTO);

        return userDepoRelRepository
            .findById(userDepoRelDTO.getId())
            .map(
                existingUserDepoRel -> {
                    userDepoRelMapper.partialUpdate(existingUserDepoRel, userDepoRelDTO);
                    return existingUserDepoRel;
                }
            )
            .map(userDepoRelRepository::save)
            .map(userDepoRelMapper::toDto);
    }

    /**
     * Get all the userDepoRels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDepoRelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserDepoRels");
        return userDepoRelRepository.findAll(pageable).map(userDepoRelMapper::toDto);
    }

    /**
     * Get one userDepoRel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDepoRelDTO> findOne(Long id) {
        log.debug("Request to get one user warehouse relation by id : {}", id);
        return userDepoRelRepository.findById(id).map(userDepoRelMapper::toDto);
    }

    /**
     * Get List Of userDepoRel by warehouse id.
     *
     * @param warehouseId the warehouse id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public List<UserDepoRelDTO> findByWarehouse(Long warehouseId) {
        log.debug("Request to get list of user warehouse relation by warehouseId : {}", warehouseId);
        return userDepoRelRepository.findByWarehouse_Id(warehouseId).stream().map(userDepoRelMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Delete the userDepoRel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserDepoRel : {}", id);
        UserDepoRel userDepoRel = userDepoRelRepository.findById(id).orElseThrow(InvalidIdException::new);
        userDepoRelRepository.deleteById(id);
        userService.clearUserCaches(userDepoRel.getUser());
    }

    public void saveBulk(UserDepoRelSaveDto userDepoRelSaveDto){
        userDepoRelSaveDto.getWarehouseList()
        .forEach(warehouseDTO -> {
            Optional<Warehouse> warehouse = warehouseRepository.findByCodeAndCompanyCode(warehouseDTO.getCode(),warehouseDTO.getCompanyCode());
            if (warehouse.isPresent()){
               warehouseDTO.setId(warehouse.get().getId());
            }
            else {
                Warehouse newWarehouse = warehouseMapper.toEntity(warehouseDTO);
                newWarehouse.setCountable(true);
                newWarehouse.setReal(true);
                newWarehouse.setTransferCode(warehouseDTO.getCode());
                newWarehouse.setPickingRuleType(WarehousePickingRuleType.DEFAULT);
                newWarehouse.setReceivingCode(warehouseDTO.getCode());
                Warehouse createdOne = warehouseRepository.save(newWarehouse);
                warehouseDTO.setId(createdOne.getId());
            }
        });
        userDepoRelSaveDto.getUserList().stream()
            .map(requested -> userRepository.findById(requested.getId()).orElseThrow(InvalidIdException::new))
            .forEach(user -> userDepoRelSaveDto.getWarehouseList().forEach(warehouseDTO -> {
            Optional<UserDepoRel> userDepoRel = userDepoRelRepository.findByUser_IdAndWarehouse_Id(user.getId(),warehouseDTO.getId());
            if(userDepoRel.isEmpty()){
                userService.clearUserCaches(user);
                UserDepoRel saveItem = new UserDepoRel();
                saveItem.setUser(user);
                saveItem.setWarehouse(warehouseMapper.toEntity(warehouseDTO));
                userDepoRelRepository.save(saveItem);
            }
        }));
    }

}
