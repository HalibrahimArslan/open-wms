package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hisarresearch.wms.domain.AurSayimUrun} entity. This class is used
 * in {@link com.hisarresearch.wms.web.rest.AurSayimUrunResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aur-sayim-uruns?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AurSayimUrunCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SayimDurumu
     */
    public static class SayimDurumuFilter extends Filter<SayimDurumu> {

        public SayimDurumuFilter() {}

        public SayimDurumuFilter(SayimDurumuFilter filter) {
            super(filter);
        }

        @Override
        public SayimDurumuFilter copy() {
            return new SayimDurumuFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter sayimUrunId;

    private StringFilter stokKod;

    private SayimDurumuFilter status;

    private LongFilter aurSayimTanimId;

    private StringFilter barkod;

    private StringFilter companyCode;

    private BooleanFilter checkPartialItem;

    private StringFilter stokAdi;

    public AurSayimUrunCriteria() {}

    public AurSayimUrunCriteria(AurSayimUrunCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sayimUrunId = other.sayimUrunId == null ? null : other.sayimUrunId.copy();
        this.stokKod = other.stokKod == null ? null : other.stokKod.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.aurSayimTanimId = other.aurSayimTanimId == null ? null : other.aurSayimTanimId.copy();
        this.barkod = other.barkod == null ? null : other.barkod.copy();
        this.checkPartialItem = other.checkPartialItem == null ? null : other.checkPartialItem.copy();
        this.companyCode = other.companyCode == null ? null : other.companyCode.copy();
        this.stokAdi = other.stokAdi == null ? null : other.stokAdi.copy();
    }

    @Override
    public AurSayimUrunCriteria copy() {
        return new AurSayimUrunCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSayimUrunId() {
        return sayimUrunId;
    }

    public LongFilter sayimUrunId() {
        if (sayimUrunId == null) {
            sayimUrunId = new LongFilter();
        }
        return sayimUrunId;
    }

    public void setSayimUrunId(LongFilter sayimUrunId) {
        this.sayimUrunId = sayimUrunId;
    }

    public StringFilter getStokKod() {
        return stokKod;
    }

    public StringFilter stokKod() {
        if (stokKod == null) {
            stokKod = new StringFilter();
        }
        return stokKod;
    }

    public void setStokKod(StringFilter stokKod) {
        this.stokKod = stokKod;
    }

    public SayimDurumuFilter getStatus() {
        return status;
    }

    public SayimDurumuFilter status() {
        if (status == null) {
            status = new SayimDurumuFilter();
        }
        return status;
    }

    public void setStatus(SayimDurumuFilter status) {
        this.status = status;
    }


    public LongFilter getAurSayimTanimId() {
        return aurSayimTanimId;
    }

    public LongFilter aurSayimTanimId() {
        if (aurSayimTanimId == null) {
            aurSayimTanimId = new LongFilter();
        }
        return aurSayimTanimId;
    }

    public void setAurSayimTanimId(LongFilter aurSayimTanimId) {
        this.aurSayimTanimId = aurSayimTanimId;
    }

    public StringFilter getBarkod() {
        return barkod;
    }

    public void setBarkod(StringFilter barkod) {
        this.barkod = barkod;
    }

    public BooleanFilter getCheckPartialItem() {
        return checkPartialItem;
    }

    public void setCheckPartialItem(BooleanFilter checkPartialItem) {
        this.checkPartialItem = checkPartialItem;
    }

    public StringFilter getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(StringFilter companyCode) {
        this.companyCode = companyCode;
    }

    public StringFilter getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(StringFilter stokAdi) {
        this.stokAdi = stokAdi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AurSayimUrunCriteria that = (AurSayimUrunCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sayimUrunId, that.sayimUrunId) &&
            Objects.equals(stokKod, that.stokKod) &&
            Objects.equals(status, that.status) &&
            Objects.equals(aurSayimTanimId, that.aurSayimTanimId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sayimUrunId, stokKod, status, aurSayimTanimId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurSayimUrunCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sayimUrunId != null ? "sayimUrunId=" + sayimUrunId + ", " : "") +
            (stokKod != null ? "stokKod=" + stokKod + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (aurSayimTanimId != null ? "aurSayimTanimId=" + aurSayimTanimId + ", " : "") +
            "}";
    }
}
