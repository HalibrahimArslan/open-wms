package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import com.hisarresearch.wms.service.AurOrderMasterQueryService;
import com.hisarresearch.wms.service.AurOrderMasterService;
import com.hisarresearch.wms.service.criteria.OrderMasterCriteria;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.service.dto.AurOrderMasterQueryDTO;
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

@RestController
@RequestMapping("/api")
public class AurOrderMasterResource {
    private static final String ENTITY_NAME = "aurOrderMaster";

    private final Logger log = LoggerFactory.getLogger(AurOrderMasterResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurOrderMasterService aurOrderMasterService;

    private final AurOrderMasterQueryService aurOrderMasterQueryService;

    public AurOrderMasterResource(AurOrderMasterService aurOrderMasterService, AurOrderMasterQueryService aurOrderMasterQueryService) {
        this.aurOrderMasterService = aurOrderMasterService;
        this.aurOrderMasterQueryService = aurOrderMasterQueryService;
    }


    @GetMapping("/order-id/{orderInfo}")
    public Long getOrderIdFromOrderInfo(@PathVariable String orderInfo){
        return aurOrderMasterService.isExistOrderByOrderInfo(orderInfo).getId();
    }

    @PatchMapping(value = "/order-masters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurOrderMaster> updateOrder(@PathVariable(value = "id", required = false) final Long id, @RequestBody AurOrderMaster orderMaster ){
        log.debug("REST request to update AurOrderMaster : {}", orderMaster);
        if(orderMaster.getId() == null){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(!Objects.equals(id,orderMaster.getId())){
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        Optional<AurOrderMaster> result = aurOrderMasterService.partialUpdate(orderMaster);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMaster.getId().toString())
        );

    }

    @GetMapping("/aur-order-info/{id}")
    public String getAurOrderById(@PathVariable Long id) {
        AurOrderMaster order = aurOrderMasterService.findById(id).orElseThrow(InvalidOrderException::new);
        return order.getOrderInfo();
    }

    /**
     * {@code GET  /order-master/count} : count all the order masters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/order-master/count")
    public ResponseEntity<Long> countOrders(OrderMasterCriteria criteria) {
        log.debug("REST request to count order masters by criteria: {}", criteria);
        return ResponseEntity.ok().body(aurOrderMasterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-master} : get all the orderMasters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderMaster in body.
     */
    @GetMapping("/order-master")
    public ResponseEntity<List<AurOrderMasterQueryDTO>> getOrderMasterList(OrderMasterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrderMaster by criteria: {}", criteria);
        Page<AurOrderMasterQueryDTO> page = aurOrderMasterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


}
