package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.ProductAddressQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductAddressQueueResource {

    @Autowired
    private ProductAddressQueueService productAddressService;
//
//    @PutMapping("/product-address-queue/{barcode}/{addressId}/{depoCode}/amount")
//    public String updateProductAmount(@PathVariable String barcode, @PathVariable Long addressId, @PathVariable String depoCode, @RequestParam int amount) {
//        return productAddressService.updateProductAmount(barcode, addressId, amount,depoCode);
//    }
//
//    @GetMapping("/result/{requestId}")
//    public String getUpdateResult(@PathVariable String requestId) {
//        return productAddressService.getUpdateResult(requestId);
//    }
}
