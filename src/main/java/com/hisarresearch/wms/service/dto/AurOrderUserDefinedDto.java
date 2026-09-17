package com.hisarresearch.wms.service.dto;

public class AurOrderUserDefinedDto {

    private String firmCode;
    private String status;
    private String opType;
    private Integer depoNo;

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpType() {
        return opType;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }
}
