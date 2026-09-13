package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.OrderPickingTransaction;
import com.hisarresearch.wms.repository.OrderPickingTransactionRepository;
import com.hisarresearch.wms.service.OrderPickingTransactionQueryService;
import com.hisarresearch.wms.service.OrderPickingTransactionService;
import com.hisarresearch.wms.service.criteria.OrderPickingTransactionCriteria;
import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceDTO;
import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceResponseDTO;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.OrderPickingTransaction}.
 */
@RestController
@RequestMapping("/api")
public class OrderPickingTransactionResource {

    private final Logger log = LoggerFactory.getLogger(OrderPickingTransactionResource.class);

    private static final String ENTITY_NAME = "orderPickingTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderPickingTransactionService orderPickingTransactionService;

    private final OrderPickingTransactionRepository orderPickingTransactionRepository;

    private final OrderPickingTransactionQueryService orderPickingTransactionQueryService;

    public OrderPickingTransactionResource(
        OrderPickingTransactionService orderPickingTransactionService,
        OrderPickingTransactionRepository orderPickingTransactionRepository,
        OrderPickingTransactionQueryService orderPickingTransactionQueryService
    ) {
        this.orderPickingTransactionService = orderPickingTransactionService;
        this.orderPickingTransactionRepository = orderPickingTransactionRepository;
        this.orderPickingTransactionQueryService = orderPickingTransactionQueryService;
    }

    /**
     * {@code POST  /order-picking-transactions} : Create a new orderPickingTransaction.
     *
     * @param orderPickingTransaction the orderPickingTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderPickingTransaction, or with status {@code 400 (Bad Request)} if the orderPickingTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-picking-transactions")
    public ResponseEntity<OrderPickingTransaction> createOrderPickingTransaction(
        @RequestBody OrderPickingTransaction orderPickingTransaction
    ) throws URISyntaxException {
        log.debug("REST request to save OrderPickingTransaction : {}", orderPickingTransaction);
        if (orderPickingTransaction.getId() != null) {
            throw new BadRequestAlertException("A new orderPickingTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderPickingTransaction result = orderPickingTransactionService.save(orderPickingTransaction);
        return ResponseEntity
            .created(new URI("/api/order-picking-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-picking-transactions/:id} : Updates an existing orderPickingTransaction.
     *
     * @param id the id of the orderPickingTransaction to save.
     * @param orderPickingTransaction the orderPickingTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderPickingTransaction,
     * or with status {@code 400 (Bad Request)} if the orderPickingTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderPickingTransaction couldn't be updated.
     */
    @PutMapping("/order-picking-transactions/{id}")
    public ResponseEntity<OrderPickingTransaction> updateOrderPickingTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderPickingTransaction orderPickingTransaction
    ) {
        log.debug("REST request to update OrderPickingTransaction : {}, {}", id, orderPickingTransaction);
        if (orderPickingTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderPickingTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderPickingTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderPickingTransaction result = orderPickingTransactionService.save(orderPickingTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderPickingTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-picking-transactions/:id} : Partial updates given fields of an existing orderPickingTransaction, field will ignore if it is null
     *
     * @param id the id of the orderPickingTransaction to save.
     * @param orderPickingTransaction the orderPickingTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderPickingTransaction,
     * or with status {@code 400 (Bad Request)} if the orderPickingTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the orderPickingTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderPickingTransaction couldn't be updated.
     */
    @PatchMapping(value = "/order-picking-transactions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrderPickingTransaction> partialUpdateOrderPickingTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderPickingTransaction orderPickingTransaction
    )  {
        log.debug("REST request to partial update OrderPickingTransaction partially : {}, {}", id, orderPickingTransaction);
        if (orderPickingTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderPickingTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderPickingTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderPickingTransaction> result = orderPickingTransactionService.partialUpdate(orderPickingTransaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderPickingTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /order-picking-transactions} : get all the orderPickingTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderPickingTransactions in body.
     */
    @GetMapping("/order-picking-transactions")
    public ResponseEntity<List<OrderPickingTransaction>> getAllOrderPickingTransactions(
        OrderPickingTransactionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get OrderPickingTransactions by criteria: {}", criteria);
        Page<OrderPickingTransaction> page = orderPickingTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-picking-transactions/count} : count all the orderPickingTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/order-picking-transactions/count")
    public ResponseEntity<Long> countOrderPickingTransactions(OrderPickingTransactionCriteria criteria) {
        log.debug("REST request to count OrderPickingTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderPickingTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-picking-transactions/:id} : get the "id" orderPickingTransaction.
     *
     * @param id the id of the orderPickingTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderPickingTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-picking-transactions/{id}")
    public ResponseEntity<OrderPickingTransaction> getOrderPickingTransaction(@PathVariable Long id) {
        log.debug("REST request to get OrderPickingTransaction : {}", id);
        Optional<OrderPickingTransaction> orderPickingTransaction = orderPickingTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderPickingTransaction);
    }

    /**
     * {@code DELETE  /order-picking-transactions/:id} : delete the "id" orderPickingTransaction.
     *
     * @param id the id of the orderPickingTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-picking-transactions/{id}")
    public ResponseEntity<Void> deleteOrderPickingTransaction(@PathVariable Long id) {
        log.debug("REST request to delete OrderPickingTransaction : {}", id);
        orderPickingTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    @GetMapping("user-performance")
    public ResponseEntity<List<UserPerformanceResponseDTO>> getPerformance(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
        @RequestParam(required = false) String user,
        @RequestParam(required = false) Integer companyCode
    ) {
        return ResponseEntity.ok(orderPickingTransactionQueryService.getUserPerformance(start, end, user, companyCode));
    }
    @GetMapping("user-all-performance")
    public ResponseEntity<List<UserPerformanceDTO>> getPerformance(@RequestParam(required = false) Integer companyCode) {
        return ResponseEntity.ok(orderPickingTransactionQueryService.getAllUserPerformance(companyCode));
    }


}
