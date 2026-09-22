package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.UserDepoRel;
import com.hisarresearch.wms.repository.UserDepoRelRepository;
import com.hisarresearch.wms.service.UserDepoRelQueryService;
import com.hisarresearch.wms.service.UserDepoRelService;
import com.hisarresearch.wms.service.criteria.UserDepoRelCriteria;
import com.hisarresearch.wms.service.dto.UserDepoRelDTO;
import com.hisarresearch.wms.service.dto.UserDepoRelSaveDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

import jakarta.validation.Valid;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.UserDepoRel}.
 */
@RestController
@RequestMapping("/api")
public class UserDepoRelResource {

    private final Logger log = LoggerFactory.getLogger(UserDepoRelResource.class);

    private static final String ENTITY_NAME = "userDepoRel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDepoRelService userDepoRelService;

    private final UserDepoRelRepository userDepoRelRepository;

    private final UserDepoRelQueryService userDepoRelQueryService;

    public UserDepoRelResource(
        UserDepoRelService userDepoRelService,
        UserDepoRelRepository userDepoRelRepository,
        UserDepoRelQueryService userDepoRelQueryService
    ) {
        this.userDepoRelService = userDepoRelService;
        this.userDepoRelRepository = userDepoRelRepository;
        this.userDepoRelQueryService = userDepoRelQueryService;
    }

    /**
     * {@code POST  /user-depo-rels} : Create a new userDepoRel.
     *
     * @param userDepoRelDTO the userDepoRelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDepoRelDTO, or with status {@code 400 (Bad Request)} if the userDepoRel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-depo-rels")
    public ResponseEntity<UserDepoRelDTO> createUserDepoRel(@RequestBody UserDepoRelDTO userDepoRelDTO) throws URISyntaxException {
        log.debug("REST request to save UserDepoRel : {}", userDepoRelDTO);
        if (userDepoRelDTO.getId() != null) {
            throw new BadRequestAlertException("A new userDepoRel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDepoRelDTO result = userDepoRelService.save(userDepoRelDTO);
        return ResponseEntity
            .created(new URI("/api/user-depo-rels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-depo-rels/:id} : Updates an existing userDepoRel.
     *
     * @param id the id of the userDepoRelDTO to save.
     * @param userDepoRelDTO the userDepoRelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDepoRelDTO,
     * or with status {@code 400 (Bad Request)} if the userDepoRelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDepoRelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-depo-rels/{id}")
    public ResponseEntity<UserDepoRelDTO> updateUserDepoRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDepoRelDTO userDepoRelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserDepoRel : {}, {}", id, userDepoRelDTO);
        if (userDepoRelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDepoRelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDepoRelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserDepoRelDTO result = userDepoRelService.save(userDepoRelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDepoRelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-depo-rels/:id} : Partial updates given fields of an existing userDepoRel, field will ignore if it is null
     *
     * @param id the id of the userDepoRelDTO to save.
     * @param userDepoRelDTO the userDepoRelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDepoRelDTO,
     * or with status {@code 400 (Bad Request)} if the userDepoRelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userDepoRelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDepoRelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-depo-rels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserDepoRelDTO> partialUpdateUserDepoRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDepoRelDTO userDepoRelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserDepoRel partially : {}, {}", id, userDepoRelDTO);
        if (userDepoRelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDepoRelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDepoRelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDepoRelDTO> result = userDepoRelService.partialUpdate(userDepoRelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDepoRelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-depo-rels} : get all the userDepoRels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDepoRels in body.
     */
    @GetMapping("/user-depo-rels")
    public ResponseEntity<List<UserDepoRel>> getAllUserDepoRels(UserDepoRelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserDepoRels by criteria: {}", criteria);
        Page<UserDepoRel> page = userDepoRelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-depo-rels/count} : count all the userDepoRels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-depo-rels/count")
    public ResponseEntity<Long> countUserDepoRels(UserDepoRelCriteria criteria) {
        log.debug("REST request to count UserDepoRels by criteria: {}", criteria);
        return ResponseEntity.ok().body(userDepoRelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-depo-rels/:id} : get the "id" userDepoRel.
     *
     * @param id the id of the userDepoRelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDepoRelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-depo-rels/{id}")
    public ResponseEntity<UserDepoRelDTO> getUserDepoRel(@PathVariable Long id) {
        log.debug("REST request to get UserDepoRel : {}", id);
        Optional<UserDepoRelDTO> userDepoRelDTO = userDepoRelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDepoRelDTO);
    }

    /**
     * {@code DELETE  /user-depo-rels/:id} : delete the "id" userDepoRel.
     *
     * @param id the id of the userDepoRelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-depo-rels/{id}")
    public ResponseEntity<Void> deleteUserDepoRel(@PathVariable Long id) {
        log.debug("REST request to delete UserDepoRel : {}", id);
        userDepoRelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user-depo-rels-bulk")
    public void createUserDepoRel(@Valid @RequestBody UserDepoRelSaveDto userDepoRelSaveDto){
        log.debug("REST request to save UserDepoRel Bulk : {}",userDepoRelSaveDto);
        userDepoRelService.saveBulk(userDepoRelSaveDto);
    }
}
