package com.hisarresearch.wms.service.criteria;

import io.swagger.v3.oas.annotations.media.Schema;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class ProductCriteria implements Serializable, Criteria {

    private StringFilter barkod;
    private StringFilter depoCode;
    private StringFilter companyCode;
    private BooleanFilter status;
    private StringFilter stokAdi;
    private StringFilter stokKodu;

    @Schema(description = "Barkod, stok kodu ve stok adı üzerinde tek alanla arama yapar (OR). Örn: multiSearch.contains=ABC")
    private StringFilter multiSearch;
    private BooleanFilter lotBasedTracking;

    public ProductCriteria(){}


    public ProductCriteria(ProductCriteria other) {
        this.barkod = other.barkod == null ? null : other.barkod.copy();
        this.depoCode = other.depoCode == null ? null : other.depoCode.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.companyCode = other.companyCode == null ? null : other.companyCode.copy();
        this.stokAdi = other.stokAdi == null ? null : other.stokAdi.copy();
        this.stokKodu = other.stokKodu == null ? null : other.stokKodu.copy();
        this.multiSearch = other.multiSearch == null ? null : other.multiSearch.copy();
        this.lotBasedTracking = other.lotBasedTracking == null ? null : other.lotBasedTracking.copy();

    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public StringFilter getBarkod() {
        return barkod;
    }

    public void setBarkod(StringFilter barkod) {
        this.barkod = barkod;
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

    public StringFilter getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(StringFilter depoCode) {
        this.depoCode = depoCode;
    }

    public StringFilter getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(StringFilter stokAdi) {
        this.stokAdi = stokAdi;
    }

    public StringFilter getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(StringFilter stokKodu) {
        this.stokKodu = stokKodu;
    }

    public StringFilter getMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(StringFilter multiSearch) {
        this.multiSearch = multiSearch;
    }

    public BooleanFilter getLotBasedTracking() {
        return lotBasedTracking;
    }

    public void setLotBasedTracking(BooleanFilter lotBasedTracking) {
        this.lotBasedTracking = lotBasedTracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCriteria that = (ProductCriteria) o;
        return Objects.equals(barkod, that.barkod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barkod);
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
            "barkod=" + barkod +
            ", depoCode=" + depoCode +
            ", companyCode=" + companyCode +
            ", status=" + status +
            ", stokAdi=" + stokAdi +
            ", stokKodu=" + stokKodu +
            ", multiSearch=" + multiSearch +
            ", lotBasedTracking=" + lotBasedTracking +
            '}';
    }
}
