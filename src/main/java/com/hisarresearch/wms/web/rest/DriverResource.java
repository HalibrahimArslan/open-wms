package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurDriver;
import com.hisarresearch.wms.service.AurDriverQueryService;
import com.hisarresearch.wms.service.DriverService;
import com.hisarresearch.wms.service.criteria.AurDriverCriteria;
import com.hisarresearch.wms.service.dto.AurDriverDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Transactional
public class DriverResource {

    private static final String ENTITY_NAME = "driver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(DriverResource.class);

    private final DriverService driverService;

    private final AurDriverQueryService aurDriverQueryService;

    public DriverResource(DriverService driverService, AurDriverQueryService aurDriverQueryService) {
        this.driverService = driverService;
        this.aurDriverQueryService = aurDriverQueryService;
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<AurDriver>> getAllDrivers(AurDriverCriteria aurDriverCriteria, Pageable pageable) throws BadRequestException {
        Page<AurDriver> page = aurDriverQueryService.findByCriteria(aurDriverCriteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/driver")
    public ResponseEntity<AurDriverDTO> createDriver(@Valid @RequestBody AurDriverDTO aurDriverDTO) {
        return ResponseEntity.ok().body(driverService.createDriver(aurDriverDTO));
    }

    @PutMapping("/driver/{id}")
    public ResponseEntity<AurDriverDTO> updateDriver(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody AurDriverDTO aurDriverDTO) {
        log.debug("REST request to update AurUser : {}, {}", id, aurDriverDTO);
        if (aurDriverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurDriverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurDriverDTO.getId().toString()))
            .body(driverService.updateDriver(aurDriverDTO));
    }

    @DeleteMapping("/driver/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        log.debug("REST request to delete AurUser : {}", id);
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

}
