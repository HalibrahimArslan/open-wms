package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.UserFirmRel;
import com.hisarresearch.wms.repository.UserFirmRelRepository;
import com.hisarresearch.wms.service.UserFirmRelQueryService;
import com.hisarresearch.wms.service.UserFirmRelService;
import com.hisarresearch.wms.service.criteria.UserFirmRelCriteria;
import com.hisarresearch.wms.service.dto.UserFirmRelDTO;
import com.hisarresearch.wms.service.dto.UserFirmRelSaveDto;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.UserFirmRel}.
 */
@RestController
@RequestMapping("/api")
public class UserFirmRelResource {

    private final Logger log = LoggerFactory.getLogger(UserFirmRelResource.class);

    private static final String ENTITY_NAME = "userFirmRel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserFirmRelService userFirmRelService;

    private final UserFirmRelRepository userFirmRelRepository;

    private final UserFirmRelQueryService userFirmRelQueryService;

    public UserFirmRelResource(UserFirmRelService userFirmRelService, UserFirmRelRepository userFirmRelRepository,UserFirmRelQueryService userFirmRelQueryService) {
        this.userFirmRelService = userFirmRelService;
        this.userFirmRelRepository = userFirmRelRepository;
        this.userFirmRelQueryService = userFirmRelQueryService;
    }

    /**
     * {@code POST  /user-firm-rels} : Create a new userFirmRel.
     *
     * @param userFirmRelDTO the userFirmRelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userFirmRelDTO, or with status {@code 400 (Bad Request)} if the userFirmRel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-firm-rels")
    public ResponseEntity<UserFirmRelDTO> createUserFirmRel(@RequestBody UserFirmRelDTO userFirmRelDTO) throws URISyntaxException {
        log.debug("REST request to save UserFirmRel : {}", userFirmRelDTO);
        if (userFirmRelDTO.getId() != null) {
            throw new BadRequestAlertException("A new userFirmRel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserFirmRelDTO result = userFirmRelService.save(userFirmRelDTO);
        return ResponseEntity
            .created(new URI("/api/user-firm-rels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-firm-rels/:id} : Updates an existing userFirmRel.
     *
     * @param id the id of the userFirmRelDTO to save.
     * @param userFirmRelDTO the userFirmRelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFirmRelDTO,
     * or with status {@code 400 (Bad Request)} if the userFirmRelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userFirmRelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-firm-rels/{id}")
    public ResponseEntity<UserFirmRelDTO> updateUserFirmRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserFirmRelDTO userFirmRelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserFirmRel : {}, {}", id, userFirmRelDTO);
        if (userFirmRelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userFirmRelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userFirmRelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserFirmRelDTO result = userFirmRelService.save(userFirmRelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userFirmRelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-firm-rels/:id} : Partial updates given fields of an existing userFirmRel, field will ignore if it is null
     *
     * @param id the id of the userFirmRelDTO to save.
     * @param userFirmRelDTO the userFirmRelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFirmRelDTO,
     * or with status {@code 400 (Bad Request)} if the userFirmRelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userFirmRelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userFirmRelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-firm-rels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserFirmRelDTO> partialUpdateUserFirmRel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserFirmRelDTO userFirmRelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserFirmRel partially : {}, {}", id, userFirmRelDTO);
        if (userFirmRelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userFirmRelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userFirmRelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserFirmRelDTO> result = userFirmRelService.partialUpdate(userFirmRelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userFirmRelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-firm-rels/:id} : get the "id" userFirmRel.
     *
     * @param id the id of the userFirmRelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userFirmRelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-firm-rels/{id}")
    public ResponseEntity<UserFirmRelDTO> getUserFirmRel(@PathVariable Long id) {
        log.debug("REST request to get UserFirmRel : {}", id);
        Optional<UserFirmRelDTO> userFirmRelDTO = userFirmRelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userFirmRelDTO);
    }


    @GetMapping("/user-firm-rels")
    public ResponseEntity<List<UserFirmRelDTO>> getAllUserFirmRels(UserFirmRelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserFirmRels by criteria: {}", criteria);
        Page<UserFirmRelDTO> page = userFirmRelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-depo-rels/count} : count all the userDepoRels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-firm-rels/count")
    public ResponseEntity<Long> countUserDepoRels(UserFirmRelCriteria criteria) {
        log.debug("REST request to count UserFirmRels by criteria: {}", criteria);
        return ResponseEntity.ok().body(userFirmRelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-firm-rels-by-user}
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userFirmRelDTO, or with status {@code 404 (Not Found)}.
     */

    @GetMapping("/user-firm-rels-by-user")
    public List<UserFirmRelDTO> getUserFirmRelByUser() {
        return userFirmRelService.findByUsername();
    }

    /**
     * {@code DELETE  /user-firm-rels/:id} : delete the "id" userFirmRel.
     *
     * @param id the id of the userFirmRelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-firm-rels/{id}")
    public ResponseEntity<Void> deleteUserFirmRel(@PathVariable Long id) {
        log.debug("REST request to delete UserFirmRel : {}", id);
        userFirmRelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/user-firm-rel-bulk")
    public ResponseEntity<List<UserFirmRel>> createUserFirmRelBulk(@RequestBody UserFirmRelSaveDto userFirmRelSaveDto) throws BadRequestAlertException{
        log.debug("REST request to save UserFirmRel : {}", userFirmRelSaveDto);

        if(userFirmRelSaveDto.getUserList().size() == 0){
            throw  new BadRequestAlertException("Invalid userIdList",ENTITY_NAME,"Invalid userIdList");
        }

        List<UserFirmRel> result = userFirmRelService.saveBulk(userFirmRelSaveDto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.toString()))
            .body(result);

    }
}
