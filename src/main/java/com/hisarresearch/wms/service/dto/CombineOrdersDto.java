package com.hisarresearch.wms.service.dto;

import java.util.List;

public class CombineOrdersDto {

    String orderInfo;

    List<Long> orderIdList;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<Long> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<Long> orderIdList) {
        this.orderIdList = orderIdList;
    }
}
