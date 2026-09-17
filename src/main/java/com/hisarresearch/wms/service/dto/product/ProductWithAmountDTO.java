package com.hisarresearch.wms.service.dto.product;

import com.hisarresearch.wms.domain.Product;

public class ProductWithAmountDTO {
    private double depoAmount = 0.0;
    private Product product;

    public double getDepoAmount() {
        return depoAmount;
    }

    public void setDepoAmount(double depoAmount) {
        this.depoAmount = depoAmount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
