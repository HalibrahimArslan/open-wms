package com.hisarresearch.wms.service.dto;

import java.util.List;

public class AurOrderWithoutAssignDto {
    int depoCode;
    String firmCode;
    String opType;
    List<String> orderList;

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

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }
}
