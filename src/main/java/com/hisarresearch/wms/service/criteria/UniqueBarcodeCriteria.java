package com.hisarresearch.wms.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class UniqueBarcodeCriteria implements Serializable, Criteria {
    private StringFilter erpOrderInfo;
    private StringFilter barcode;

    public UniqueBarcodeCriteria(){}


    public UniqueBarcodeCriteria(UniqueBarcodeCriteria other) {
        this.erpOrderInfo = other.erpOrderInfo == null ? null : other.erpOrderInfo.copy();
        this.barcode = other.barcode == null ? null : other.barcode.copy();
    }

    @Override
    public UniqueBarcodeCriteria copy() {
        return new UniqueBarcodeCriteria(this);
    }

    public StringFilter getErpOrderInfo() {
        return erpOrderInfo;
    }

    public void setErpOrderInfo(StringFilter erpOrderInfo) {
        this.erpOrderInfo = erpOrderInfo;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UniqueBarcodeCriteria)) return false;
        UniqueBarcodeCriteria that = (UniqueBarcodeCriteria) o;
        return Objects.equals(erpOrderInfo, that.erpOrderInfo) &&
            Objects.equals(barcode, that.barcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(erpOrderInfo, barcode);
    }

    @Override
    public String toString() {
        return "UniqueBarcodeCriteria{" +
            "erpOrderInfo=" + erpOrderInfo +
            ", barcode=" + barcode +
            '}';
    }
}
