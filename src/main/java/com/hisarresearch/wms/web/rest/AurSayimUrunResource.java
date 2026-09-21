package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurSayimResult;
import com.hisarresearch.wms.domain.AurSayimUrun;
import com.hisarresearch.wms.repository.AurSayimUrunRepository;
import com.hisarresearch.wms.service.AurSayimUrunQueryService;
import com.hisarresearch.wms.service.AurSayimUrunService;
import com.hisarresearch.wms.service.criteria.AurSayimUrunCriteria;
import com.hisarresearch.wms.service.dto.ComparativeCountingResultDTO;
import com.hisarresearch.wms.service.dto.CountingReportMicroDto;
import com.hisarresearch.wms.service.dto.CountingReportDto;
import com.hisarresearch.wms.service.dto.counting.CountingDetailDTO;
import com.hisarresearch.wms.service.mapper.CountingDetailMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.undertow.util.BadRequestException;
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

/**
 * REST controller for managing {@link com.hisarresearch.wms.domain.AurSayimUrun}.
 */
@RestController
@RequestMapping("/api")
public class AurSayimUrunResource {

    private final Logger log = LoggerFactory.getLogger(AurSayimUrunResource.class);

    private static final String ENTITY_NAME = "aurSayimUrun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurSayimUrunService aurSayimUrunService;

    private final AurSayimUrunRepository aurSayimUrunRepository;

    private final AurSayimUrunQueryService aurSayimUrunQueryService;

    private final CountingDetailMapper countingDetailMapper;

    public AurSayimUrunResource(
        AurSayimUrunService aurSayimUrunService,
        AurSayimUrunRepository aurSayimUrunRepository,
        AurSayimUrunQueryService aurSayimUrunQueryService, CountingDetailMapper countingDetailMapper
    ) {
        this.aurSayimUrunService = aurSayimUrunService;
        this.aurSayimUrunRepository = aurSayimUrunRepository;
        this.aurSayimUrunQueryService = aurSayimUrunQueryService;
        this.countingDetailMapper = countingDetailMapper;
    }

