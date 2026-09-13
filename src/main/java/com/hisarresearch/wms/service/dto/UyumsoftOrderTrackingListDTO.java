package com.hisarresearch.wms.service.dto;

public class UyumsoftOrderTrackingListDTO {

    private String orderNo;
    private String cargoDetail;
    private String customerCode;
    private String description;
    private String actionCode;
    private String orderMasterNo;
    private String depoCode;
    private String orderDate;
    private String customerName;
    private Boolean enable = true;

    private Boolean hasDone = false;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCargoDetail() {
        return cargoDetail;
    }

    public void setCargoDetail(String cargoDetail) {
        this.cargoDetail = cargoDetail;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getOrderMasterNo() {
        return orderMasterNo;
    }

    public void setOrderMasterNo(String orderMasterNo) {
        this.orderMasterNo = orderMasterNo;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getHasDone() {
        return hasDone;
    }

    public void setHasDone(Boolean hasDone) {
        this.hasDone = hasDone;
    }
}
