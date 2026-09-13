package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.AurOrderDetail;

import java.util.List;

public class AurOrderDetailReformSevkiyatDTO {
    private Long aurOrderId;
    private String status;
    private List<AurOrderDetail> deleteItems;
    private List<AurCariOrderDetailListDto> addedItems;

    public Long getAurOrderId() {
        return aurOrderId;
    }

    public void setAurOrderId(Long aurOrderId) {
        this.aurOrderId = aurOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AurOrderDetail> getDeleteItems() {
        return deleteItems;
    }

    public void setDeleteItems(List<AurOrderDetail> deleteItems) {
        this.deleteItems = deleteItems;
    }

    public List<AurCariOrderDetailListDto> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<AurCariOrderDetailListDto> addedItems) {
        this.addedItems = addedItems;
    }
}
