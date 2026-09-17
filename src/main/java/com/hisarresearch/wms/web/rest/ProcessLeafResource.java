package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.ProcessLeaf;
import com.hisarresearch.wms.service.ProcessLeafService;
import com.hisarresearch.wms.service.dto.process.ProcessLeafCreateDto;
import com.hisarresearch.wms.service.dto.process.ProcessLeafDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProcessLeafResource {
    private final Logger log = LoggerFactory.getLogger(ProcessLeafResource.class);

    private static final String ENTITY_NAME = "process-leaf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessLeafService processLeafService;

    public ProcessLeafResource(ProcessLeafService processLeafService) {
        this.processLeafService = processLeafService;
    }

    @GetMapping("/process-leaves")
    public ResponseEntity<List<ProcessLeaf>> getAllProcessLeaves() {
        log.debug("REST request to get all process leaf");
        return ResponseEntity.ok().body(processLeafService.getAllProcessLeaves());
    }

    @GetMapping("/process-leaf/{id}")
    public ResponseEntity<ProcessLeafDto> getProcessLeafById(@PathVariable Long id) {
        log.debug("REST request to get one process leaf by id");
        return ResponseEntity.ok().body(processLeafService.getOneById(id));
    }

    @PostMapping("/process-leaf")
    public ResponseEntity<ProcessLeaf> saveProcessLeaf(@RequestBody ProcessLeafCreateDto processLeafCreateDtO) throws URISyntaxException {
        log.debug("REST request to save process leaf {}", processLeafCreateDtO);

        if(processLeafCreateDtO.getId() != null){
            throw new BadRequestAlertException("A new process cannot already have an ID", "process-leaf", "idexists");
        }
        else{
            ProcessLeaf savedProcess = processLeafService.createProcessLeaf(processLeafCreateDtO);
            return ResponseEntity
                .created(new URI("/process-leaf-created"))
                .headers(HeaderUtil.createAlert(applicationName, "processTree.created", processLeafCreateDtO.toString()))
                .body(savedProcess);
        }
    }

    @PatchMapping(value = "/process-leaf/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProcessLeaf> partialUpdateProcessTree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessLeafCreateDto processLeafCreateDtO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Process Tree partially : {}, {}", id, processLeafCreateDtO);
        if (processLeafCreateDtO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processLeafCreateDtO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processLeafService.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessLeaf> result = processLeafService.partialUpdate(processLeafCreateDtO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processLeafCreateDtO.getId().toString())
        );
    }

}
