package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;

import java.util.List;

public class ProductCountingTypePairingBulkDTO {
    private Long id;

    private String countingType;

    private int warehouseCode;

    private List<ProductWithoutAddressDTO> productList;

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

    public List<ProductWithoutAddressDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductWithoutAddressDTO> productList) {
        this.productList = productList;
    }
}
