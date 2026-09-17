package com.hisarresearch.wms.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;

import java.io.Serializable;

public class OrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isTransferred;

    private IntegerFilter entranceWarehosue;

    private IntegerFilter transferWarehouse;

    private LongFilter orderstatus;

    private IntegerFilter documentType;


    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {

        this.id = other.id == null ? null : other.id.copy();
        this.isTransferred = other.isTransferred == null ? null : other.isTransferred.copy();
        this.entranceWarehosue = other.entranceWarehosue == null ? null : other.entranceWarehosue.copy();
        this.transferWarehouse = other.transferWarehouse == null ? null : other.transferWarehouse.copy();
        this.orderstatus = other.orderstatus == null ? null : other.orderstatus.copy();
        this.documentType = other.documentType == null ? null : other.documentType.copy();

    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getIsTransferred() {
        return isTransferred;
    }

    public void setIsTransferred(BooleanFilter isTransferred) {
        this.isTransferred = isTransferred;
    }

    public IntegerFilter getEntranceWarehosue() {
        return entranceWarehosue;
    }

    public void setEntranceWarehosue(IntegerFilter entranceWarehosue) {
        this.entranceWarehosue = entranceWarehosue;
    }

    public IntegerFilter getTransferWarehouse() {
        return transferWarehouse;
    }

    public void setTransferWarehouse(IntegerFilter transferWarehouse) {
        this.transferWarehouse = transferWarehouse;
    }

    public LongFilter getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(LongFilter orderstatus) {
        this.orderstatus = orderstatus;
    }

    public IntegerFilter getDocumentType() {
        return documentType;
    }

    public void setDocumentType(IntegerFilter documentType) {
        this.documentType = documentType;
    }
}
