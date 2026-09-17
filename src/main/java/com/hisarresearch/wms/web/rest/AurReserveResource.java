package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurReserve;
import com.hisarresearch.wms.service.AurReserveService;
import com.hisarresearch.wms.service.dto.AurReserveDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AurReserveResource {

    private final Logger log = LoggerFactory.getLogger(AurReserveResource.class);

    private final AurReserveService aurReserveService;

    public AurReserveResource(AurReserveService aurReserveService) {
        this.aurReserveService = aurReserveService;
    }

    @GetMapping("/aur-reserves")
    public ResponseEntity<List<AurReserveDTO>> getAllAurReserves(@RequestParam String companyCode) {
        log.debug("REST request to get all AurReserves");
        List<AurReserveDTO> reserveDTOS = aurReserveService.getAurReserves(companyCode);
        return ResponseEntity.ok().body(reserveDTOS);
    }

    @PostMapping("/aur-reserves")
    public ResponseEntity<List<AurReserveDTO>> saveAurReserves(@RequestBody List<AurReserveDTO> aurReserveDTOs) throws URISyntaxException {
        log.debug("REST request to save AurReserve : {}", aurReserveDTOs);
        List<AurReserveDTO> savedAurReserves = aurReserveService.saveAurReserves(aurReserveDTOs);
        return ResponseEntity.created(new URI("/api/aur-reserves/" + savedAurReserves.get(0).getId())).body(savedAurReserves);
    }

    @PutMapping("/aur-reserve")
    public ResponseEntity<AurReserve> updateAurReserve(@Valid @RequestBody AurReserveDTO aurReserveDTO) {
        log.debug("REST request to update AurReserve : {}", aurReserveDTO);
        if(aurReserveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", null, "idnull");
        }
        Optional<AurReserve> updatedAurReserve = aurReserveService.updateAurReserve(aurReserveDTO);
        return ResponseUtil.wrapOrNotFound(updatedAurReserve);

    }
}
