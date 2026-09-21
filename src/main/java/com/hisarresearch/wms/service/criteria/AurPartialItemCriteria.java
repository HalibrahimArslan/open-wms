package com.hisarresearch.wms.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.BooleanFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;


public class AurPartialItemCriteria implements Serializable, Criteria {

    private StringFilter packageBarcode;

    private StringFilter packageCode;

    private StringFilter packageName;

    private BooleanFilter status;

    public AurPartialItemCriteria(){}

    public AurPartialItemCriteria(AurPartialItemCriteria other){
        this.packageBarcode = other.packageBarcode == null ? null : other.packageBarcode.copy();
        this.packageCode = other.packageCode == null ? null : other.packageCode.copy();
        this.packageName = other.packageName == null ? null : other.packageName.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public AurPartialItemCriteria copy(){ return new AurPartialItemCriteria(this);}

    public StringFilter getPackageBarcode() {
        return packageBarcode;
    }

    public void setPackageBarcode(StringFilter packageBarcode) {
        this.packageBarcode = packageBarcode;
    }

    public StringFilter getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(StringFilter packageCode) {
        this.packageCode = packageCode;
    }

    public StringFilter getPackageName() {
        return packageName;
    }

    public void setPackageName(StringFilter packageName) {
        this.packageName = packageName;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AurPartialItemCriteria that = (AurPartialItemCriteria) o;
        return Objects.equals(packageBarcode, that.packageBarcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageBarcode);
    }

    @Override
    public String toString() {
        return "AurPartialItemCriteria{" +
            "packageBarcode=" + packageBarcode +
            "packageCode=" + packageCode +
            '}';
    }
}
