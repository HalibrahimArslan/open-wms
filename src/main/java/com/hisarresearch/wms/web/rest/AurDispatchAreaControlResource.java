package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.AurDispatchAreaControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@Transactional
public class AurDispatchAreaControlResource {
    private static final String ENTITY_NAME = "aurDispatchAreaControl";

    private final Logger log = LoggerFactory.getLogger(AurDispatchAreaControlResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    private final AurDispatchAreaControlService aurDispatchAreaControlService;

    public AurDispatchAreaControlResource(AurDispatchAreaControlService aurDispatchAreaControlService) {
        this.aurDispatchAreaControlService = aurDispatchAreaControlService;
    }

    @GetMapping("/dispatch-area-list/{orderId}")
    public ResponseEntity<Collection<String>> getDispatchAreaList(@PathVariable Long orderId) throws Exception {
        log.debug("REST request to get dispatch area list by orderId : {}", orderId);
        return ResponseEntity.ok().body(aurDispatchAreaControlService.getDispatchListByOrderId(orderId));
    }

    @GetMapping("/save-order-list-by-pallet-barcode/{palletBarcode}")
    public ResponseEntity<Void> saveDispatchAreaList(@PathVariable String palletBarcode)  {
        log.debug("REST request to save dispatch area list by palletBarcode : {}", palletBarcode);
        aurDispatchAreaControlService.saveByPalletBarcode(palletBarcode);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "saveDispatchList", ENTITY_NAME)).build();
    }

}
