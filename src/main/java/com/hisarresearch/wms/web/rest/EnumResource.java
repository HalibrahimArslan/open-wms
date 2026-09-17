package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.enumeration.AddressMovementType;
import com.hisarresearch.wms.service.EnumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EnumResource {
    private final Logger log = LoggerFactory.getLogger(EnumResource.class);

    private final EnumService enumService;

    public EnumResource(EnumService enumService) {
        this.enumService = enumService;
    }

    /**
     * {@code GET  /address-movement-types}  : Get address movement type list.
     * @return the {@link ResponseEntity} with status {@code 200 (Success)} and with body the address movement type list.
     */
    @GetMapping("address-movement-type")
    public ResponseEntity<List<AddressMovementType>> getAddressMovementTypes(){
        log.debug("Rest request to get address movement types");
        return ResponseEntity.ok().body(enumService.getAddressMovementTypes());
    }


}
