package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.ProcessTree;
import com.hisarresearch.wms.service.ProcessTreeQueryService;
import com.hisarresearch.wms.service.ProcessTreeService;
import com.hisarresearch.wms.service.criteria.ProcessTreeCriteria;
import com.hisarresearch.wms.service.dto.process.ProcessTreeCreateDto;
import com.hisarresearch.wms.service.dto.process.ProcessTreeDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProcessTreeResource {

    private final Logger log = LoggerFactory.getLogger(ProcessTreeResource.class);
    private static final String ENTITY_NAME = "process-tree";

    private final ProcessTreeService processTreeService;
    private final ProcessTreeQueryService processTreeQueryService;

    public ProcessTreeResource(ProcessTreeService processTreeService, ProcessTreeQueryService processTreeQueryService) {
        this.processTreeService = processTreeService;
        this.processTreeQueryService = processTreeQueryService;
    }

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code GET  /process-tree} : get all the process trees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processTress in body.
     */
    @GetMapping("/process-tree")
    public ResponseEntity<List<ProcessTree>> getAllProcessTrees(
        ProcessTreeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ProcessTress by criteria: {}", criteria);
        Page<ProcessTree> page = processTreeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/process-tree")
    public ResponseEntity<ProcessTreeDto> saveProcessTree(@RequestBody ProcessTreeDto processTreeDTO) throws URISyntaxException {
        log.debug("REST request to save process tree {}",processTreeDTO);

        if(processTreeDTO.getId() != null){
            throw new BadRequestAlertException("A new process cannot already have an ID", "process-tree", "idexists");
        }
        else{
            ProcessTreeDto savedProcess = processTreeService.createProcessTree(processTreeDTO);
            return ResponseEntity
                .created(new URI("/process-created"))
                .headers(HeaderUtil.createAlert(applicationName, "processTree.created", processTreeDTO.toString()))
                .body(savedProcess);
        }
    }

    @PatchMapping(value = "/process-trees/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProcessTree> partialUpdateProcessTree(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessTreeCreateDto processTreeCreateDto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Process Tree partially : {}, {}", id, processTreeCreateDto);
        if (processTreeCreateDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processTreeCreateDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processTreeService.findById(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessTree> result = processTreeService.partialUpdate(processTreeCreateDto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processTreeCreateDto.getId().toString())
        );
    }

    @GetMapping("/complete-process/{processTreeId}")
    public ResponseEntity completeProcess(@PathVariable Long processTreeId) throws CloneNotSupportedException {
        processTreeService.completeProcessTree(processTreeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-process-with-leaf/{processChildId}/{depoCode}")
    public ProcessTreeDto findOneByProcessChildIdAndDepoCde(@PathVariable Long processChildId, @PathVariable Long depoCode){
        return processTreeService.findByProcessChildId(processChildId,depoCode);
    }

}