    /**
     * {@code POST  /aur-sayim-uruns} : Create a new aurSayimUrun.
     *
     * @param countingDetailDTO the aurSayimUrun to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurSayimUrun, or with status {@code 400 (Bad Request)} if the aurSayimUrun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aur-sayim-uruns")
    public ResponseEntity<AurSayimUrun> createAurSayimUrun(@RequestBody CountingDetailDTO countingDetailDTO) throws URISyntaxException {
        log.debug("REST request to save AurSayimUrun : {}", countingDetailDTO);
        if (countingDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new aurSayimUrun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurSayimUrun result = aurSayimUrunService.save(countingDetailDTO);
        return ResponseEntity
            .created(new URI("/api/aur-sayim-uruns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aur-sayim-uruns/:id} : Updates an existing aurSayimUrun.
     *
     * @param id the id of the aurSayimUrun to save.
     * @param countingDetailDTO the aurSayimUrun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurSayimUrun,
     * or with status {@code 400 (Bad Request)} if the aurSayimUrun is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurSayimUrun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aur-sayim-uruns/{id}")
    public ResponseEntity<AurSayimUrun> updateAurSayimUrun(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CountingDetailDTO countingDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AurSayimUrun : {}, {}", id, countingDetailDTO);
        if (countingDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countingDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurSayimUrunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AurSayimUrun result = aurSayimUrunService.save(countingDetailDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countingDetailDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aur-sayim-uruns/:id} : Partial updates given fields of an existing aurSayimUrun, field will ignore if it is null
     *
     * @param id the id of the aurSayimUrun to save.
     * @param aurSayimUrun the aurSayimUrun to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurSayimUrun,
     * or with status {@code 400 (Bad Request)} if the aurSayimUrun is not valid,
     * or with status {@code 404 (Not Found)} if the aurSayimUrun is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurSayimUrun couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aur-sayim-uruns/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurSayimUrun> partialUpdateAurSayimUrun(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurSayimUrun aurSayimUrun
    ) throws URISyntaxException {
        log.debug("REST request to partial update AurSayimUrun partially : {}, {}", id, aurSayimUrun);
        if (aurSayimUrun.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurSayimUrun.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurSayimUrunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurSayimUrun> result = aurSayimUrunService.partialUpdate(aurSayimUrun);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurSayimUrun.getId().toString())
        );
    }

    /**
     * {@code GET  /aur-sayim-uruns} : get all the aurSayimUruns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurSayimUruns in body.
     */
    @GetMapping("/aur-sayim-uruns")
    public ResponseEntity<List<CountingDetailDTO>> getAllAurSayimUruns(AurSayimUrunCriteria criteria, Pageable pageable) throws BadRequestException {
        log.debug("REST request to get AurSayimUruns by criteria: {}", criteria);
        Page<AurSayimUrun> page = aurSayimUrunQueryService.findByCriteria(criteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(countingDetailMapper.toDto(page.getContent()));
    }

    /**
     * {@code GET  /aur-sayim-uruns/count} : count all the aurSayimUruns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/aur-sayim-uruns/count")
    public ResponseEntity<Long> countAurSayimUruns(AurSayimUrunCriteria criteria) throws BadRequestException {
        log.debug("REST request to count AurSayimUruns by criteria: {}", criteria);
        return ResponseEntity.ok().body(aurSayimUrunQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aur-sayim-uruns/:id} : get the "id" aurSayimUrun.
     *
     * @param id the id of the aurSayimUrun to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurSayimUrun, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aur-sayim-uruns/{id}")
    public ResponseEntity<AurSayimUrun> getAurSayimUrun(@PathVariable Long id) {
        log.debug("REST request to get AurSayimUrun : {}", id);
        Optional<AurSayimUrun> aurSayimUrun = aurSayimUrunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aurSayimUrun);
    }

    /**
     * {@code DELETE  /aur-sayim-uruns/:id} : delete the "id" aurSayimUrun.
     *
     * @param id the id of the aurSayimUrun to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aur-sayim-uruns/{id}")
    public ResponseEntity<Void> deleteAurSayimUrun(@PathVariable Long id) {
        log.debug("REST request to delete AurSayimUrun : {}", id);
        aurSayimUrunService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/aur-sayim-results/{sayimTanimId}")
    public ResponseEntity<List<AurSayimResult>> getAllAurSayimResult(@PathVariable  Long sayimTanimId) {
       log.debug("REST request to get sayim list by id : {} ", sayimTanimId);
        List<AurSayimResult> aurSayimResults = aurSayimUrunService.findSayimResult(sayimTanimId);

        return ResponseEntity.ok().body(aurSayimResults);
    }

    @GetMapping("/aur-sayim-address/{sayimTanimId}")
    public ResponseEntity<List<CountingDetailDTO>> getDistinctUrunAdresId(@PathVariable  Long sayimTanimId) {
        log.debug("REST request to get AurSayimUruns addresses by criteria: {}", sayimTanimId);
        List<CountingDetailDTO> aurSayimResults = aurSayimUrunService.findProcessedAddress(sayimTanimId);

        return ResponseEntity.ok().body(aurSayimResults);
    }

    @GetMapping("/aur-sayim-result-list/{countingDefinitionId}")
    public ResponseEntity<List<AurSayimUrun>> getAllList(@PathVariable  Long countingDefinitionId) {
        log.debug("REST request to get AurSayimUruns addresses by countingDefinitionId: {}", countingDefinitionId);
        List<AurSayimUrun> countingDetails = aurSayimUrunService.findByTanimId(countingDefinitionId);

        return ResponseEntity.ok().body(countingDetails);
    }


    @GetMapping("/aur-sayim-uruns-with-pallet-barcode/{aurSayimTanimId}/{palletBarcode}/{urunAdresId}")
    public ResponseEntity<Void> saveAurSayimUrunsWithPalletBarcode(@PathVariable Long aurSayimTanimId,@PathVariable  String palletBarcode,@PathVariable Long urunAdresId){
        log.debug("REST request to save AurSayimUruns inside a palletbarcode : {}", palletBarcode);
        aurSayimUrunService.saveByPalletBarcode(aurSayimTanimId,palletBarcode,urunAdresId);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, palletBarcode))
            .build();
    }

    /**
     * {@code GET  /counting-report/counting-definition-id} : get counting report by id.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counting-reports in body.
     */
    @GetMapping("/counting-report/{countingDefinitionId}")
    public ResponseEntity<List<CountingReportDto>> getCountingReport(@PathVariable Long countingDefinitionId) {
        log.debug("REST request to get counting report list by countingDefinitionId: {}", countingDefinitionId);
        return ResponseEntity.ok().body(aurSayimUrunService.getCountingReportList(countingDefinitionId));
    }


    /**
     * {@code GET  /counting-report-micro/counting-definition-id} : get counting report by id.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counting-reports in body.
     */
    @GetMapping("/counting-report-micro/{countingDefinitionId}")
    public ResponseEntity<List<CountingReportMicroDto>> getCountingReportMicro(@PathVariable Long countingDefinitionId) {
        log.debug("REST request to get counting report list by countingDefinitionId: {}", countingDefinitionId);
        return ResponseEntity.ok().body(aurSayimUrunService.getCountingReportListMicro(countingDefinitionId));
    }

    /**
     * {@code GET  /comparative-counting-report/countingId/controlCountingId} : get comparative counting report by controllingId and controlCountingId.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comparative-counting-reports in body.
     */
    @GetMapping("/comparative-counting-report/{countingId}/{controlCountingId}")
    public ResponseEntity<List<ComparativeCountingResultDTO>> getComparativeCountingReport(@PathVariable Long countingId, @PathVariable Long controlCountingId) {
        log.debug("REST request to get comparative counting report list by countingId and controlCountingId: {} {}", countingId,controlCountingId);
        return ResponseEntity.ok().body(aurSayimUrunService.getComparativeCountingReport(countingId,controlCountingId));
    }

}
