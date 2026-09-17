package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.PalletBarcodeOrderRelService;
import com.hisarresearch.wms.service.dto.barcode.PalletBarcodeRequestDto;
import com.hisarresearch.wms.service.dto.barcode.PalletBarcodeSaveDto;
import com.hisarresearch.wms.service.dto.barcode.PalletInfoDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PalletBarcodeOrderRelResource {
    private final Logger log = LoggerFactory.getLogger(PalletBarcodeOrderRelResource.class);

    private static final String ENTITY_NAME = "palletBarcodeOrderRel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PalletBarcodeOrderRelService palletBarcodeOrderRelService;

    public PalletBarcodeOrderRelResource(PalletBarcodeOrderRelService palletBarcodeOrderRelService){
        this.palletBarcodeOrderRelService = palletBarcodeOrderRelService;
    }

    @PostMapping("/generate-order-pallet-barcode-rel")
    public ResponseEntity<PalletBarcodeSaveDto> generateOrderPalletBarcodeRel(@RequestBody PalletBarcodeRequestDto palletBarcodeRequestDto)throws URISyntaxException {
        log.debug("Generate pallet barcode for selected stock codes wrt order {}",palletBarcodeRequestDto);

        if(palletBarcodeRequestDto.getPalletBarcodeList().isEmpty()){
            throw new BadRequestAlertException("Empty stock code list",ENTITY_NAME,"invalidSize");
        }

        PalletBarcodeSaveDto response = palletBarcodeOrderRelService.savePalletBarcode(palletBarcodeRequestDto);
        return ResponseEntity.created(new URI("/api/generate-order-pallet-barcode-rel"))
            .headers(HeaderUtil.createAlert(applicationName, "palletBarcode.created", palletBarcodeRequestDto.getAurOrderId()))
            .body(response);

    }

    @GetMapping("/pallet-barcodes/{orderInfo}")
    public ResponseEntity<List<PalletBarcodeSaveDto>> getPalletBarcodeListByOrderInfo(@PathVariable String orderInfo){
        log.debug("Get all pallet barcodes wrt {}",orderInfo);
        return ResponseEntity.ok().body(palletBarcodeOrderRelService.getPalletBarcodeList(orderInfo));

    }


    @GetMapping("/pallet-barcodes-detail/{palletBarcode}")
    public ResponseEntity<List<PalletBarcodeSaveDto>> getPalletBarcodeDetail(@PathVariable String palletBarcode){
        log.debug("Get pallet barcode detail list");
        return ResponseEntity.ok().body(palletBarcodeOrderRelService.getPalletDetail(palletBarcode));
    }

    @DeleteMapping("/pallet-barcodes/{palletBarcodeId}")
    public ResponseEntity<Void> deletePallet(@PathVariable Long palletBarcodeId){
        log.debug("Delete pallet barcode by palletBarcode {}",palletBarcodeId);
        palletBarcodeOrderRelService.deletePalletBarcode(palletBarcodeId);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/pallet-barcodes-detail")
    public ResponseEntity<Void> deletePalletDetail(@RequestBody List<Long> palletBarcodeOrderRelIds){
        log.debug("Delete pallet barcode detail by palletBarcodeOrderRelId {}",palletBarcodeOrderRelIds);
        palletBarcodeOrderRelService.deletePalletBarcodeDetail(palletBarcodeOrderRelIds);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/pallet-barcodes-order-detail/{palletBarcode}")
    public ResponseEntity<List<PalletInfoDTO>> getPalletBarcodeOrderDetail(@PathVariable String palletBarcode){
        log.debug("Get pallet barcode detail list");
        return ResponseEntity.ok().body(palletBarcodeOrderRelService.getPalletInfo(palletBarcode));
    }


}
