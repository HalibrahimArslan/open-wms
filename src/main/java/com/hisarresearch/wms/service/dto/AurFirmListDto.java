package com.hisarresearch.wms.service.dto;

import java.util.List;

public class AurFirmListDto {
    private List<Integer> depoList;
    private String firmCode;
    private Integer sipTip;
    private String transGroupCode;
    public List<Integer> getDepoList() {
        return depoList;
    }

    public void setDepoList(List<Integer> depoList) {
        this.depoList = depoList;
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

    public String getTransGroupCode() {
        return transGroupCode;
    }

    public void setTransGroupCode(String transGroupCode) {
        this.transGroupCode = transGroupCode;
    }
}
