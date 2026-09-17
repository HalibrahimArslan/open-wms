package com.hisarresearch.wms.service.dto;

public class WaybillQueryRequestDto {
    private String firmCode;
    private Integer evrakTip;

    /** {@code null} ise kaynak filtresi uygulanmaz; {@code ""}/{@code "DYS"}/{@code "ERP"} olabilir. */
    private String kaynak;

    private String beginDate;
    private String endDate;

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public Integer getEvrakTip() {
        return evrakTip;
    }

    public void setEvrakTip(Integer evrakTip) {
        this.evrakTip = evrakTip;
    }

    public String getKaynak() {
        return kaynak;
    }

    public void setKaynak(String kaynak) {
        this.kaynak = kaynak;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
