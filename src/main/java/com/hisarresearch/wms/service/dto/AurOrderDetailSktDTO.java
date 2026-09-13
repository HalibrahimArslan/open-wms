package com.hisarresearch.wms.service.dto;

import java.time.Instant;

public class AurOrderDetailSktDTO {
    private Long id;
    private Instant sktDate;
    private double quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSktDate() {
        return sktDate;
    }

    public void setSktDate(Instant sktDate) {
        this.sktDate = sktDate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
