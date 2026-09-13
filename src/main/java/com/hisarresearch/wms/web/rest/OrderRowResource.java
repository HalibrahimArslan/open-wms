package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.repository.OrderRowRepository;
import com.hisarresearch.wms.service.OrderRowService;
import com.hisarresearch.wms.service.dto.OrderRowDTO;
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
 * REST controller for managing {@link com.hisarresearch.wms.domain.OrderRow}.
 */
@RestController
@RequestMapping("/api")
public class OrderRowResource {

    private final Logger log = LoggerFactory.getLogger(OrderRowResource.class);

    private static final String ENTITY_NAME = "orderRow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderRowService orderRowService;

    private final OrderRowRepository orderRowRepository;

    public OrderRowResource(OrderRowService orderRowService, OrderRowRepository orderRowRepository) {
        this.orderRowService = orderRowService;
        this.orderRowRepository = orderRowRepository;
    }

    /**
     * {@code POST  /order-rows} : Create a new orderRow.
     *
     * @param orderRowDTO the orderRowDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderRowDTO, or with status {@code 400 (Bad Request)} if the orderRow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-rows")
    public ResponseEntity<OrderRowDTO> createOrderRow(@RequestBody OrderRowDTO orderRowDTO) throws URISyntaxException {
        log.debug("REST request to save OrderRow : {}", orderRowDTO);
        if (orderRowDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderRow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderRowDTO result = orderRowService.save(orderRowDTO);
        return ResponseEntity
            .created(new URI("/api/order-rows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-rows/:id} : Updates an existing orderRow.
     *
     * @param id the id of the orderRowDTO to save.
     * @param orderRowDTO the orderRowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderRowDTO,
     * or with status {@code 400 (Bad Request)} if the orderRowDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderRowDTO couldn't be updated.
     */
    @PutMapping("/order-rows/{id}")
    public ResponseEntity<OrderRowDTO> updateOrderRow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderRowDTO orderRowDTO
    ) {
        log.debug("REST request to update OrderRow : {}, {}", id, orderRowDTO);
        if (orderRowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderRowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderRowDTO result = orderRowService.save(orderRowDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderRowDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-rows/:id} : Partial updates given fields of an existing orderRow, field will ignore if it is null
     *
     * @param id the id of the orderRowDTO to save.
     * @param orderRowDTO the orderRowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderRowDTO,
     * or with status {@code 400 (Bad Request)} if the orderRowDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderRowDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderRowDTO couldn't be updated.
     */
    @PatchMapping(value = "/order-rows/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrderRowDTO> partialUpdateOrderRow(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderRowDTO orderRowDTO
    ) {
        log.debug("REST request to partial update OrderRow partially : {}, {}", id, orderRowDTO);
        if (orderRowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderRowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderRowDTO> result = orderRowService.partialUpdate(orderRowDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderRowDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-rows} : get all the orderRows.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderRows in body.
     */
    @GetMapping("/order-rows")
    public List<OrderRowDTO> getAllOrderRows() {
        log.debug("REST request to get all OrderRows");
        return orderRowService.findAll();
    }

    /**
     * {@code GET  /order-rows/:id} : get the "id" orderRow.
     *
     * @param id the id of the orderRowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderRowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-rows/{id}")
    public ResponseEntity<OrderRowDTO> getOrderRow(@PathVariable Long id) {
        log.debug("REST request to get OrderRow : {}", id);
        Optional<OrderRowDTO> orderRowDTO = orderRowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderRowDTO);
    }

    /**
     * {@code GET  /order-rows/:orderId} : get the "orderId" orderRow.
     *
     * @param orderId the id of the orderRowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderRowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-rows-list/{orderId}")
    public ResponseEntity<List<OrderRowDTO>> getOrderRowsByOrderId(@PathVariable Long orderId) {
        log.debug("REST request to get OrderRow List by orderId : {}", orderId);
        List<OrderRowDTO> orderRowDTO = orderRowService.findByOrderId(orderId);
        return ResponseEntity.ok().body(orderRowDTO);
    }

    /**
     * {@code DELETE  /order-rows/:id} : delete the "id" orderRow.
     *
     * @param id the id of the orderRowDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-rows/{id}")
    public ResponseEntity<Void> deleteOrderRow(@PathVariable Long id) {
        log.debug("REST request to delete OrderRow : {}", id);
        orderRowService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
