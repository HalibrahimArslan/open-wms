package com.hisarresearch.wms.service.dto.barcode;

import com.hisarresearch.wms.domain.enumeration.PalletBarcodeStatus;

public class PalletBarcodeResponseDto {
    private String palletBarcode;
    private PalletBarcodeStatus palletBarcodeStatus;

    public String getPalletBarcode() {
        return palletBarcode;
    }

    public void setPalletBarcode(String palletBarcode) {
        this.palletBarcode = palletBarcode;
    }

    public PalletBarcodeStatus getPalletBarcodeStatus() {
        return palletBarcodeStatus;
    }

    public void setPalletBarcodeStatus(PalletBarcodeStatus palletBarcodeStatus) {
        this.palletBarcodeStatus = palletBarcodeStatus;
    }
}
