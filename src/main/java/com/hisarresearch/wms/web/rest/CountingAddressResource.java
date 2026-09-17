package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.domain.CountingUserAddressRel;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.service.CountingAddressService;
import com.hisarresearch.wms.service.CountingUserAddressRelService;
import com.hisarresearch.wms.service.criteria.AurSayimTanimCriteria;
import com.hisarresearch.wms.service.dto.counting.CountingUserAddressRelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CountingAddressResource {
    private final Logger log = LoggerFactory.getLogger(CountingAddressResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountingAddressService countingAddressService;

    public CountingAddressResource(CountingAddressService countingAddressService) {
        this.countingAddressService = countingAddressService;
    }


    /**
     * {@code POST  /counting-address/:countingDefinitionId} : check address is countable and return id of address by countingDefinitionId
     *
     * @param aurDepoUrunAdres the address information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the id of address in body.
     */
    @PostMapping("/counting-address/{countingDefinitionId}")
    public ResponseEntity<Long> getAllAurSayimTanims(@PathVariable long countingDefinitionId,@RequestBody AurDepoUrunAdres aurDepoUrunAdres) {
        log.debug("REST request to check address is countable and get id of it by countingDefinitionId: {}", countingDefinitionId);
        return ResponseEntity.ok().body(countingAddressService.checkCountableAddress(aurDepoUrunAdres,countingDefinitionId));
    }

}
