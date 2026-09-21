package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurIntegrationLogs;
import com.hisarresearch.wms.service.AurLogQueryService;
import com.hisarresearch.wms.service.criteria.AurLogCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class AurLogResource {
    private final Logger log = LoggerFactory.getLogger(AurLogResource.class);

    private final AurLogQueryService aurLogQueryService;

    public AurLogResource(AurLogQueryService aurLogQueryService) {
        this.aurLogQueryService = aurLogQueryService;
    }

    /**
     * {@code GET  /integration-logs} : get all the aur-integration-logs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aur-integration-logs in body.
     */
    @GetMapping("/integration-logs")
    public ResponseEntity<List<AurIntegrationLogs>> getIntegrationLogs(AurLogCriteria criteria, Pageable pageable) {
        log.debug("REST request to get integration logs by criteria: {}", criteria);
        Page<AurIntegrationLogs> page = aurLogQueryService.findByCriteria(criteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /integration-logs/count} : count all the aur-integration-logs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/integration-logs/count")
    public ResponseEntity<Long> countIntegrationLogs(AurLogCriteria criteria) {
        log.debug("REST request to count integration logs by criteria: {}", criteria);
        return ResponseEntity.ok().body(aurLogQueryService.countByCriteria(criteria));
    }

}
