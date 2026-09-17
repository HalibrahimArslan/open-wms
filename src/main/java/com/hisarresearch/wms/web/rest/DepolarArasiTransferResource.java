package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.DepolarArasiTransferService;
import com.hisarresearch.wms.service.dto.DepolarArasiTransferDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Transactional
public class DepolarArasiTransferResource {
    private final Logger log = LoggerFactory.getLogger(DepolarArasiTransferResource.class);

    private final DepolarArasiTransferService depolarArasiTransferService;

    public DepolarArasiTransferResource(DepolarArasiTransferService depolarArasiTransferService) {
        this.depolarArasiTransferService = depolarArasiTransferService;
    }

    @PostMapping("/depolar-arasi-transfer")
    public ResponseEntity<Void> depolarArasiTransfer(@Valid @RequestBody DepolarArasiTransferDto dto) throws Exception {
        log.debug("REST request to depolar arasi transfer : {}", dto);
        depolarArasiTransferService.depolarArasiTransfer(dto);
        return ResponseEntity.ok().build();
    }

}
