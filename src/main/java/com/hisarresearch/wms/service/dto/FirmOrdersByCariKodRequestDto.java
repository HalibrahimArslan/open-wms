package com.hisarresearch.wms.service.dto;

public class FirmOrdersByCariKodRequestDto {
    private Integer depoNo;
    private Integer sipTip;
    private Integer cbt;
    private String cariKod;

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public Integer getSipTip() {
        return sipTip;
    }

    public void setSipTip(Integer sipTip) {
        this.sipTip = sipTip;
    }

    public Integer getCbt() {
        return cbt;
    }

    public void setCbt(Integer cbt) {
        this.cbt = cbt;
    }

    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }
}
