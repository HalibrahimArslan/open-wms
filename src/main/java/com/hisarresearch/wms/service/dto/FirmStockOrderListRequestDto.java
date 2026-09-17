package com.hisarresearch.wms.service.dto;

public class FirmStockOrderListRequestDto {
    private Integer depoNo;
    private String firmCode;
    private Integer sipTip;

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public Integer getSipTip() {
        return sipTip;
    }

    public void setSipTip(Integer sipTip) {
        this.sipTip = sipTip;
    }
}
