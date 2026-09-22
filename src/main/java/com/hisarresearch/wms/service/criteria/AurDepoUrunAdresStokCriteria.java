package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class AurDepoUrunAdresStokCriteria implements Serializable, Criteria {
    private LongFilter id;

    private LongFilter urunAdresId;

    private StringFilter depoCode;


    public AurDepoUrunAdresStokCriteria(){}

    public AurDepoUrunAdresStokCriteria(AurDepoUrunAdresStokCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.urunAdresId = other.urunAdresId == null ? null : other.urunAdresId.copy();
        this.depoCode = other.depoCode == null ? null : other.depoCode.copy();
    }



    @Override
    public AurDepoUrunAdresStokCriteria copy() {
        return new AurDepoUrunAdresStokCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(LongFilter urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public StringFilter getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(StringFilter depoCode) {
        this.depoCode = depoCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AurDepoUrunAdresStokCriteria that = (AurDepoUrunAdresStokCriteria) o;
        return (
            Objects.equals(id, that.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            urunAdresId,
            depoCode
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurDepoUrunAdresStokCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (urunAdresId != null ? "urunAdresId=" + urunAdresId + "}" : "") +
            (depoCode != null ? "depoCode=" + depoCode + "}" : "}");

    }
}
