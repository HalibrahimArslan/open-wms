package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.AurMenu;
import com.hisarresearch.wms.domain.AurVwUserMenuRel;
import com.hisarresearch.wms.repository.AurMenuRepository;
import com.hisarresearch.wms.service.dto.AurRecursiveMenuDto;
import com.hisarresearch.wms.service.dto.AurUserMenuDto;
import com.hisarresearch.wms.exception.validation.InvalidParentMenuIdException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AurMenuService {
    private final Logger log = LoggerFactory.getLogger(AurMenuService.class);

    @Autowired
    private EntityManager em;

    @Autowired
    private AurMenuRepository aurMenuRepository;

    @Autowired
    private UserService userService;

    public List<AurUserMenuDto> getUserMenus(){
        log.debug("Get user menu list wrt username");

        Query q = em.createNamedQuery("getUserMenu");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        q.setParameter("userName", username);
        q.setParameter("companyCode", userService.getUserCompanyCode());
        ModelMapper mm = new ModelMapper();
        List<AurVwUserMenuRel> dbResultList = q.getResultList();

        return dbResultList
            .stream()
            .map(domain -> mm.map(domain, AurUserMenuDto.class))
            .collect(Collectors.toList());
    }

    public List<AurRecursiveMenuDto> getUserMenusArden() {
        log.debug("Get user menu list wrt tree view design");
        List<AurUserMenuDto> resultDtoList = getUserMenus();
        return userMenuDTOToRecursiveDto(resultDtoList);
    }


    public List<AurRecursiveMenuDto> userMenuDTOToRecursiveDto(List<AurUserMenuDto> resultDtoList){
        List<AurRecursiveMenuDto> menuTreeList = new ArrayList<>();

        for(AurUserMenuDto dto :resultDtoList){
            if(dto.getParentMenuId() == 0){
                AurRecursiveMenuDto menuTree = new AurRecursiveMenuDto();
                menuTree.setId(dto.getMenuId().toString());
                menuTree.setName(dto.getMenuName());
                menuTree.setPath(dto.getPath());
                menuTree.setIndex(dto.getIndex());
                menuTree.setCompanyCode(dto.getCompanyCode());
                menuTree.setIcon(dto.getIcon());
                List<AurRecursiveMenuDto> aurMenuMuiBaseDtoList = getMenuChildren(dto.getMenuId(),resultDtoList);
                menuTree.setChildren(aurMenuMuiBaseDtoList);
                menuTreeList.add(menuTree);
            }
        }

        return menuTreeList;

    }

    public List<AurRecursiveMenuDto> getMenuChildren(Integer menuId,List<AurUserMenuDto> resultDtoList){
        List<AurUserMenuDto> aurUserMenuDtoList = resultDtoList.stream().filter(x -> Objects.equals(x.getParentMenuId(), menuId)).collect(Collectors.toList());
        List<AurRecursiveMenuDto> aurMenuMuiBaseDtoList = new ArrayList<>();

        if(!aurUserMenuDtoList.isEmpty()){
            aurUserMenuDtoList.forEach(item -> {
                AurRecursiveMenuDto aurMenuMuiBaseDto = new AurRecursiveMenuDto();
                aurMenuMuiBaseDto.setId(item.getMenuId().toString());
                aurMenuMuiBaseDto.setName(item.getMenuName());
                aurMenuMuiBaseDto.setPath(item.getPath());
                aurMenuMuiBaseDto.setIndex(item.getIndex());
                aurMenuMuiBaseDto.setCompanyCode(item.getCompanyCode());
                aurMenuMuiBaseDto.setIcon(item.getIcon());

                aurMenuMuiBaseDto.setChildren(getMenuChildren(item.getMenuId(),resultDtoList));
                aurMenuMuiBaseDtoList.add(aurMenuMuiBaseDto);
            });

        }

        return aurMenuMuiBaseDtoList;

    }

    public List<AurRecursiveMenuDto> getUserParentMenus(){
        log.debug("Get user menu list wrt tree view design not nested menu type");
        List<AurUserMenuDto> resultDtoList = getUserMenus().stream().filter(q->!q.getMenuType().equals("NESTED")).collect(Collectors.toList());
        return userMenuDTOToRecursiveDto(resultDtoList);
    }

    public AurMenu saveMenu(AurMenu aurMenu) {
        log.debug("Save new menu definition");

        if(aurMenu.getParentMenuId() != 0){
            aurMenuRepository.findById(Long.valueOf(aurMenu.getParentMenuId())).orElseThrow(InvalidParentMenuIdException::new);
        }

        if(aurMenu.getMenuName().isBlank()){
            throw new RuntimeException("Menu name has to be non-null");
        }

        AurMenu savedOne = aurMenuRepository.save(aurMenu);
        savedOne.setMenuType("TERMINAL");

        return savedOne;
    }



}
