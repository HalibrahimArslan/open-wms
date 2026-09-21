package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.BooleanFilter;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;

public class WarehouseCriteria implements Serializable, Criteria {
    private LongFilter id;
    private StringFilter name;
    private BooleanFilter real;
    private StringFilter companyCode;
    private BooleanFilter autoScan;
    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BooleanFilter getReal() {
        return real;
    }

    public void setReal(BooleanFilter real) {
        this.real = real;
    }

    public StringFilter getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(StringFilter companyCode) {
        this.companyCode = companyCode;
    }

    public BooleanFilter getAutoScan() {return autoScan;}
    public void setAutoScan(BooleanFilter autoScan) {this.autoScan = autoScan;}

    public WarehouseCriteria(){}

    public WarehouseCriteria(WarehouseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.real = other.real == null ? null : other.real.copy();
        this.companyCode = other.companyCode == null ? null : other.companyCode.copy();
    }
    @Override
    public WarehouseCriteria copy(){ return new WarehouseCriteria(this);}
}
