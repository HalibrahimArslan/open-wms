package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.UserFirmRel;
import com.hisarresearch.wms.repository.UserFirmRelRepository;
import com.hisarresearch.wms.service.dto.UserFirmRelDTO;
import com.hisarresearch.wms.service.dto.UserFirmRelSaveDto;
import com.hisarresearch.wms.service.mapper.UserFirmRelMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserFirmRel}.
 */
@Service
@Transactional
public class UserFirmRelService {

    private final Logger log = LoggerFactory.getLogger(UserFirmRelService.class);

    private final UserFirmRelRepository userFirmRelRepository;

    private final UserService userService;

    private final UserFirmRelMapper userFirmRelMapper;

    public UserFirmRelService(UserFirmRelRepository userFirmRelRepository, UserFirmRelMapper userFirmRelMapper, UserService userService) {
        this.userFirmRelRepository = userFirmRelRepository;
        this.userFirmRelMapper = userFirmRelMapper;
        this.userService = userService;
    }

    /**
     * Save a userFirmRel.
     *
     * @param userFirmRelDTO the entity to save.
     * @return the persisted entity.
     */
    public UserFirmRelDTO save(UserFirmRelDTO userFirmRelDTO) {
        log.debug("Request to save UserFirmRel : {}", userFirmRelDTO);
        UserFirmRel userFirmRel = userFirmRelMapper.toEntity(userFirmRelDTO);
        userFirmRel = userFirmRelRepository.save(userFirmRel);
        return userFirmRelMapper.toDto(userFirmRel);
    }

    /**
     * Partially update a userFirmRel.
     *
     * @param userFirmRelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserFirmRelDTO> partialUpdate(UserFirmRelDTO userFirmRelDTO) {
        log.debug("Request to partially update UserFirmRel : {}", userFirmRelDTO);

        return userFirmRelRepository
            .findById(userFirmRelDTO.getId())
            .map(
                existingUserFirmRel -> {
                    userFirmRelMapper.partialUpdate(existingUserFirmRel, userFirmRelDTO);

                    return existingUserFirmRel;
                }
            )
            .map(userFirmRelRepository::save)
            .map(userFirmRelMapper::toDto);
    }

    /**
     * Get all the userFirmRels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserFirmRelDTO> findAll() {
        log.debug("Request to get all UserFirmRels");
        return userFirmRelRepository.findAll().stream().map(userFirmRelMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userFirmRel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserFirmRelDTO> findOne(Long id) {
        log.debug("Request to get UserFirmRel : {}", id);
        return userFirmRelRepository.findById(id).map(userFirmRelMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<UserFirmRelDTO> findByUserId(Long userId) {
        log.debug("Request to get UserFirmRel : {}", userId);
        return userFirmRelRepository.findByUser_Id(userId).stream().map(userFirmRelMapper::toDto).collect(Collectors.toList());
    }

    public List<UserFirmRelDTO> findByUsername(){
        Long userId = userService.getUserId();
        return findByUserId(userId);
    }

    /**
     * Delete the userFirmRel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserFirmRel : {}", id);
        userFirmRelRepository.deleteById(id);
    }

    public List<UserFirmRel> saveBulk(UserFirmRelSaveDto userFirmRelSaveDto){
        log.debug("Request to save UserFirmRel : {}", userFirmRelSaveDto);
        List<UserFirmRel> userFirmRelList = new ArrayList<>();
        userFirmRelSaveDto.getUserList().forEach(user -> userFirmRelSaveDto.getFirmList().forEach(firm -> {
            Optional<UserFirmRel> userFirmRel = userFirmRelRepository.findByUser_IdAndFirmCode(user.getId(),firm.getFirmCode());
            if(!userFirmRel.isPresent()){
                UserFirmRel saveItem = new UserFirmRel();

                saveItem.setFirmCode(firm.getFirmCode());
                saveItem.setFirmName(firm.getFirmName());
                saveItem.setUser(user);

                userFirmRelRepository.save(saveItem);

                userFirmRelList.add(saveItem);

            }
        }));

        return userFirmRelList;
    }
}
