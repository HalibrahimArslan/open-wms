package com.hisarresearch.wms.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;

public class CountingAddressExceptionCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter countingDefinitionId;

    private BooleanFilter status;

    private StringFilter address;

    public CountingAddressExceptionCriteria() {}

    public CountingAddressExceptionCriteria(CountingAddressExceptionCriteria other) {
        this.countingDefinitionId = other.countingDefinitionId == null ? null : other.countingDefinitionId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.address = other.address == null ? null : other.address.copy();
    }

    @Override
    public CountingAddressExceptionCriteria copy() {
        return new CountingAddressExceptionCriteria(this);
    }

    public LongFilter getCountingDefinitionId() {
        return countingDefinitionId;
    }

    public void setCountingDefinitionId(LongFilter countingDefinitionId) {
        this.countingDefinitionId = countingDefinitionId;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }
}
