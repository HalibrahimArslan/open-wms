package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.ProductCountingTypePairing;
import com.hisarresearch.wms.service.ProductCountingTypePairingQueryService;
import com.hisarresearch.wms.service.ProductCountingTypePairingService;
import com.hisarresearch.wms.service.criteria.ProductCountingTypePairingCriteria;
import com.hisarresearch.wms.service.dto.ProductCountingTypePairingBulkDTO;
import com.hisarresearch.wms.service.dto.ProductCountingTypePairingDTO;
import com.hisarresearch.wms.service.mapper.ProductCountingTypePairingMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductCountingTypePairingResource {

    private static final String ENTITY_NAME = "productCountingTypePairing";

    private final Logger log = LoggerFactory.getLogger(ProductCountingTypePairingResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductCountingTypePairingService productCountingTypePairingService;

    private final ProductCountingTypePairingQueryService productCountingTypePairingQueryService;

    private final ProductCountingTypePairingMapper productCountingTypePairingMapper;

    public ProductCountingTypePairingResource(ProductCountingTypePairingService productCountingTypePairingService,
                                              ProductCountingTypePairingQueryService productCountingTypePairingQueryService,
                                              ProductCountingTypePairingMapper productCountingTypePairingMapper) {
        this.productCountingTypePairingService = productCountingTypePairingService;
        this.productCountingTypePairingQueryService = productCountingTypePairingQueryService;
        this.productCountingTypePairingMapper = productCountingTypePairingMapper;
    }


    /**
     * {@code GET  /product-counting-type-pairings} : get all the productCountingTypePairings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCountingTypePairing in body.
     */
    @GetMapping("/product-counting-type-pairings")
    public ResponseEntity<List<ProductCountingTypePairingDTO>> getAllProductCountingTypePairings(ProductCountingTypePairingCriteria productCountingTypePairingCriteria) {
        log.debug("REST request to get ProductCountingTypePairings");
        List<ProductCountingTypePairing> entityList = productCountingTypePairingQueryService.findByCriteria(productCountingTypePairingCriteria);
        return ResponseEntity.ok().body(productCountingTypePairingMapper.toDto(entityList));
    }

    /**
     * {@code GET  /product-counting-type-pairings/count} : count all the productCountingTypePairings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-counting-type-pairings/count")
    public ResponseEntity<Long> countProductCountingTypePairings(ProductCountingTypePairingCriteria criteria) {
        log.debug("REST request to count ProductCountingTypePairings by criteria: {}", criteria);
        return ResponseEntity.ok().body(productCountingTypePairingQueryService.countByCriteria(criteria));
    }

    @PostMapping("/product-counting-type-pairing")
    public ResponseEntity<ProductCountingTypePairingDTO> createProductCountingTypePairing(@RequestBody ProductCountingTypePairingDTO productCountingTypePairing) {
        log.debug("REST request to create ProductCountingTypePairing : {}", productCountingTypePairing);
        if (productCountingTypePairing.getId() != null) {
            throw new BadRequestAlertException("A new productCountingTypePairing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductCountingTypePairingDTO createdOne = productCountingTypePairingService.createProductCountingTypePairing(productCountingTypePairing);
        return ResponseEntity.ok().body(createdOne);

    }

    @PostMapping("/product-counting-type-pairing-bulk")
    public ResponseEntity<List<ProductCountingTypePairingDTO>> createBulkProductCountingTypePairing(@RequestBody ProductCountingTypePairingBulkDTO productCountingTypePairingBulkDTO) {
        log.debug("REST request to create bulk ProductCountingTypePairingBulkDTO : {}", productCountingTypePairingBulkDTO);
        if (productCountingTypePairingBulkDTO.getId() != null) {
            throw new BadRequestAlertException("A new productCountingTypePairing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        List<ProductCountingTypePairingDTO> createdList = productCountingTypePairingService.createBulkProductCountingTypePairing(productCountingTypePairingBulkDTO);
        return ResponseEntity.ok().body(createdList);

    }

    @DeleteMapping("/product-counting-type-pairing/{id}")
    public ResponseEntity<Void> deleteProductCountingTypePairing(@PathVariable Long id) {
        log.debug("REST request to delete ProductCountingTypePairing : {}", id);
        productCountingTypePairingService.deleteOne(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "productCountingTypePairing.deleted", id.toString())).build();

    }


}
