package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurRole;
import com.hisarresearch.wms.repository.AurRoleRepository;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurRole}.
 */
@RestController
@RequestMapping("/api")
public class AurRoleResource {

    private final Logger log = LoggerFactory.getLogger(AurRoleResource.class);

    private static final String ENTITY_NAME = "aurRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurRoleRepository aurRoleRepository;

    public AurRoleResource(AurRoleRepository aurRoleRepository) {
        this.aurRoleRepository = aurRoleRepository;
    }

    /**
     * {@code POST  /aur-roles} : Create a new aurRole.
     *
     * @param aurRole the aurRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurRole, or with status {@code 400 (Bad Request)} if the aurRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-roles")
    public ResponseEntity<AurRole> createAurRole(@RequestBody AurRole aurRole) throws URISyntaxException {
        log.debug("REST request to save AurRole : {}", aurRole);
        if (aurRole.getId() != null) {
            throw new BadRequestAlertException("A new aurRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurRole result = aurRoleRepository.save(aurRole);
        return ResponseEntity
            .created(new URI("/api/aur-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-roles/:id} : Updates an existing aurRole.
     *
     * @param id the id of the aurRole to save.
     * @param aurRole the aurRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurRole,
     * or with status {@code 400 (Bad Request)} if the aurRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-roles/{id}")
    public ResponseEntity<AurRole> updateAurRole(@PathVariable(value = "id", required = false) final Long id, @RequestBody AurRole aurRole)
        throws URISyntaxException {
        log.debug("REST request to update AurRole : {}, {}", id, aurRole);
        if (aurRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurRole result = aurRoleRepository.save(aurRole);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-roles/:id} : Partial updates given fields of an existing aurRole, field will ignore if it is null
     *
     * @param id the id of the aurRole to save.
     * @param aurRole the aurRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurRole,
     * or with status {@code 400 (Bad Request)} if the aurRole is not valid,
     * or with status {@code 404 (Not Found)} if the aurRole is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-roles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurRole> partialUpdateAurRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurRole aurRole
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurRole partially : {}, {}", id, aurRole);
        if (aurRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurRole.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurRole> result = aurRoleRepository
            .findById(aurRole.getId())
            .map(
                existingAurRole -> {
                    if (aurRole.getRoleName() != null) {
                        existingAurRole.setRoleName(aurRole.getRoleName());
                    }
                    if (aurRole.getCompanyCode() != null) {
                        existingAurRole.setCompanyCode(aurRole.getCompanyCode());
                    }

                    return existingAurRole;
                }
            )
            .map(aurRoleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurRole.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-roles} : get all the aurRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurRoles in body.
     */
    @GetMapping("/aur-roles")
    public List<AurRole> getAllAurRoles(@RequestParam("companyCode") Integer companyCode) {
        log.debug("REST request to get all AurRoles");
        return aurRoleRepository.findAllByCompanyCode(companyCode);
    }

    /**
     * {@code GET  /aur-roles/:id} : get the "id" aurRole.
     *
     * @param id the id of the aurRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-roles/{id}")
    public ResponseEntity<AurRole> getAurRole(@PathVariable Long id) {
        log.debug("REST request to get AurRole : {}", id);
        Optional<AurRole> aurRole = aurRoleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aurRole);
    }

    /**
     * {@code DELETE  /aur-roles/:id} : delete the "id" aurRole.
     *
     * @param id the id of the aurRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-roles/{id}")
    public ResponseEntity<Void> deleteAurRole(@PathVariable Long id) {
        log.debug("REST request to delete AurRole : {}", id);
        aurRoleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
