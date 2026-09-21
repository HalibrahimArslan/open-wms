package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.domain.enumeration.AddressMovementType;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.Filter;
import com.hisarresearch.wms.framework.service.filter.InstantFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;

public class AddressMovementHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    public static class AddressMovementTypeFilter extends Filter<AddressMovementType> {

        public AddressMovementTypeFilter() {}

        public AddressMovementTypeFilter(AddressMovementHistoryCriteria.AddressMovementTypeFilter filter) {
            super(filter);
        }

        @Override
        public AddressMovementHistoryCriteria.AddressMovementTypeFilter copy() {
            return new AddressMovementHistoryCriteria.AddressMovementTypeFilter(this);
        }
    }

    private AddressMovementTypeFilter addressMovementType;

    private InstantFilter createdDate;

    private StringFilter multiSearch;

    public AddressMovementHistoryCriteria() {}

    public AddressMovementHistoryCriteria(AddressMovementHistoryCriteria other) {
        this.addressMovementType = other.addressMovementType == null ? null : other.addressMovementType.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.multiSearch = other.multiSearch == null ? null : other.multiSearch.copy();
    }

    @Override
    public AddressMovementHistoryCriteria copy() {
        return new AddressMovementHistoryCriteria(this);
    }

    public AddressMovementTypeFilter getAddressMovementType() {
        return addressMovementType;
    }

    public void setAddressMovementType(AddressMovementTypeFilter addressMovementType) {
        this.addressMovementType = addressMovementType;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(StringFilter multiSearch) {
        this.multiSearch = multiSearch;
    }
}
