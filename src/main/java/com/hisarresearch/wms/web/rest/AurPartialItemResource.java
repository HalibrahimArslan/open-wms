package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.repository.AurPartialItemRepository;
import com.hisarresearch.wms.service.AurPartialItemQueryService;
import com.hisarresearch.wms.service.AurPartialItemService;
import com.hisarresearch.wms.service.criteria.AurPartialItemCriteria;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.service.dto.AurPartialResponseDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurPartialItem}.
 */
@RestController
@RequestMapping("/api")
public class AurPartialItemResource {

    private final Logger log = LoggerFactory.getLogger(AurPartialItemResource.class);

    private static final String ENTITY_NAME = "aurPartialItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurPartialItemService aurPartialItemService;

    private final AurPartialItemQueryService aurPartialItemQueryService;

    private final AurPartialItemRepository aurPartialItemRepository;

    public AurPartialItemResource(AurPartialItemService aurPartialItemService,
                                  AurPartialItemRepository aurPartialItemRepository,
                                  AurPartialItemQueryService aurPartialItemQueryService) {
        this.aurPartialItemService = aurPartialItemService;
        this.aurPartialItemRepository = aurPartialItemRepository;
        this.aurPartialItemQueryService = aurPartialItemQueryService;
    }

    /**
     * {@code POST  /aur-partial-items} : Create a new aurPartialItem.
     *
     * @param aurPartialItemDTO the aurPartialItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurPartialItemDTO, or with status {@code 400 (Bad Request)} if the aurPartialItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-partial-items")
    public ResponseEntity<AurPartialItemDTO> createAurPartialItem(@Valid @RequestBody AurPartialItemDTO aurPartialItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save AurPartialItem : {}", aurPartialItemDTO);
        if (aurPartialItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new aurPartialItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurPartialItemDTO result = aurPartialItemService.save(aurPartialItemDTO);
        return ResponseEntity
            .created(new URI("/api/aur-partial-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-partial-items/:id} : Updates an existing aurPartialItem.
     *
     * @param id the id of the aurPartialItemDTO to save.
     * @param aurPartialItemDTO the aurPartialItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurPartialItemDTO,
     * or with status {@code 400 (Bad Request)} if the aurPartialItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurPartialItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-partial-items/{id}")
    public ResponseEntity<AurPartialItemDTO> updateAurPartialItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AurPartialItemDTO aurPartialItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AurPartialItem : {}, {}", id, aurPartialItemDTO);
        if (aurPartialItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurPartialItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurPartialItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurPartialItemDTO result = aurPartialItemService.save(aurPartialItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurPartialItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-partial-items/:id} : Partial updates given fields of an existing aurPartialItem, field will ignore if it is null
     *
     * @param id the id of the aurPartialItemDTO to save.
     * @param aurPartialItemDTO the aurPartialItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurPartialItemDTO,
     * or with status {@code 400 (Bad Request)} if the aurPartialItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aurPartialItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurPartialItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-partial-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurPartialItemDTO> partialUpdateAurPartialItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AurPartialItemDTO aurPartialItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurPartialItem partially : {}, {}", id, aurPartialItemDTO);
        if (aurPartialItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurPartialItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurPartialItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurPartialItemDTO> result = aurPartialItemService.partialUpdate(aurPartialItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurPartialItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-partial-items} : get all the aurPartialItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurPartialItems in body.
     */
    @GetMapping("/aur-partial-items")
    public ResponseEntity<List<AurPartialItem>> getAllAurPartialItems(AurPartialItemCriteria aurPartialItemCriteria, Pageable pageable) {
        log.debug("REST request to get a page of AurPartialItems");
        Page<AurPartialItem> page = aurPartialItemQueryService.findByCriteria(aurPartialItemCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/aur-partial-items/count")
    public ResponseEntity<Long> countAurPartialItems(AurPartialItemCriteria aurPartialItemCriteria) {
        log.debug("REST request to count partial item list by criteria: {}", aurPartialItemCriteria);
        return ResponseEntity.ok().body(aurPartialItemQueryService.countByCriteria(aurPartialItemCriteria));
    }


    /**
     * {@code GET  /aur-partial-items/:id} : get the "id" aurPartialItem.
     *
     * @param id the id of the aurPartialItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurPartialItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-partial-items/{id}")
    public ResponseEntity<AurPartialItemDTO> getAurPartialItem(@PathVariable Long id) {
        log.debug("REST request to get AurPartialItem : {}", id);
        Optional<AurPartialItemDTO> aurPartialItemDTO = aurPartialItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aurPartialItemDTO);
    }


    /**
     * {@code POST  /possible-aur-partial-list} : get the possible partial list.
     *
     * @param barcodeList the barcodes of the related order items.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurPartialDetailsDTO
     */
    @PostMapping("/possible-aur-partial-list")
    public ResponseEntity<List<AurPartialResponseDto>> getPossibleMasterList(@RequestBody List<String> barcodeList) {
        log.debug("REST request to get possible AurPartialDetails : {}", barcodeList);
        List<AurPartialResponseDto> aurPartialDetailsDTO = aurPartialItemService.findRelevantItemByBarcode(barcodeList);
        return ResponseEntity.ok(aurPartialDetailsDTO);
    }

    /**
     * {@code DELETE  /aur-partial-items/:id} : delete the "id" aurPartialItem.
     *
     * @param id the id of the aurPartialItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-partial-items/{id}")
    public ResponseEntity<Void> deleteAurPartialItem(@PathVariable Long id) {
        log.debug("REST request to delete AurPartialItem : {}", id);
        aurPartialItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /aur-partial-items-with-details/:id} : delete the "id" aurPartialItem with child items.
     *
     * @param id the id of the aurPartialItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-partial-items-with-details/{id}")
    public ResponseEntity<Void> deleteAurPartialItemWithDetails(@PathVariable Long id) {
        log.debug("REST request to delete AurPartialItem : {}", id);
        aurPartialItemService.deleteWithChildren(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/aur-partial-items-with-detail")
    public ResponseEntity<List<AurPartialResponseDto>> getPackageDetails(@RequestBody List<String> packageCodes){
        log.debug("REST request to getPackageDetails : {}",packageCodes);
        return ResponseEntity.ok(aurPartialItemService.getPartialItemDetail(packageCodes));

    }

    @PostMapping("/aur-partial-items-by-id")
    public ResponseEntity<List<AurPartialResponseDto>> getPackageDetailsById(@RequestBody List<Long> idList){
        log.debug("REST request to getPackageDetails : {}",idList);
        return ResponseEntity.ok(aurPartialItemService.getPartialItemDetailById(idList));

    }


    @GetMapping("/update-partial-item/{partialItemId}")
    public ResponseEntity<AurPartialItem> updatePartialItemFromMicro(@PathVariable Long partialItemId) throws Exception {
        log.debug("REST request to update partial item from micro: {}", partialItemId);
        AurPartialItem updatedPartialItem =  aurPartialItemService.updatePartialFromMicro(partialItemId);
        return ResponseEntity.ok().body(updatedPartialItem);
    }

    @GetMapping("/transfer-from-micro/{stockCode}")
    public ResponseEntity<AurPartialItem> transferFromMicro(@PathVariable String stockCode) throws Exception {
        log.debug("REST request to transfer partial item information from micro by stockCode {}", stockCode);
        AurPartialItem transferPartialFromMicro =  aurPartialItemService.transferPartialFromMicro(stockCode);
        return ResponseEntity.ok().body(transferPartialFromMicro);
    }



}
