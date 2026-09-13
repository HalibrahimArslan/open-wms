package com.hisarresearch.wms.service.dto.counting;

import com.hisarresearch.wms.domain.Product;

public class AurSayimDetailDto {
    private Product product;
    private double countingAmount;
    private boolean countingStatus;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getCountingAmount() {
        return countingAmount;
    }

    public void setCountingAmount(double countingAmount) {
        this.countingAmount = countingAmount;
    }

    public boolean isCountingStatus() {
        return countingStatus;
    }

    public void setCountingStatus(boolean countingStatus) {
        this.countingStatus = countingStatus;
    }
}
