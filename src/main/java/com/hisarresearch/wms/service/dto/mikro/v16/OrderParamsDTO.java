package com.hisarresearch.wms.service.dto.mikro.v16;

public class OrderParamsDTO {
    private String evrakSeri;

    private String evrakSira;

    private int sipTip;

    private int depoNo;

    public String getEvrakSeri() {
        return evrakSeri;
    }

    public void setEvrakSeri(String evrakSeri) {
        this.evrakSeri = evrakSeri;
    }

    public String getEvrakSira() {
        return evrakSira;
    }

    public void setEvrakSira(String evrakSira) {
        this.evrakSira = evrakSira;
    }

    public int getSipTip() {
        return sipTip;
    }

    public void setSipTip(int sipTip) {
        this.sipTip = sipTip;
    }

    public int getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(int depoNo) {
        this.depoNo = depoNo;
    }
}
