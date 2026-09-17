package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.MetabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Transactional
public class MetabaseResource {
    private final Logger log = LoggerFactory.getLogger(MetabaseResource.class);

    private static final String ENTITY_NAME = "metabase";

    private final MetabaseService metabaseService;

    public MetabaseResource(MetabaseService metabaseService) {
        this.metabaseService = metabaseService;
    }

    @GetMapping("/generate-token/{warehouseName}")
    public ResponseEntity<String> generateToken(@PathVariable String warehouseName) {
        log.debug("REST request to generate token for warehouse: {}", warehouseName);
        return ResponseEntity.ok().body(metabaseService.generateToken(warehouseName));
    }

    @GetMapping("/generate-metabase-url")
    public ResponseEntity<String> generateMetabaseUrl(@RequestParam Map<String, String> params) {
        log.debug("REST request to generate token for warehouse: {}", params);
        return ResponseEntity.ok().body(metabaseService.generateMatabaseUrl(params));
    }

}
