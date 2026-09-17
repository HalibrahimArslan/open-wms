package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.repository.OrderRepository;
import com.hisarresearch.wms.service.OrderQueryService;
import com.hisarresearch.wms.service.OrderService;
import com.hisarresearch.wms.service.criteria.OrderCriteria;
import com.hisarresearch.wms.service.dto.OrderDTO;
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
 * REST controller for managing {@link com.hisarresearch.wms.domain.Order}.
 */
@RestController
@RequestMapping("/api")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "order";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final OrderQueryService orderQueryService;

    public OrderResource(OrderService orderService, OrderRepository orderRepository, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderQueryService = orderQueryService;
    }

    /**
     * {@code POST  /orders} : Create a new order.
     *
     * @param orderDTO the orderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderDTO, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to save Order : {}", orderDTO);
        if (orderDTO.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderDTO result = orderService.save(orderDTO);
        return ResponseEntity
            .created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orders/:id} : Updates an existing order.
     *
     * @param id the id of the orderDTO to save.
     * @param orderDTO the orderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDTO,
     * or with status {@code 400 (Bad Request)} if the orderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderDTO couldn't be updated.
     */
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderDTO orderDTO
    ) {
        log.debug("REST request to update Order : {}, {}", id, orderDTO);
        if (orderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderDTO result = orderService.save(orderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orders/:id} : Partial updates given fields of an existing order, field will ignore if it is null
     *
     * @param id the id of the orderDTO to save.
     * @param orderDTO the orderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDTO,
     * or with status {@code 400 (Bad Request)} if the orderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderDTO couldn't be updated.
     */
    @PatchMapping(value = "/orders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrderDTO> partialUpdateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderDTO orderDTO
    ){
        log.debug("REST request to partial update Order partially : {}, {}", id, orderDTO);
        if (orderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderDTO> result = orderService.partialUpdate(orderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<OrderDTO> orderDTO = orderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

    /**
     * {@code GET  /orders/count} : count all the orders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/orders/count")
    public ResponseEntity<Long> countOrders(OrderCriteria criteria) {
        log.debug("REST request to count orders by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code DELETE  /orders/:id} : delete the "id" order.
     *
     * @param id the id of the orderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /transfer-order/:id} : transfer to micro the "id" order.
     *
     * @param id the id of the orderDTO to transfer.
     * @return the {@link ResponseEntity} with status {@code 200 and list of transferred order}.
     */
    @GetMapping("/transfer-order/{id}")
    public ResponseEntity<OrderDTO> transferToMicro(@PathVariable Long id) throws Exception{
        log.debug("REST request to transfer Order to micro api : {}", id);
        OrderDTO orderDTO = orderService.transferMicro(id);
        return ResponseEntity.ok().body(orderDTO);
    }

    /**
     * {@code GET  /orders} : get all orders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processTress in body.
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(OrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProcessTress by criteria: {}", criteria);
        Page<OrderDTO> page = orderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /complete-order/:id/:orderId} : complete interwarehouse order process and update related tables by id and wms order id.
     *
     * @param id the id of the orderDTO to transfer.
     * @param orderId the id of aurOrderMaster that wms order progress table
     * @return the {@link ResponseEntity} with status {@code 200}.
     */
    @GetMapping("/complete-order/{id}/{orderId}")
    public ResponseEntity<Void> completeOrder(@PathVariable Long id,@PathVariable Long orderId) throws Exception{
        log.debug("REST request to transfer inter-warehouse order w.r.t : {} and progressing wms order by id : {}", id,orderId);
        orderService.completeOrder(id,orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code GET  /cancel-order/:id/:orderId} : cancel interwarehouse order process and update related tables by id and wms order id.
     *
     * @param id the id of the orderDTO to transfer.
     * @param orderId the id of aurOrderMaster that wms order progress table
     * @return the {@link ResponseEntity} with status {@code 200} and cancelled order.
     */
    @GetMapping("/cancel-order/{id}/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id,@PathVariable Long orderId) throws Exception{
        log.debug("REST request to cancel inter-warehouse order w.r.t : {} and progressing wms order by id : {}", id,orderId);
        OrderDTO cancelledOrder = orderService.cancelOrder(id,orderId);
        return ResponseEntity.ok().body(cancelledOrder);
    }
}
