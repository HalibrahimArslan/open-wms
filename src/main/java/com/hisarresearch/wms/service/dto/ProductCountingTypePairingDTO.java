package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;


public class ProductCountingTypePairingDTO {
    private Long id;

    private String countingType;

    private int warehouseCode;

    private ProductWithoutAddressDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountingType() {
        return countingType;
    }

    public void setCountingType(String countingType) {
        this.countingType = countingType;
    }

    public int getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(int warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public ProductWithoutAddressDTO getProduct() {
        return product;
    }

    public void setProduct(ProductWithoutAddressDTO product) {
        this.product = product;
    }
}
