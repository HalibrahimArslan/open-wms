package com.hisarresearch.wms.service.dto;

import javax.validation.constraints.NotNull;

public class FirmDto {

    @NotNull
    private String firmCode;

    @NotNull
    private String firmName;

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
}
