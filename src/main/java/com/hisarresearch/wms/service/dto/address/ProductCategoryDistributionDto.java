package com.hisarresearch.wms.service.dto.address;

public class ProductCategoryDistributionDto {
    private String productCategoryName;

    private Long count;

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
