package com.hisarresearch.wms.service.dto.barcode;

import java.util.List;

public class PalletBarcodeSaveDto {

    private Long palletBarcodeId;
    private Boolean palletBarcodeStatus = true;
    private String palletBarcode;

    private List<PalletDetailDto> palletBarcodeList;

    public Long getPalletBarcodeId() {
        return palletBarcodeId;
    }

    public void setPalletBarcodeId(Long palletBarcodeId) {
        this.palletBarcodeId = palletBarcodeId;
    }

    public String getPalletBarcode() {
        return palletBarcode;
    }

    public void setPalletBarcode(String palletBarcode) {
        this.palletBarcode = palletBarcode;
    }

    public List<PalletDetailDto> getPalletBarcodeList() {
        return palletBarcodeList;
    }

    public void setPalletBarcodeList(List<PalletDetailDto> palletBarcodeList) {
        this.palletBarcodeList = palletBarcodeList;
    }

    public Boolean getPalletBarcodeStatus() {
        return palletBarcodeStatus;
    }
}
