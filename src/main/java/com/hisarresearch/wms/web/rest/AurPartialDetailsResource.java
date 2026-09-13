package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.repository.AurPartialDetailsRepository;
import com.hisarresearch.wms.service.AurPartialDetailsService;
import com.hisarresearch.wms.service.dto.AurPartialDetailsDTO;
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
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurPartialDetails}.
 */
@RestController
@RequestMapping("/api")
public class AurPartialDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AurPartialDetailsResource.class);

    private static final String ENTITY_NAME = "aurPartialDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurPartialDetailsService aurPartialDetailsService;

    private final AurPartialDetailsRepository aurPartialDetailsRepository;

    public AurPartialDetailsResource(
        AurPartialDetailsService aurPartialDetailsService,
        AurPartialDetailsRepository aurPartialDetailsRepository
    ) {
        this.aurPartialDetailsService = aurPartialDetailsService;
        this.aurPartialDetailsRepository = aurPartialDetailsRepository;
    }

    /**
     * {@code POST  /aur-partial-details} : Create a new aurPartialDetails.
     *
     * @param aurPartialDetailsDTO the aurPartialDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurPartialDetailsDTO, or with status {@code 400 (Bad Request)} if the aurPartialDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-partial-details")
    public ResponseEntity<AurPartialDetailsDTO> createAurPartialDetails(@RequestBody AurPartialDetailsDTO aurPartialDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save AurPartialDetails : {}", aurPartialDetailsDTO);
        if (aurPartialDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new aurPartialDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurPartialDetailsDTO result = aurPartialDetailsService.save(aurPartialDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/aur-partial-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-partial-details/:id} : Updates an existing aurPartialDetails.
     *
     * @param id the id of the aurPartialDetailsDTO to save.
     * @param aurPartialDetailsDTO the aurPartialDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurPartialDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the aurPartialDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurPartialDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-partial-details/{id}")
    public ResponseEntity<AurPartialDetailsDTO> updateAurPartialDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurPartialDetailsDTO aurPartialDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AurPartialDetails : {}, {}", id, aurPartialDetailsDTO);
        if (aurPartialDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurPartialDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurPartialDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurPartialDetailsDTO result = aurPartialDetailsService.save(aurPartialDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurPartialDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-partial-details/:id} : Partial updates given fields of an existing aurPartialDetails, field will ignore if it is null
     *
     * @param id the id of the aurPartialDetailsDTO to save.
     * @param aurPartialDetailsDTO the aurPartialDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurPartialDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the aurPartialDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aurPartialDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurPartialDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-partial-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurPartialDetailsDTO> partialUpdateAurPartialDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurPartialDetailsDTO aurPartialDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurPartialDetails partially : {}, {}", id, aurPartialDetailsDTO);
        if (aurPartialDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurPartialDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurPartialDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurPartialDetailsDTO> result = aurPartialDetailsService.partialUpdate(aurPartialDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurPartialDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-partial-details} : get all the aurPartialDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurPartialDetails in body.
     */
    @GetMapping("/aur-partial-details")
    public ResponseEntity<List<AurPartialDetailsDTO>> getAllAurPartialDetails(Pageable pageable) {
        log.debug("REST request to get a page of AurPartialDetails");
        Page<AurPartialDetailsDTO> page = aurPartialDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aur-partial-details/:id} : get the "id" aurPartialDetails.
     *
     * @param id the id of the aurPartialDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurPartialDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-partial-details/{id}")
    public ResponseEntity<AurPartialDetailsDTO> getAurPartialDetails(@PathVariable Long id) {
        log.debug("REST request to get AurPartialDetails : {}", id);
        Optional<AurPartialDetailsDTO> aurPartialDetailsDTO = aurPartialDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aurPartialDetailsDTO);
    }



    /**
     * {@code DELETE  /aur-partial-details/:id} : delete the "id" aurPartialDetails.
     *
     * @param id the id of the aurPartialDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-partial-details/{id}")
    public ResponseEntity<Void> deleteAurPartialDetails(@PathVariable Long id) {
        log.debug("REST request to delete AurPartialDetails : {}", id);
        aurPartialDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
