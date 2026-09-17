package com.hisarresearch.wms.service.dto.address;

public class PartialProductSaveToAddressDTO {
    private String  address;

    private String barcode;

    private Long partialItemId;

    private Double amount;

    private String depoCode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getPartialItemId() {
        return partialItemId;
    }

    public void setPartialItemId(Long partialItemId) {
        this.partialItemId = partialItemId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }
}
