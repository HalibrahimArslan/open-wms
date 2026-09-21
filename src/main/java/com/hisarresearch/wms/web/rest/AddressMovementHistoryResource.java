package com.hisarresearch.wms.web.rest;


import com.hisarresearch.wms.domain.AddressMovementHistory;
import com.hisarresearch.wms.service.AddressMovementHistoryQueryService;
import com.hisarresearch.wms.service.AddressMovementHistoryService;
import com.hisarresearch.wms.service.criteria.AddressMovementHistoryCriteria;
import com.hisarresearch.wms.service.dto.AddressPlacementDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import java.util.List;

/**
 * REST controller for managing the address placement from temporary area to constant locations.
 */


@RestController
@RequestMapping("/api")
public class AddressMovementHistoryResource {

    private final Logger log = LoggerFactory.getLogger(AddressMovementHistoryResource.class);


    private final AddressMovementHistoryService addressMovementHistoryService;

    private final AddressMovementHistoryQueryService addressMovementHistoryQueryService;

    public AddressMovementHistoryResource(AddressMovementHistoryService addressMovementHistoryService, AddressMovementHistoryQueryService addressMovementHistoryQueryService) {
        this.addressMovementHistoryService = addressMovementHistoryService;
        this.addressMovementHistoryQueryService = addressMovementHistoryQueryService;
    }

    @GetMapping("/aur-address-placement-history")
    public List<AddressMovementHistory> getHistoryList() {
        return addressMovementHistoryService.getAllPlacementHistory();
    }

    @PostMapping("/aur-address-placement-history")
    public AddressMovementHistory saveAddressPlacementHistory(@RequestBody AddressPlacementDto addressPlacementDto)  {
        log.debug("REST request to save address placement history by addressPlacementDto {}",addressPlacementDto);
        return addressMovementHistoryService.saveAddressPlacementFromTmp(addressPlacementDto);
    }

    /**
     * {@code GET  /address-movement-history} : get all the address-movement-history.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of address-movement-history in body.
     */
    @GetMapping("/address-movement-history")
    public ResponseEntity<List<AddressMovementHistory>> getAllAddressMovementHistory(AddressMovementHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AddressMovement by criteria: {}", criteria);
        Page<AddressMovementHistory> page = addressMovementHistoryQueryService.findByCriteria(criteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /address-movement-history/count} : count all the address-movement-history.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/address-movement-history/count")
    public ResponseEntity<Long> countAddressMovementHistory(AddressMovementHistoryCriteria criteria) {
        log.debug("REST request to count AddressMovementHistory by criteria: {}", criteria);
        return ResponseEntity.ok().body(addressMovementHistoryQueryService.countByCriteria(criteria));
    }
}
