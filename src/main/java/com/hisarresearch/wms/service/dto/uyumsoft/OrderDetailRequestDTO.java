package com.hisarresearch.wms.service.dto.uyumsoft;

public class OrderDetailRequestDTO {
    private String operationType;
    private int depoCode;
    private String firmCode;
    private String orderMasterNo;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(int depoCode) {
        this.depoCode = depoCode;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getOrderMasterNo() {
        return orderMasterNo;
    }

    public void setOrderMasterNo(String orderMasterNo) {
        this.orderMasterNo = orderMasterNo;
    }
}
