package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.WarehouseQueryService;
import com.hisarresearch.wms.service.WarehouseService;
import com.hisarresearch.wms.service.criteria.WarehouseCriteria;
import com.hisarresearch.wms.service.mapper.WarehouseMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Warehouse}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WarehouseResource {

    private final Logger log = LoggerFactory.getLogger(WarehouseResource.class);

    private static final String ENTITY_NAME = "warehouse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private WarehouseQueryService warehouseQueryService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseMapper warehouseMapper;

    private final WarehouseRepository warehouseRepository;

    public WarehouseResource(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * {@code POST  /depos} : Create a new depo.
     *
     * @param warehouse the depo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depo, or with status {@code 400 (Bad Request)} if the depo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depos")
    public ResponseEntity<Warehouse> createDepo(@RequestBody Warehouse warehouse) throws URISyntaxException {
        log.debug("REST request to save Depo : {}", warehouse);
        if (warehouse.getId() != null) {
            throw new BadRequestAlertException("A new depo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Warehouse result = warehouseRepository.save(warehouse);
        return ResponseEntity
            .created(new URI("/api/depos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depos/:id} : Updates an existing depo.
     *
     * @param id the id of the depo to save.
     * @param warehouse the depo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depo,
     * or with status {@code 400 (Bad Request)} if the depo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depos/{id}")
    public ResponseEntity<Warehouse> updateDepo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Warehouse warehouse)
        throws URISyntaxException {
        log.debug("REST request to update Depo : {}, {}", id, warehouse);
        if (warehouse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warehouseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Warehouse result = warehouseRepository.save(warehouse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warehouse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depos/:id} : Partial updates given fields of an existing depo, field will ignore if it is null
     *
     * @param id the id of the depo to save.
     * @param warehouse the depo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depo,
     * or with status {@code 400 (Bad Request)} if the depo is not valid,
     * or with status {@code 404 (Not Found)} if the depo is not found,
     * or with status {@code 500 (Internal Server Error)} if the depo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Warehouse> partialUpdateDepo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Warehouse warehouse)
        throws URISyntaxException {
        log.debug("REST request to partial update Depo partially : {}, {}", id, warehouse);
        if (warehouse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warehouseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Warehouse> result = warehouseRepository
            .findById(warehouse.getId())
            .map(
                existingDepo -> {
                    if (warehouse.getCode() != null) {
                        existingDepo.setCode(warehouse.getCode());
                    }
                    if (warehouse.getName() != null) {
                        existingDepo.setName(warehouse.getName());
                    }
                    if (warehouse.getCompanyCode() != null) {
                        existingDepo.setCompanyCode(warehouse.getCompanyCode());
                    }
                    if (warehouse.getTransferCode() != null) {
                        existingDepo.setTransferCode(warehouse.getTransferCode());
                    }
                    if (warehouse.getAutoScan() != null) {
                        existingDepo.setAutoScan(warehouse.getAutoScan());
                    }
                    if(warehouse.getUniquePickingAddress() != null){
                        existingDepo.setUniquePickingAddress(warehouse.getUniquePickingAddress());
                    }

                    return existingDepo;
                }
            )
            .map(warehouseRepository::save);

        warehouseService.clearRelatedUserCaches(id);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warehouse.getId().toString())
        );
    }

    /**
     * {@code GET  /depos/:id} : get the "id" depo.
     *
     * @param id the id of the depo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depos/{id}")
    public ResponseEntity<Warehouse> getDepo(@PathVariable Long id) {
        log.debug("REST request to get Depo : {}", id);
        Optional<Warehouse> depo = warehouseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(depo);
    }

    /**
     * {@code DELETE  /depos/:id} : delete the "id" depo.
     *
     * @param id the id of the depo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depos/{id}")
    public ResponseEntity<Void> deleteDepo(@PathVariable Long id) {
        log.debug("REST request to delete Depo : {}", id);
        warehouseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/warehouse")
    public ResponseEntity<List<Warehouse>> getWarehouseList(WarehouseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get a page of warehouses by criteria: {}", criteria);
        Page<Warehouse> page = warehouseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depo-by-criteria/count} : count all the warehouses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/warehouse/count")
    public ResponseEntity<Long> countWarehouses(WarehouseCriteria criteria) {
        log.debug("REST request to count warehouses by criteria: {}", criteria);
        return ResponseEntity.ok().body(warehouseQueryService.countByCriteria(criteria));
    }

    @PostMapping ("/create-vm-warehouse")
    public ResponseEntity<String> createVmWarehouse(@RequestBody Warehouse warehouse) throws Exception{
        log.debug("REST request to save virtual warehouse by depo params: {}", warehouse);
        String resultMessage = warehouseService.createVirtualWarehouse(warehouse.getCode(), warehouse.getName());
        return ResponseEntity.created(new URI("/api/create-vm-depo/" + resultMessage))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resultMessage))
            .body(resultMessage);

    }
}
