package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurMenu;
import com.hisarresearch.wms.repository.AurMenuRepository;
import com.hisarresearch.wms.service.AurMenuService;
import com.hisarresearch.wms.service.dto.AurRecursiveMenuDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;
import com.hisarresearch.wms.framework.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurMenu}.
 */
@RestController
@RequestMapping("/api")
public class AurMenuResource {

    private final Logger log = LoggerFactory.getLogger(AurMenuResource.class);

    private static final String ENTITY_NAME = "aurMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurMenuRepository aurMenuRepository;

    private final AurMenuService aurMenuService;

    public AurMenuResource(AurMenuRepository aurMenuRepository,AurMenuService aurMenuService) {
        this.aurMenuRepository = aurMenuRepository;
        this.aurMenuService = aurMenuService;
    }

    /**
     * {@code POST  /aur-menus} : Create a new aurMenu.
     *
     * @param aurMenu the aurMenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurMenu, or with status {@code 400 (Bad Request)} if the aurMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-menus")
    public ResponseEntity<AurMenu> createAurMenu(@RequestBody AurMenu aurMenu) throws Exception {
        log.debug("REST request to save AurMenu : {}", aurMenu);
        if (aurMenu.getId() != null) {
            throw new BadRequestAlertException("A new aurMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurMenu result = aurMenuService.saveMenu(aurMenu);
        return ResponseEntity
            .created(new URI("/api/aur-menus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-menus/:id} : Updates an existing aurMenu.
     *
     * @param id the id of the aurMenu to save.
     * @param aurMenu the aurMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurMenu,
     * or with status {@code 400 (Bad Request)} if the aurMenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-menus/{id}")
    public ResponseEntity<AurMenu> updateAurMenu(@PathVariable(value = "id", required = false) final Long id, @RequestBody AurMenu aurMenu)
        throws URISyntaxException {
        log.debug("REST request to update AurMenu : {}, {}", id, aurMenu);
        if (aurMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurMenu result = aurMenuRepository.save(aurMenu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurMenu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-menus/:id} : Partial updates given fields of an existing aurMenu, field will ignore if it is null
     *
     * @param id the id of the aurMenu to save.
     * @param aurMenu the aurMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurMenu,
     * or with status {@code 400 (Bad Request)} if the aurMenu is not valid,
     * or with status {@code 404 (Not Found)} if the aurMenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-menus/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurMenu> partialUpdateAurMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurMenu aurMenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurMenu partially : {}, {}", id, aurMenu);
        if (aurMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurMenu> result = aurMenuRepository
            .findById(aurMenu.getId())
            .map(
                existingAurMenu -> {
                    if (aurMenu.getParentMenuId() != null) {
                        existingAurMenu.setParentMenuId(aurMenu.getParentMenuId());
                    }
                    if (aurMenu.getMenuName() != null) {
                        existingAurMenu.setMenuName(aurMenu.getMenuName());
                    }
                    if (aurMenu.getMenuType() != null) {
                        existingAurMenu.setMenuType(aurMenu.getMenuType());
                    }
                    if (aurMenu.getCompanyCode() != null) {
                        existingAurMenu.setCompanyCode(aurMenu.getCompanyCode());
                    }

                    return existingAurMenu;
                }
            )
            .map(aurMenuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurMenu.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-menus} : get all the aurMenus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurMenus in body.
     */
    @GetMapping("/aur-menus")
    public ResponseEntity<List<AurMenu>> getAllAurMenus(Pageable pageable) {
        log.debug("REST request to get AurMenusByPage");
        Page<AurMenu> page = aurMenuRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/aur-menus/count")
    public ResponseEntity<Long> getCountOfMenuList() {
        log.debug("REST request to get count of menu list");
        Long countOfMenuList = aurMenuRepository.findAll().stream().count();
        return ResponseEntity.ok().body(countOfMenuList);
    }


    /**
     * {@code GET  /aur-menus/:id} : get the "id" aurMenu.
     *
     * @param id the id of the aurMenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurMenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-menus/{id}")
    public ResponseEntity<AurMenu> getAurMenu(@PathVariable Long id) {
        log.debug("REST request to get AurMenu : {}", id);
        Optional<AurMenu> aurMenu = aurMenuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aurMenu);
    }

    /**
     * {@code DELETE  /aur-menus/:id} : delete the "id" aurMenu.
     *
     * @param id the id of the aurMenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-menus/{id}")
    public ResponseEntity<Void> deleteAurMenu(@PathVariable Long id) {
        log.debug("REST request to delete AurMenu : {}", id);
        aurMenuRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    @GetMapping("/menu-tree")
    public ResponseEntity<List<AurRecursiveMenuDto>> getUserMenuTree(Long menuId) {
        log.debug("user menu list");
        List<AurRecursiveMenuDto> menuList = aurMenuService.getUserMenusArden();
        if(Objects.isNull(menuId)){
            return ResponseEntity.ok(aurMenuService.getUserParentMenus());
        }
        else{
            return ResponseEntity.ok(menuList.stream().filter(q -> q.getId().equals(menuId.toString())).collect(Collectors.toList()));

        }
    }
}
