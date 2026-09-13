package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurVwZReport;
import com.hisarresearch.wms.service.ZReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZReportResource {
    private final Logger log = LoggerFactory.getLogger(ZReportResource.class);

    private static final String ENTITY_NAME = "zReport";

    private final ZReportService zReportService;

    public ZReportResource(ZReportService zReportService) {
        this.zReportService = zReportService;
    }

    @GetMapping("/zreport/{previousDay}")
    public List<AurVwZReport> dailyZReport(@PathVariable int previousDay){
        log.debug("REST request to get ZReport for previous day: {}", previousDay);
        return zReportService.dailyZReport(previousDay);
    }

}
