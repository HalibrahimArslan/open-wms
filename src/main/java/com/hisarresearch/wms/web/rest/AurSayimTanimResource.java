package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.repository.AurSayimTanimRepository;
import com.hisarresearch.wms.service.AurSayimTanimQueryService;
import com.hisarresearch.wms.service.AurSayimTanimService;
import com.hisarresearch.wms.service.criteria.AurSayimTanimCriteria;
import com.hisarresearch.wms.service.dto.address.AddressCountingResponseDto;
import com.hisarresearch.wms.service.dto.counting.AurSayimDetailDto;
import com.hisarresearch.wms.service.dto.counting.CountingDefinitionDTO;
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

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurSayimTanim}.
 */
@RestController
@RequestMapping("/api")
public class AurSayimTanimResource {

    private final Logger log = LoggerFactory.getLogger(AurSayimTanimResource.class);

    private static final String ENTITY_NAME = "aurSayimTanim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurSayimTanimService aurSayimTanimService;

    private final AurSayimTanimRepository aurSayimTanimRepository;

    private final AurSayimTanimQueryService aurSayimTanimQueryService;

    public AurSayimTanimResource(
        AurSayimTanimService aurSayimTanimService,
        AurSayimTanimRepository aurSayimTanimRepository,
        AurSayimTanimQueryService aurSayimTanimQueryService
    ) {
        this.aurSayimTanimService = aurSayimTanimService;
        this.aurSayimTanimRepository = aurSayimTanimRepository;
        this.aurSayimTanimQueryService = aurSayimTanimQueryService;
    }

