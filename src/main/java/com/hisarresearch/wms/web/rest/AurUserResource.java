package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurUser;
import com.hisarresearch.wms.repository.AurUserRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AurUserResource {

    private final Logger log = LoggerFactory.getLogger(AurUserResource.class);

    private static final String ENTITY_NAME = "aurUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurUserRepository aurUserRepository;

    public AurUserResource(AurUserRepository aurUserRepository) {
        this.aurUserRepository = aurUserRepository;
    }

    /**
     * {@code POST  /aur-users} : Create a new aurUser.
     *
     * @param aurUser the aurUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurUser, or with status {@code 400 (Bad Request)} if the aurUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-users")
    public ResponseEntity<AurUser> createAurUser(@RequestBody AurUser aurUser) throws URISyntaxException {
        log.debug("REST request to save AurUser : {}", aurUser);
        if (aurUser.getId() != null) {
            throw new BadRequestAlertException("A new aurUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurUser result = aurUserRepository.save(aurUser);
        return ResponseEntity
            .created(new URI("/api/aur-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-users/:id} : Updates an existing aurUser.
     *
     * @param id the id of the aurUser to save.
     * @param aurUser the aurUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurUser,
     * or with status {@code 400 (Bad Request)} if the aurUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-users/{id}")
    public ResponseEntity<AurUser> updateAurUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody AurUser aurUser)
        throws URISyntaxException {
        log.debug("REST request to update AurUser : {}, {}", id, aurUser);
        if (aurUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurUser result = aurUserRepository.save(aurUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-users/:id} : Partial updates given fields of an existing aurUser, field will ignore if it is null
     *
     * @param id the id of the aurUser to save.
     * @param aurUser the aurUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurUser,
     * or with status {@code 400 (Bad Request)} if the aurUser is not valid,
     * or with status {@code 404 (Not Found)} if the aurUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurUser> partialUpdateAurUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurUser aurUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurUser partially : {}, {}", id, aurUser);
        if (aurUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurUser> result = aurUserRepository
            .findById(aurUser.getId())
            .map(
                existingAurUser -> {
                    if (aurUser.getLogin() != null) {
                        existingAurUser.setLogin(aurUser.getLogin());
                    }
                    if (aurUser.getCompanyCode() != null) {
                        existingAurUser.setCompanyCode(aurUser.getCompanyCode());
                    }

                    return existingAurUser;
                }
            )
            .map(aurUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurUser.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-users} : get all the aurUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurUsers in body.
     */
    @GetMapping("/aur-users")
    public List<AurUser> getAllAurUsers() {
        log.debug("REST request to get all AurUsers");
        return aurUserRepository.findAll();
    }

    /**
     * {@code GET  /aur-users/:id} : get the "id" aurUser.
     *
     * @param id the id of the aurUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-users/{id}")
    public ResponseEntity<AurUser> getAurUser(@PathVariable Long id) {
        log.debug("REST request to get AurUser : {}", id);
        Optional<AurUser> aurUser = aurUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aurUser);
    }

    /**
     * {@code DELETE  /aur-users/:id} : delete the "id" aurUser.
     *
     * @param id the id of the aurUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-users/{id}")
    public ResponseEntity<Void> deleteAurUser(@PathVariable Long id) {
        log.debug("REST request to delete AurUser : {}", id);
        aurUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
