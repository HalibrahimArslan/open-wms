package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.sevkplani.AurTmpSevkPlani;
import com.hisarresearch.wms.service.AurTmpSevkPlaniService;
import com.hisarresearch.wms.service.dto.AurTmpSevkPlaniDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AurTmpSevkPlaniResource {

    private final AurTmpSevkPlaniService service;

    public AurTmpSevkPlaniResource(AurTmpSevkPlaniService service) {
        this.service = service;
    }
    @PostMapping("/sevk-plani")
    public void sevkSave (@RequestBody @Valid AurTmpSevkPlaniDTO plan) {
        service.save(plan);
    }
    @PostMapping("/sevk-plani-all")
    public void sevkAllSave (@RequestBody @Valid List<AurTmpSevkPlaniDTO> plan) {
        service.saveAll(plan);
    }

    @GetMapping("/sevk-plani/{id}")
    public ResponseEntity<AurTmpSevkPlaniDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/sevk-plani")
    public ResponseEntity<List<AurTmpSevkPlani>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}

