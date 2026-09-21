package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.CountingUserAddressRel;
import com.hisarresearch.wms.service.CountingUserAddressRelService;
import com.hisarresearch.wms.service.dto.counting.CountingUserAddressRelDTO;
import com.hisarresearch.wms.service.dto.counting.CountingUserAddressSearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api")
public class CountingUserAddressRelResource {

    private final Logger log = LoggerFactory.getLogger(CountingUserAddressRelResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountingUserAddressRelService countingUserAddressRelService;


    public CountingUserAddressRelResource(CountingUserAddressRelService countingUserAddressRelService) {
        this.countingUserAddressRelService = countingUserAddressRelService;
    }

    @PostMapping("/search-counting-user-addresses")
    public ResponseEntity<List<CountingUserAddressRelDTO>> getAllCountingUserAddressRelations(@Valid @RequestBody CountingUserAddressSearchDTO countingUserAddressSearchDTO) {
        log.debug("REST request to get CountingUserAddressRelations");
        return ResponseEntity.ok().body(countingUserAddressRelService.getCountingUserAddressRelations(countingUserAddressSearchDTO));

    }

    @PostMapping("/counting-user-addresses")
    public ResponseEntity<List<CountingUserAddressRelDTO>> saveCountingUserAddresses(@RequestBody List<@Valid CountingUserAddressRelDTO> countingUserAddressRelDTO) throws URISyntaxException {
        log.debug("REST request to save CountingUserAddresses : {}", countingUserAddressRelDTO);
        List<CountingUserAddressRel> savedCountingAddressRelations = countingUserAddressRelService.saveCountingUserAddresses(countingUserAddressRelDTO);
        List<Long> ids = savedCountingAddressRelations.stream().map(CountingUserAddressRel::getId).collect(Collectors.toList());
        return ResponseEntity.created(new URI("/api/counting-user-addresses")).body(countingUserAddressRelService.findAllByIdIn(ids));
    }

    @DeleteMapping("/counting-user-addresses")
    public ResponseEntity<Void> deleteCountingUserAddresses(@RequestBody List<CountingUserAddressRelDTO> countingUserAddressRelDTOS){
        log.debug("REST request to delete CountingUserAddresses : {}", countingUserAddressRelDTOS);
        countingUserAddressRelService.deleteCountingUserAddressRel(countingUserAddressRelDTOS);
        return ResponseEntity.noContent().build();
    }
}
