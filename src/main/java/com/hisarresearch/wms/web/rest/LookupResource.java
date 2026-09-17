package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.service.AurLookupService;
import com.hisarresearch.wms.service.dto.LookupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class LookupResource {
    private final Logger log = LoggerFactory.getLogger(LookupResource.class);

    private final AurLookupService aurLookupService;

    public LookupResource(AurLookupService aurLookupService) {
        this.aurLookupService = aurLookupService;
    }

    @GetMapping("/lookup/{lookupCode}")
    public ResponseEntity<List<AurLookupTable>> getByLookupCode(@PathVariable String lookupCode){
        log.debug("REST request to get Lookup detail by lookupCode : {}", lookupCode);
        return ResponseEntity.ok().body(aurLookupService.getByLookupCode(lookupCode));

    }

    @GetMapping("/lookup-name/{lookupName}")
    public ResponseEntity<List<AurLookupTable>> getByLookupName(@PathVariable String lookupName){
        log.debug("REST request to get Lookup detail by lookupName : {}", lookupName);
        return ResponseEntity.ok().body(aurLookupService.getByLookupName(lookupName));

    }

    @PostMapping("/lookups")
    public ResponseEntity<List<AurLookupTable>> getAllByLookupNames(@RequestBody List<String> lookupNames){
        log.debug("REST request to get Lookup detail by lookupNames : {}", lookupNames);
        return ResponseEntity.ok().body(aurLookupService.getByLookupNames(lookupNames));

    }


    @PostMapping("/lookup")
    public ResponseEntity<AurLookupTable> createOLookup(@Valid @RequestBody LookupDTO saveDTO) throws URISyntaxException {
        log.debug("REST request to save or update Lookup : {}", saveDTO);
        AurLookupTable savedOne = aurLookupService.createOrUpdateLookup(saveDTO);
        return ResponseEntity
            .created(new URI("/api/lookup" + saveDTO.getLookupName()))
            .body(savedOne);
    }

    /**
     * {@code DELETE /lookup/:id} : delete the lookup
     * @param id the id of the lookup
     * @return {ResponseEntity} with status {@code 204 (NO_CONTENT)}
     */
    @DeleteMapping("/lookup/{id}")
    public ResponseEntity<Void> deleteLookUpCode(@PathVariable Long id){
        log.debug("Rest request to delete lookup by id {}",id);
        aurLookupService.deleteLookupCode(id);
        return ResponseEntity.noContent().build();
    }
}
