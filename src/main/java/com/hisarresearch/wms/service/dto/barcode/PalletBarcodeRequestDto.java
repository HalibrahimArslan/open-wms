package com.hisarresearch.wms.service.dto.barcode;

import java.util.List;

public class PalletBarcodeRequestDto {

    private String aurOrderId;

    private Long palletBarcodeId;

    private List<String> palletBarcodeList;

    public String getAurOrderId() {
        return aurOrderId;
    }

    public void setAurOrderId(String aurOrderId) {
        this.aurOrderId = aurOrderId;
    }

    public List<String> getPalletBarcodeList() {
        return palletBarcodeList;
    }

    public void setPalletBarcodeList(List<String> palletBarcodeList) {
        this.palletBarcodeList = palletBarcodeList;
    }

    public Long getPalletBarcodeId() {
        return palletBarcodeId;
    }

    public void setPalletBarcodeId(Long palletBarcodeId) {
        this.palletBarcodeId = palletBarcodeId;
    }
}
