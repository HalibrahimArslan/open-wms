package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import java.io.Serializable;
import java.util.Objects;
import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.BooleanFilter;
import com.hisarresearch.wms.framework.service.filter.DoubleFilter;
import com.hisarresearch.wms.framework.service.filter.Filter;
import com.hisarresearch.wms.framework.service.filter.FloatFilter;
import com.hisarresearch.wms.framework.service.filter.InstantFilter;
import com.hisarresearch.wms.framework.service.filter.IntegerFilter;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hisarresearch.wms.domain.AurSayimTanim} entity. This class is used
 * in {@link com.hisarresearch.wms.web.rest.AurSayimTanimResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /aur-sayim-tanims?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AurSayimTanimCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering SayimDurumu
     */
    public static class CountingTypeFilter extends Filter<CountingType> {

        public CountingTypeFilter() {}

        public CountingTypeFilter(CountingTypeFilter filter) {
            super(filter);
        }

        @Override
        public CountingTypeFilter copy() {
            return new CountingTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter status;

    private StringFilter sayimAdi;

    private StringFilter depoNo;

    private InstantFilter sayimTarihi;

    private SayimDurumuFilter sayimDurumu;

    private LongFilter aurSayimUrunId;

    private CountingTypeFilter countingType;

    public AurSayimTanimCriteria() {}

    public AurSayimTanimCriteria(AurSayimTanimCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.sayimAdi = other.sayimAdi == null ? null : other.sayimAdi.copy();
        this.depoNo = other.depoNo == null ? null : other.depoNo.copy();
        this.sayimTarihi = other.sayimTarihi == null ? null : other.sayimTarihi.copy();
        this.sayimDurumu = other.sayimDurumu == null ? null : other.sayimDurumu.copy();
        this.aurSayimUrunId = other.aurSayimUrunId == null ? null : other.aurSayimUrunId.copy();
        this.countingType = other.countingType == null ? null : other.countingType.copy();
    }

    @Override
    public AurSayimTanimCriteria copy() {
        return new AurSayimTanimCriteria(this);
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

    public BooleanFilter getStatus() {
        return status;
    }

    public BooleanFilter status() {
        if (status == null) {
            status = new BooleanFilter();
        }
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public StringFilter getSayimAdi() {
        return sayimAdi;
    }

    public StringFilter sayimAdi() {
        if (sayimAdi == null) {
            sayimAdi = new StringFilter();
        }
        return sayimAdi;
    }

    public void setSayimAdi(StringFilter sayimAdi) {
        this.sayimAdi = sayimAdi;
    }

    public StringFilter getDepoNo() {
        return depoNo;
    }

    public StringFilter depoNo() {
        if (depoNo == null) {
            depoNo = new StringFilter();
        }
        return depoNo;
    }

    public void setDepoNo(StringFilter depoNo) {
        this.depoNo = depoNo;
    }

    public InstantFilter getSayimTarihi() {
        return sayimTarihi;
    }

    public InstantFilter sayimTarihi() {
        if (sayimTarihi == null) {
            sayimTarihi = new InstantFilter();
        }
        return sayimTarihi;
    }

    public void setSayimTarihi(InstantFilter sayimTarihi) {
        this.sayimTarihi = sayimTarihi;
    }

    public SayimDurumuFilter getSayimDurumu() {
        return sayimDurumu;
    }

    public SayimDurumuFilter sayimDurumu() {
        if (sayimDurumu == null) {
            sayimDurumu = new SayimDurumuFilter();
        }
        return sayimDurumu;
    }

    public CountingTypeFilter getCountingType() {
        return countingType;
    }

    public void setCountingType(CountingTypeFilter countingType) {
        this.countingType = countingType;
    }

    public CountingTypeFilter countingType() {
        if (countingType == null) {
            countingType = new CountingTypeFilter();
        }
        return countingType;
    }

    public void setSayimDurumu(SayimDurumuFilter sayimDurumu) {
        this.sayimDurumu = sayimDurumu;
    }

    public LongFilter getAurSayimUrunId() {
        return aurSayimUrunId;
    }

    public LongFilter aurSayimUrunId() {
        if (aurSayimUrunId == null) {
            aurSayimUrunId = new LongFilter();
        }
        return aurSayimUrunId;
    }

    public void setAurSayimUrunId(LongFilter aurSayimUrunId) {
        this.aurSayimUrunId = aurSayimUrunId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AurSayimTanimCriteria that = (AurSayimTanimCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(sayimAdi, that.sayimAdi) &&
            Objects.equals(depoNo, that.depoNo) &&
            Objects.equals(sayimTarihi, that.sayimTarihi) &&
            Objects.equals(sayimDurumu, that.sayimDurumu) &&
            Objects.equals(aurSayimUrunId, that.aurSayimUrunId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            status,
            sayimAdi,
            depoNo,
            sayimTarihi,
            sayimDurumu,
            aurSayimUrunId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurSayimTanimCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (sayimAdi != null ? "sayimAdi=" + sayimAdi + ", " : "") +
            (depoNo != null ? "depoNo=" + depoNo + ", " : "") +
            (sayimTarihi != null ? "sayimTarihi=" + sayimTarihi + ", " : "") +
            (sayimDurumu != null ? "sayimDurumu=" + sayimDurumu + ", " : "") +
            (aurSayimUrunId != null ? "aurSayimUrunId=" + aurSayimUrunId + ", " : "") +
            "}";
    }
}
