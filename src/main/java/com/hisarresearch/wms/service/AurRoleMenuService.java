package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurMenu;
import com.hisarresearch.wms.domain.AurMenuRoleRel;
import com.hisarresearch.wms.domain.AurRole;
import com.hisarresearch.wms.repository.AurMenuRepository;
import com.hisarresearch.wms.repository.AurMenuRoleRelRepository;
import com.hisarresearch.wms.repository.AurRoleRepository;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AurRoleMenuService {
    private final Logger log = LoggerFactory.getLogger(AurRoleMenuService.class);

    private final AurMenuRoleRelRepository aurMenuRoleRelRepository;
    private final AurRoleRepository aurRoleRepository;
    private final AurMenuRepository aurMenuRepository;

    public AurRoleMenuService(AurMenuRoleRelRepository aurMenuRoleRelRepository,
                              AurRoleRepository aurRoleRepository, AurMenuRepository aurMenuRepository) {
        this.aurMenuRoleRelRepository = aurMenuRoleRelRepository;
        this.aurRoleRepository = aurRoleRepository;
        this.aurMenuRepository = aurMenuRepository;
    }

    public List<AurMenuRoleRel> saveRoleMenu(AurMenuRoleRel aurMenuRoleRel) {
        log.debug("Request to save AurMenuRoleRel : {}", aurMenuRoleRel);
        List<AurMenuRoleRel> saveList = new ArrayList<>();
        saveList.add(aurMenuRoleRel);
        AurMenu saveMenu = aurMenuRepository.findById(aurMenuRoleRel.getMenu().getId()).orElseThrow(() -> new BadRequestAlertException("Menu does not exist", "aurMenuRoleRel", "AurMenuRoleRel"));
        AurRole saveRole = aurRoleRepository.findById(aurMenuRoleRel.getRole().getId()).orElseThrow(()-> new BadRequestAlertException("Role does not exist", "aurRole", "AurRole"));
        if(saveMenu.getParentMenuId() == 0){
            aurMenuRepository.findByParentMenuId(Math.toIntExact(saveMenu.getId())).forEach(aurMenu -> {
                AurMenuRoleRel roleRel = new AurMenuRoleRel();
                roleRel.setMenu(aurMenu);
                roleRel.setRole(saveRole);
                saveList.add(roleRel);
            });
        }

        return aurMenuRoleRelRepository.saveAll(saveList);
    }

    public void deleteRoleMenu(AurMenuRoleRel aurMenuRoleRel) {
        log.debug("Request to delete AurMenuRoleRel : {}", aurMenuRoleRel);
        List<AurMenuRoleRel> deleteList = new ArrayList<>();
        deleteList.add(aurMenuRoleRel);
        if(aurMenuRoleRel.getMenu().getParentMenuId() == 0){
            aurMenuRepository.findByParentMenuId(Math.toIntExact(aurMenuRoleRel.getMenu().getId())).forEach(menu -> {
                AurMenuRoleRel roleRel = new AurMenuRoleRel();
                roleRel.setMenu(menu);
                roleRel.setRole(aurMenuRoleRel.getRole());
                deleteList.add(roleRel);
            });
        }

        aurMenuRoleRelRepository.deleteAll(deleteList);
    }
}
