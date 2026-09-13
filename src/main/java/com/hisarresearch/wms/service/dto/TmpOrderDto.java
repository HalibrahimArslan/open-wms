package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.AurOrderDetail;

import java.util.List;

public class TmpOrderDto {
    private long masterId;

    private List<AurOrderDetail> aurOrderDetailList;

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public List<AurOrderDetail> getAurTmpDetailList() {
        return aurOrderDetailList;
    }

    public void setAurTmpDetailList(List<AurOrderDetail> aurOrderDetailList) {
        this.aurOrderDetailList = aurOrderDetailList;
    }
}
