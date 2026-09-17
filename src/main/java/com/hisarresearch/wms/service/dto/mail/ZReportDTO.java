package com.hisarresearch.wms.service.dto.mail;

import com.hisarresearch.wms.domain.AurVwZReport;

import java.util.List;

public class ZReportDTO {
    private List<AurVwZReport> receivingOrderList;
    private List<AurVwZReport> dispatchOrderList;

    public List<AurVwZReport> getReceivingOrderList() {
        return receivingOrderList;
    }

    public void setReceivingOrderList(List<AurVwZReport> receivingOrderList) {
        this.receivingOrderList = receivingOrderList;
    }

    public List<AurVwZReport> getDispatchOrderList() {
        return dispatchOrderList;
    }

    public void setDispatchOrderList(List<AurVwZReport> dispatchOrderList) {
        this.dispatchOrderList = dispatchOrderList;
    }
}
