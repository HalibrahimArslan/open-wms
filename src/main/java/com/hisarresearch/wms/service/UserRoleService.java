package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurUserRoleRel;
import com.hisarresearch.wms.repository.AurUserRoleRelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserRoleService {
    private final Logger log = LoggerFactory.getLogger(UserRoleService.class);

    private final AurUserRoleRelRepository userRoleRelRepository;

    public UserRoleService(AurUserRoleRelRepository userRoleRelRepository) {
        this.userRoleRelRepository = userRoleRelRepository;
    }

    @Transactional
    public Page<AurUserRoleRel> findAll(Pageable page) {
        log.debug("Request to get all AurUserRoleRels");
        return userRoleRelRepository.findAll(page);
    }

    public List<AurUserRoleRel> saveUserRoleRelBulk(List<AurUserRoleRel> userRoleList) {
        log.debug("Request to save AurUserRoleRel : {}", userRoleList);
        List<AurUserRoleRel> newUserRoleList =  userRoleList.stream().filter(userRoleRel -> {
            Optional<AurUserRoleRel> presentUserRoleRel = userRoleRelRepository.findByRole_IdAndUser_Id(userRoleRel.getRole().getId(), userRoleRel.getUser().getId());
            return !presentUserRoleRel.isPresent();
        }).collect(Collectors.toList());
        return userRoleRelRepository.saveAll(newUserRoleList);
    }
}
