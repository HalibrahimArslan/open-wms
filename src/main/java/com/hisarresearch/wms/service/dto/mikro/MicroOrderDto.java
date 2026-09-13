package com.hisarresearch.wms.service.dto.mikro;


import java.util.List;

public class MicroOrderDto {
    private String orderDate;
    private String orderNo;
    private String cariKod;
    private int depoNo;
    private List<MicroOrderDetailDto> orderDetailList;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public int getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(int depoNo) {
        this.depoNo = depoNo;
    }

    public List<MicroOrderDetailDto> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<MicroOrderDetailDto> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
