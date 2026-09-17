package com.hisarresearch.wms.service.dto;

public class AurCariOrderSummaryDto {

    private String orderNo;
    private String transGroupName;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTransGroupName() {
        return transGroupName;
    }

    public void setTransGroupName(String transGroupName) {
        this.transGroupName = transGroupName;
    }
}
