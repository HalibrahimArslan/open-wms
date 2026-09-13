package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.service.ProductQueryService;
import com.hisarresearch.wms.service.ProductService;
import com.hisarresearch.wms.service.criteria.ProductCriteria;
import com.hisarresearch.wms.service.dto.product.ProductWithAmountDTO;
import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class ProductResource {
    private static final String ENTITY_NAME = "product";

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductQueryService productQueryService;

    private final ProductService productService;

    public ProductResource(ProductQueryService productQueryService,
                           ProductService productService) {
        this.productQueryService = productQueryService;
        this.productService = productService;
    }

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getProductListByCriteria(ProductCriteria productCriteria, Pageable pageable)  {
        log.debug("REST request to get product by query");
        Page<Product> page = productQueryService.findByCriteria(productCriteria,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/product/count")
    public ResponseEntity<Long> getErpStockInfoCount(ProductCriteria productCriteria){
        log.debug("REST request to get product count");
        return ResponseEntity.ok().body(productQueryService.countByCriteria(productCriteria));
    }

    @PostMapping("/product")
    public ResponseEntity<ProductWithoutAddressDTO> createProduct(@Valid @RequestBody ProductWithoutAddressDTO dto) {
        log.debug("REST request to save Product : {}", dto);
        return ResponseEntity.ok(productService.create(dto));
    }

    @PatchMapping(value = "/product", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductWithoutAddressDTO> partialUpdateProduct(@RequestBody ProductWithoutAddressDTO dto) {
        log.debug("REST request to partial update Product : {}", dto);
        return ResponseEntity.ok(productService.partialUpdate(dto));
    }

    @GetMapping("/product-via-amount")
    public ResponseEntity<List<ProductWithAmountDTO>> getProductDetailListByCriteria(ProductCriteria productCriteria, Pageable pageable) throws Exception {
        log.debug("REST request to get product details with amount of erp source by query");
        List<ProductWithAmountDTO> productDTOList = productService.getProductsWithAmount(productCriteria,pageable);

        return ResponseEntity.ok().body(productDTOList);
    }



}
