package com.hisarresearch.wms.service.dto.uyumsoft;

import java.util.List;

public class CloseOrderDto {
    private String depoNo;
    private String firmCode;
    private String orderNo;
    private List<IrsaliyeDetailRequestDTO> orderDetailList;

    public String getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(String depoNo) {
        this.depoNo = depoNo;
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

    public List<IrsaliyeDetailRequestDTO> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<IrsaliyeDetailRequestDTO> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
