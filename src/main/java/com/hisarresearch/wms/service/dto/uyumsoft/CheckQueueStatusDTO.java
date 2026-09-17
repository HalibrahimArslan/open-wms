package com.hisarresearch.wms.service.dto.uyumsoft;

public class CheckQueueStatusDTO {

    String orderInfo;
    String batchRequestId;
    String firmaKod;
    String msip_no;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getBatchRequestId() {
        return batchRequestId;
    }

    public void setBatchRequestId(String batchRequestId) {
        this.batchRequestId = batchRequestId;
    }

    public String getFirmaKod() {
        return firmaKod;
    }

    public void setFirmaKod(String firmaKod) {
        this.firmaKod = firmaKod;
    }

    public String getMsip_no() {
        return msip_no;
    }

    public void setMsip_no(String msip_no) {
        this.msip_no = msip_no;
    }
}
