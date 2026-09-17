package com.hisarresearch.wms.service.dto;

import java.util.List;

public class OrderDetailListRequestDto {
    private List<String> orderNoList;
    private Integer sipTip;
    private List<Integer> depoList;

    public List<String> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<String> orderNoList) {
        this.orderNoList = orderNoList;
    }

    public Integer getSipTip() {
        return sipTip;
    }

    public void setSipTip(Integer sipTip) {
        this.sipTip = sipTip;
    }

    public List<Integer> getDepoList() {
        return depoList;
    }

    public void setDepoList(List<Integer> depoList) {
        this.depoList = depoList;
    }
}
