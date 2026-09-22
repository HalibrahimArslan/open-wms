package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.barcode.PalletBarcode;
import com.hisarresearch.wms.domain.enumeration.PalletBarcodeStatus;
import com.hisarresearch.wms.service.PalletBarcodeService;
import com.hisarresearch.wms.service.dto.barcode.PalletBarcodeResponseDto;
import com.hisarresearch.wms.service.dto.barcode.PalletBarcodeWithDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PalletBarcodeResource {
    private final Logger log = LoggerFactory.getLogger(PalletBarcodeResource.class);

    private static final String ENTITY_NAME = "palletBarcode";

    private final PalletBarcodeService palletBarcodeService;

    public PalletBarcodeResource(PalletBarcodeService palletBarcodeService){
        this.palletBarcodeService = palletBarcodeService;
    }

    @GetMapping ("/generate-pallet-barcode/{quantity}")
    ResponseEntity<List<String>> generatePalletBarcode(@PathVariable int quantity){
        log.debug("REST request to generate pallet barcode with amount of : {}", quantity);
        return ResponseEntity.ok(palletBarcodeService.createPalletBarcode(quantity));

    }

    @GetMapping ("/pallet-barcode-list/{palletBarcodeStatus}")
    ResponseEntity<List<PalletBarcodeResponseDto>> getPalletBarcodeListByStatus(@PathVariable @Valid PalletBarcodeStatus palletBarcodeStatus){
        log.debug("REST request to get list of pallet barcode wrt status : {}", palletBarcodeStatus);
        return ResponseEntity.ok(palletBarcodeService.getEnablePalletBarcodeList(palletBarcodeStatus));

    }

    @GetMapping("/pallet-list")
    ResponseEntity<List<PalletBarcodeWithDetailDTO>> getPalletBarcodes(){
        log.debug("REST request to get all pallet barcode list");
        return ResponseEntity.ok().body(palletBarcodeService.findPrintableList());
    }
}
