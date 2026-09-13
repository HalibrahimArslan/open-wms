package com.hisarresearch.wms.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;

public class ProductAddressCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    public StringFilter depoCode;

    public StringFilter companyCode;

    public LongFilter addressId;

    public StringFilter stokKod;

    public StringFilter barcode;

    public StringFilter stokAdi;

    public BooleanFilter status;

    public BooleanFilter temporaryAddress;

    public BooleanFilter pickingAddress;

    public BooleanFilter controlAddress;

    public StringFilter address;

    public StringFilter multiSearch;

    public BooleanFilter allAddressType;



    public ProductAddressCriteria() {

    }

    public ProductAddressCriteria(ProductAddressCriteria other) {
        this.depoCode = other.depoCode == null ? null : other.depoCode.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.stokKod = other.stokKod == null ? null : other.stokKod.copy();
        this.barcode = other.barcode == null ? null : other.barcode.copy();
        this.stokAdi = other.stokAdi == null ? null : other.stokAdi.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyCode = other.companyCode == null ? null : other.companyCode.copy();
        this.temporaryAddress = other.temporaryAddress == null ? null : other.temporaryAddress.copy();
        this.controlAddress = other.controlAddress == null ? null : other.controlAddress.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.pickingAddress  = other.pickingAddress == null ? null : other.pickingAddress.copy();
        this.multiSearch = other.multiSearch == null ? null : other.multiSearch.copy();
        this.allAddressType = other.allAddressType == null ? null : other.allAddressType.copy();
    }

    @Override
    public ProductAddressCriteria copy() {
        return new ProductAddressCriteria(new ProductAddressCriteria(this));
    }

    public StringFilter getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(StringFilter depoCode) {
        this.depoCode = depoCode;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public StringFilter getStokKod() {
        return stokKod;
    }

    public void setStokKod(StringFilter stokKod) {
        this.stokKod = stokKod;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public StringFilter getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(StringFilter stokAdi) {
        this.stokAdi = stokAdi;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public StringFilter getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(StringFilter companyCode) {
        this.companyCode = companyCode;
    }

    public BooleanFilter getTemporaryAddress() {
        return temporaryAddress;
    }

    public void setTemporaryAddress(BooleanFilter temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public BooleanFilter getControlAddress() {
        return controlAddress;
    }

    public void setControlAddress(BooleanFilter controlAddress) {
        this.controlAddress = controlAddress;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public BooleanFilter getPickingAddress() {
        return pickingAddress;
    }

    public void setPickingAddress(BooleanFilter pickingAddress) {
        this.pickingAddress = pickingAddress;
    }

    public StringFilter getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(StringFilter multiSearch) {
        this.multiSearch = multiSearch;
    }

    public BooleanFilter getAllAddressType() {
        return allAddressType;
    }

    public void setAllAddressType(BooleanFilter allAddressType) {
        this.allAddressType = allAddressType;
    }
}
