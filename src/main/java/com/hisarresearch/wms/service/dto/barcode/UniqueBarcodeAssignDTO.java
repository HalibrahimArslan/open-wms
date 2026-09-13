package com.hisarresearch.wms.service.dto.barcode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class UniqueBarcodeAssignDTO {

    @NotNull
    private String barcode;

    @NotNull
    @Positive
    private BigDecimal quantity;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
