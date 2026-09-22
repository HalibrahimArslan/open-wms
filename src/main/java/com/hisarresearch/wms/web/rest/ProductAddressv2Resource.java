package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.repository.ProductAddressv2Repository;
import com.hisarresearch.wms.service.ProductAddressQueryService;
import com.hisarresearch.wms.service.ProductAddressService;
import com.hisarresearch.wms.service.criteria.ProductAddressCriteria;
import com.hisarresearch.wms.service.dto.address.AurDepoStockCodeDto;
import com.hisarresearch.wms.service.dto.address.AurDepoStokUrunAdresDto;
import com.hisarresearch.wms.exception.validation.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductAddressv2Resource {
    private static final String ENTITY_NAME = "productAddressv2";
    private final Logger log = LoggerFactory.getLogger(ProductAddressv2Resource.class);

    private final ProductAddressService productAddressService;
    private final ProductAddressQueryService productAddressQueryService;
    private final ProductAddressv2Repository productAddressv2Repository;

    public ProductAddressv2Resource(ProductAddressService productAddressService, ProductAddressQueryService productAddressQueryService,
                                    ProductAddressv2Repository productAddressv2Repository) {
        this.productAddressService = productAddressService;
        this.productAddressQueryService = productAddressQueryService;
        this.productAddressv2Repository = productAddressv2Repository;
    }

    @GetMapping("/v2/product-addresses")
    public ResponseEntity<List<ProductAddressv2>> getProductAddresses(ProductAddressCriteria productAddressCriteria, Pageable pageable) throws Exception {
        log.debug("Rest request to get product addresses by criteria {}", productAddressCriteria);
        return ResponseEntity.ok().body(productAddressService.findAll(productAddressCriteria, pageable));
    }

    @GetMapping("/v2/product-addresses/count")
    public ResponseEntity<Long> getProductAddressesCount(ProductAddressCriteria productAddressCriteria) {
        log.debug("Rest request to get product addresses");
        return ResponseEntity.ok().body(productAddressQueryService.countByCriteria(productAddressCriteria));
    }

    @PostMapping("/v2/product-address")
    public ResponseEntity<ProductAddressv2> createProductAddress(@Valid @RequestBody ProductAddressv2 productAddressv2) throws Exception {
        log.debug("Rest request to create a product address");
        if (productAddressv2.getId() != null) {
            throw new BadRequestException("A new productAddressv2 cannot already have an ID");
        }

        ProductAddressv2 result = productAddressv2Repository.save(productAddressv2);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/dispatchment-addresses")
    public ResponseEntity<List<AurDepoStockCodeDto>> getAddressByStockCodeCamlica(@RequestBody AurDepoStokUrunAdresDto aurDepoStokUrunAdresDto) {
        log.debug("Get address information by stock code to pick product from address");
        return ResponseEntity.ok().body(productAddressService.getProductAddressForPicking(aurDepoStokUrunAdresDto));
    }


}