    /**
     * {@code POST  /aur-sayim-tanims} : Create a new aurSayimTanim.
     *
     * @param countingDefinitionDTO the aurSayimTanim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurSayimTanim, or with status {@code 400 (Bad Request)} if the aurSayimTanim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-sayim-tanims")
    public ResponseEntity<AurSayimTanim> createAurSayimTanim(@Valid  @RequestBody CountingDefinitionDTO countingDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to save AurSayimTanim : {}", countingDefinitionDTO);
        if (countingDefinitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new aurSayimTanim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(countingDefinitionDTO.getVisibilityAuthorities().length > 1){
            throw new BadRequestAlertException("You can not enter two authority at same time", ENTITY_NAME, "authorityduplication");
        }
        AurSayimTanim result = aurSayimTanimService.save(countingDefinitionDTO);
        return ResponseEntity
            .created(new URI("/api/aur-sayim-tanims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-sayim-tanims/:id} : Updates an existing aurSayimTanim.
     *
     * @param id the id of the aurSayimTanim to save.
     * @param countingDefinitionDTO the aurSayimTanim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurSayimTanim,
     * or with status {@code 400 (Bad Request)} if the aurSayimTanim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurSayimTanim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-sayim-tanims/{id}")
    public ResponseEntity<AurSayimTanim> updateAurSayimTanim(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CountingDefinitionDTO countingDefinitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AurSayimTanim : {}, {}", id, countingDefinitionDTO);
        if (countingDefinitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countingDefinitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurSayimTanimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurSayimTanim result = aurSayimTanimService.save(countingDefinitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countingDefinitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-sayim-tanims/:id} : Partial updates given fields of an existing aurSayimTanim, field will ignore if it is null
     *
     * @param id the id of the aurSayimTanim to save.
     * @param aurSayimTanim the aurSayimTanim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurSayimTanim,
     * or with status {@code 400 (Bad Request)} if the aurSayimTanim is not valid,
     * or with status {@code 404 (Not Found)} if the aurSayimTanim is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurSayimTanim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-sayim-tanims/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurSayimTanim> partialUpdateAurSayimTanim(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurSayimTanim aurSayimTanim
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurSayimTanim partially : {}, {}", id, aurSayimTanim);
        if (aurSayimTanim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurSayimTanim.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurSayimTanimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurSayimTanim> result = aurSayimTanimService.partialUpdate(aurSayimTanim);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurSayimTanim.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-sayim-tanims} : get all the aurSayimTanims.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurSayimTanims in body.
     */
    @GetMapping("/aur-sayim-tanims")
    public ResponseEntity<List<AurSayimTanim>> getAllAurSayimTanims(AurSayimTanimCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AurSayimTanims by criteria: {}", criteria);
        Page<AurSayimTanim> page = aurSayimTanimQueryService.findByCriteria(criteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aur-sayim-tanims/count} : count all the aurSayimTanims.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/aur-sayim-tanims/count")
    public ResponseEntity<Long> countAurSayimTanims(AurSayimTanimCriteria criteria) {
        log.debug("REST request to count AurSayimTanims by criteria: {}", criteria);
        return ResponseEntity.ok().body(aurSayimTanimQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aur-sayim-tanims/:id} : get the "id" aurSayimTanim.
     *
     * @param id the id of the aurSayimTanim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurSayimTanim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-sayim-tanims/{id}")
    public ResponseEntity<AurSayimTanim> getCountingDefinition(@PathVariable Long id) {
        log.debug("REST request to get counting-definition by id : {}", id);
        Optional<AurSayimTanim> countingDefinition = aurSayimTanimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countingDefinition);
    }

    /**
     * {@code DELETE  /aur-sayim-tanims/:id} : delete the "id" aurSayimTanim.
     *
     * @param id the id of the aurSayimTanim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-sayim-tanims/{id}")
    public ResponseEntity<Void> deleteCountingDefinition(@PathVariable Long id) {
        log.debug("REST request to delete counting-definition by id : {}", id);
        aurSayimTanimService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /not-counted-address-list/:countingDefinitionId} : get the "address" non-countable.
     *
     * @param countingDefinitionId the id of the counting-definition to get.
     * @return the {@link ResponseEntity} with status {@code 200}.
     */
    @GetMapping("/not-counted-address-list/{countingDefinitionId}")
    public List<AddressCountingResponseDto> getNotCountedAddressList(@PathVariable Long countingDefinitionId){
        log.debug("REST request to get not counted address by countingDefinitionId : {}", countingDefinitionId);
        return aurSayimTanimService.getNonCountableAddressListByCountingDefinitionId(countingDefinitionId);
    }

    /**
     * {@code GET  /countable-address-list/:countingDefinitionId} : get the count of countable address.
     *
     * @param countingDefinitionId the id of the counting definition to get.
     * @return the {@link ResponseEntity} with status {@code 200}.
     */
    @GetMapping("/count-countable-address-list/{countingDefinitionId}")
    public long getCountOfCountableAddressList(@PathVariable Long countingDefinitionId) {
        log.debug("REST request to get count of  not counted address list by countingDefinitionId : {}",countingDefinitionId);
        return aurSayimTanimService.getCountOfCountableAddressList(countingDefinitionId);
    }

    /**
     * {@code GET  /counting-detail-by-counting-type/:countingDefinitionId} : get product list w.r.t product counting type pairing.
     *
     * @param countingDefinitionId the id of the countingDefinition to get.
     * @return the {@link ResponseEntity} with status {@code 200}.
     */
    @GetMapping("/counting-detail-by-counting-type/{countingDefinitionId}")
    public ResponseEntity<List<AurSayimDetailDto>> getCountingResultByCountingType(@PathVariable Long countingDefinitionId){
        log.debug("REST request to get counting result by countingDefinitionId {}", countingDefinitionId);
        List<AurSayimDetailDto> countingResultByCountingType =aurSayimTanimService.getCountingResultByCountingType(countingDefinitionId);
        return ResponseEntity.ok().body(countingResultByCountingType);
    }

    /**
     * {@code GET  /complete-counting-definition/:id} : get the "id" countingDefinition.
     *
     * @param id the id of the countingDefinitionId to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countingDefinitionId, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/complete-counting-definition/{id}")
    public ResponseEntity<Void> completeCountingDefinition(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "false") boolean clearHistory
        ) {
        log.debug("REST request to complete counting definition by id : {}", id);
        aurSayimTanimService.completeCountingDefinition(id,clearHistory);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET  /counting-definition/count/:id} : get the "id" countingDefinitionId.
     *
     * @param id the id of the countingDefinitionId to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countingDefinitionId, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counting-definition/count/{id}")
    public ResponseEntity<Integer> countOfCountingDefinition(@PathVariable Long id) {
        log.debug("REST request to get count of counting definition by id : {}", id);
        return ResponseEntity.ok().body(aurSayimTanimService.getCountOfProductCountingTypePairingByCountingDefinitionId(id));
    }

    @GetMapping("/check-counting-address/{id}/{addressId}")
    public ResponseEntity<Void> checkCountingAddress(@PathVariable Long id, @PathVariable Long addressId) {
        log.debug("REST request to check counting address by id : {}", id);
        aurSayimTanimService.checkCountingAddress(id,addressId);
        return ResponseEntity.noContent().build();
    }


}
