package com.hisarresearch.wms.service.dto.event;

import com.hisarresearch.wms.domain.AurOrderMaster;

public class AurOrderMasterEvent {
    private AurOrderMaster aurOrderMaster;

    public AurOrderMasterEvent() {
    }

    public AurOrderMasterEvent(AurOrderMaster savedOrder) {
        this.aurOrderMaster = savedOrder;
    }

    public AurOrderMaster getAurOrderMaster() {
        return aurOrderMaster;
    }

    public void setAurOrderMaster(AurOrderMaster aurOrderMaster) {
        this.aurOrderMaster = aurOrderMaster;
    }
}
