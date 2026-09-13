package com.hisarresearch.wms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AurCariOrderDto {

    private String orderNo;
    private String orderDate;
    private Integer orderLineItemCount;
    private List<AurCariOrderDetailDto> orderDetail;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderLineItemCount() {
        return orderLineItemCount;
    }

    public void setOrderLineItemCount(Integer orderLineItemCount) {
        this.orderLineItemCount = orderLineItemCount;
    }

    public List<AurCariOrderDetailDto> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<AurCariOrderDetailDto> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
