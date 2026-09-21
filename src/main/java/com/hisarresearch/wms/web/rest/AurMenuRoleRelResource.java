package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurMenuRoleRel;
import com.hisarresearch.wms.domain.RoleMenuRelId;
import com.hisarresearch.wms.repository.AurMenuRoleRelRepository;
import com.hisarresearch.wms.service.AurRoleMenuService;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurMenuRoleRel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AurMenuRoleRelResource {

    private final Logger log = LoggerFactory.getLogger(AurMenuRoleRelResource.class);

    private static final String ENTITY_NAME = "aurMenuRoleRel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurMenuRoleRelRepository aurMenuRoleRelRepository;

    private final AurRoleMenuService aurRoleMenuService;

    public AurMenuRoleRelResource(AurMenuRoleRelRepository aurMenuRoleRelRepository,
                                  AurRoleMenuService aurRoleMenuService) {
        this.aurMenuRoleRelRepository = aurMenuRoleRelRepository;
        this.aurRoleMenuService = aurRoleMenuService;
    }

    /**
     * {@code POST  /aur-menu-role-rels} : Create a new aurMenuRoleRel.
     *
     * @param aurMenuRoleRel the aurMenuRoleRel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurMenuRoleRel, or with status {@code 400 (Bad Request)} if the aurMenuRoleRel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-menu-role-rels")
    public ResponseEntity<List<AurMenuRoleRel>> createAurMenuRoleRel(@RequestBody AurMenuRoleRel aurMenuRoleRel) throws URISyntaxException {
        log.debug("REST request to save AurMenuRoleRel : {}", aurMenuRoleRel);
        List<AurMenuRoleRel> result = aurRoleMenuService.saveRoleMenu(aurMenuRoleRel);
        return ResponseEntity
            .created(new URI("/api/aur-menu-role-rels/" + aurMenuRoleRel.getMenu().getId()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-menu-role-rels/:menuId/:roleId} : Updates an existing aurMenuRoleRel.
     *
     * @param menuId the id of the menu to save.
     * @param roleId the id of the role to save.
     * @param aurMenuRoleRel the aurMenuRoleRel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurMenuRoleRel,
     * or with status {@code 400 (Bad Request)} if the aurMenuRoleRel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurMenuRoleRel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-menu-role-rels/{menuId}/{roleId}")
    public ResponseEntity<AurMenuRoleRel> updateAurMenuRoleRel(
        @PathVariable(value = "menuId", required = false) final Long menuId,
        @PathVariable(value = "roleId", required = false) final Long roleId,
        @RequestBody AurMenuRoleRel aurMenuRoleRel
    ) throws URISyntaxException {
        log.debug("REST request to update AurMenuRoleRel : {} {}, {}", menuId, roleId, aurMenuRoleRel);
        RoleMenuRelId roleMenuRelId = new RoleMenuRelId();
        roleMenuRelId.setMenu(menuId);
        roleMenuRelId.setRole(roleId);
        if (aurMenuRoleRel.getRole() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(roleMenuRelId, aurMenuRoleRel)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurMenuRoleRelRepository.findByMenu_IdAndRole_Id(menuId,roleId).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurMenuRoleRel result = aurMenuRoleRelRepository.save(aurMenuRoleRel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurMenuRoleRel.getMenu().getId().toString() + aurMenuRoleRel.getRole().getId()))
            .body(result);
    }


    /**
     * {@code GET  /aur-menu-role-rels} : get all the aurMenuRoleRels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurMenuRoleRels in body.
     */
    @GetMapping("/aur-menu-role-rels")
    public List<AurMenuRoleRel> getAllAurMenuRoleRels() {
        log.debug("REST request to get all AurMenuRoleRels");
        return aurMenuRoleRelRepository.findAll();
    }

    @DeleteMapping("/aur-menu-role-rels")
    public ResponseEntity<Void> deleteAurMenuRoleRel(@RequestBody AurMenuRoleRel aurMenuRoleRel) {
        log.debug("REST request to delete AurMenuRoleRel : {}", aurMenuRoleRel);
        aurRoleMenuService.deleteRoleMenu(aurMenuRoleRel);
        return ResponseEntity.noContent().build();
    }

}
