package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.AurGrupMailAdres;
import com.hisarresearch.wms.service.AurGroupMailAddressService;
import com.hisarresearch.wms.service.dto.AurGrupMailAdresDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupMailAddressResource {

    private final Logger log = LoggerFactory.getLogger(GroupMailAddressResource.class);


    private final AurGroupMailAddressService aurGroupMailAddressService;

    public GroupMailAddressResource(AurGroupMailAddressService aurGroupMailAddressService) {
        this.aurGroupMailAddressService = aurGroupMailAddressService;
    }

    @GetMapping("/group-mail-address")
    public List<AurGrupMailAdres> getAllGroupMail() throws Exception {
        return aurGroupMailAddressService.getAllGrupMailAdres();
    }
    @PostMapping("/group-mail-address")
    public AurGrupMailAdres saveGroupMailAddress(@RequestBody @Valid AurGrupMailAdresDto aurGrupMailAdresDto) {
        log.debug("Save one GroupMail, controller is running", aurGrupMailAdresDto);
        return aurGroupMailAddressService.saveGrupMailAdres(aurGrupMailAdresDto);
    }
    @PutMapping("/update-group-mail-address")
    public AurGrupMailAdres updateGroupMailAddress(@RequestBody @Valid AurGrupMailAdresDto aurGrupMailAdresDto) {
        log.debug("Update one Mail, controller is running", aurGrupMailAdresDto);
        if(aurGrupMailAdresDto.getId() == null) {
            throw new BadRequestAlertException("Güncellenecek bir Mail bulunamadi","AurGrupMailAdres","update");
        }

        return aurGroupMailAddressService.updateGroupMailAdres(aurGrupMailAdresDto);

    }

    @DeleteMapping("/group-mail-address/{id}")
    public ResponseEntity<Void> deleteGroupMail(@PathVariable Long id){
        log.debug("Delete one GroupMail, controller is running", id);
        aurGroupMailAddressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
