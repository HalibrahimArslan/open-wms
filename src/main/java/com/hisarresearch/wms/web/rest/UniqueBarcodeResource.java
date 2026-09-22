package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.UniqueBarcodeQueryService;
import com.hisarresearch.wms.service.barcode.UniqueBarcodeService;
import com.hisarresearch.wms.service.criteria.UniqueBarcodeCriteria;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeCreateDTO;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UniqueBarcodeResource {

    private final Logger log = LoggerFactory.getLogger(UniqueBarcodeResource.class);


    private final UniqueBarcodeService service;
    private final UniqueBarcodeQueryService uniqueBarcodeQueryService;

    public UniqueBarcodeResource(
        UniqueBarcodeService service,
        UniqueBarcodeQueryService uniqueBarcodeQueryService)
         {
        this.service = service;
        this.uniqueBarcodeQueryService = uniqueBarcodeQueryService;
    }

    @PostMapping("/unique-barcodes")
    public List<UniqueBarcodeResponseDTO> create(@Valid @RequestBody UniqueBarcodeCreateDTO dto) {
        log.debug("REST request to save UniqueBarcode : {}", dto);
        return service.createUniqueBarcodes(dto);
    }

    @PostMapping("/unique-barcodes/bulk")
    public List<UniqueBarcodeResponseDTO> createBulk(@Valid @RequestBody List<UniqueBarcodeCreateDTO> dtoList) {
        log.debug("REST request to bulk save UniqueBarcodes, count: {}", dtoList.size());
        return service.createUniqueBarcodesBulk(dtoList);
    }

    /**
     * {@code GET  /unique-barcodes} : get all the unique barcodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uniqueBarcode in body.
     */
    @GetMapping("/unique-barcodes")
    public ResponseEntity<List<UniqueBarcodeResponseDTO>> getAllUniqueBarcodes(
        UniqueBarcodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get UniqueBarcodes by criteria: {}", criteria);
        Page<UniqueBarcodeResponseDTO> page = uniqueBarcodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
