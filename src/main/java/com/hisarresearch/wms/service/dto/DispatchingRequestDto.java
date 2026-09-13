package com.hisarresearch.wms.service.dto;

import java.util.List;

public class DispatchingRequestDto {

    Integer depoNo;
    String tarih;
    String firmCode;
    String orderNo;
    String erpUserCode;

    List<DispatchingOrderLineItemDto> orderDetailList;

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getErpUserCode() {
        return erpUserCode;
    }

    public void setErpUserCode(String erpUserCode) {
        this.erpUserCode = erpUserCode;
    }

    public List<DispatchingOrderLineItemDto> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<DispatchingOrderLineItemDto> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
