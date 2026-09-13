package com.hisarresearch.wms.service.dto;

import java.io.Serializable;

public class CamlicaProductUpdateDto implements Serializable {
    private String firmCode;

    private String barkod;

    public CamlicaProductUpdateDto(String firmCode, String barcode) {
        this.firmCode = firmCode;
        this.barkod = barcode;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }
}
