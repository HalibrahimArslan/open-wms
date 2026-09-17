package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurOrderDetailSkt;
import com.hisarresearch.wms.service.AurOrderDetailSktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AurOrderDetailSktResource {
    private static final String ENTITY_NAME = "aurTmpDetailSkt";

    private final Logger log = LoggerFactory.getLogger(AurOrderDetailSktResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurOrderDetailSktService aurTmpDetailSktService;

    public AurOrderDetailSktResource(AurOrderDetailSktService aurTmpDetailSktService) {
        this.aurTmpDetailSktService = aurTmpDetailSktService;
    }

    @GetMapping("/aur-tmp-detail-skt-list")
    public List<AurOrderDetailSkt> getAurTmpSktList() {
        return aurTmpDetailSktService.findAll();
    }
}
