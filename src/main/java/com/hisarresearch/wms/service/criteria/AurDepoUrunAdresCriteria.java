package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.BooleanFilter;
import com.hisarresearch.wms.framework.service.filter.IntegerFilter;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class AurDepoUrunAdresCriteria implements Serializable, Criteria {
    private LongFilter urunAdresId;
    private StringFilter depoNo;
    private StringFilter adres;
    private BooleanFilter toplamaGozu;
    private BooleanFilter geciciAdres;
    private BooleanFilter countable;
    private BooleanFilter kontrolAdres;
    private StringFilter koridor;
    private StringFilter kat;
    private BooleanFilter status;


    public AurDepoUrunAdresCriteria(){}

    public AurDepoUrunAdresCriteria(AurDepoUrunAdresCriteria other) {
        this.urunAdresId = other.urunAdresId == null ? null : other.urunAdresId.copy();
        this.depoNo = other.depoNo == null ? null : other.depoNo.copy();
        this.toplamaGozu = other.toplamaGozu == null ? null : other.toplamaGozu.copy();
        this.geciciAdres = other.geciciAdres == null ? null : other.geciciAdres.copy();
        this.adres = other.adres == null ? null : other.adres.copy();
        this.countable = other.countable == null ? null : other.countable.copy();
        this.kontrolAdres = other.kontrolAdres == null ? null : other.kontrolAdres.copy();
        this.koridor = other.koridor == null ? null : other.koridor.copy();
        this.kat = other.kat == null ? null : other.kat.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public AurDepoUrunAdresCriteria copy() {
        return new AurDepoUrunAdresCriteria(this);
    }

    public LongFilter getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(LongFilter urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public StringFilter getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(StringFilter depoNo) {
        this.depoNo = depoNo;
    }

    public BooleanFilter getToplamaGozu() {
        return toplamaGozu;
    }

    public void setToplamaGozu(BooleanFilter toplamaGozu) {
        this.toplamaGozu = toplamaGozu;
    }

    public BooleanFilter getGeciciAdres() {
        return geciciAdres;
    }

    public void setGeciciAdres(BooleanFilter geciciAdres) {
        this.geciciAdres = geciciAdres;
    }

    public StringFilter getAdres() {
        return adres;
    }

    public void setAdres(StringFilter adres) {
        this.adres = adres;
    }

    public BooleanFilter getCountable() {
        return countable;
    }

    public void setCountable(BooleanFilter countable) {
        this.countable = countable;
    }

    public BooleanFilter getKontrolAdres() {
        return kontrolAdres;
    }

    public void setKontrolAdres(BooleanFilter kontrolAdres) {
        this.kontrolAdres = kontrolAdres;
    }

    public StringFilter getKoridor() {
        return koridor;
    }

    public void setKoridor(StringFilter koridor) {
        this.koridor = koridor;
    }

    public StringFilter getKat() {
        return kat;
    }

    public void setKat(StringFilter kat) {
        this.kat = kat;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AurDepoUrunAdresCriteria that = (AurDepoUrunAdresCriteria) o;
        return (
            Objects.equals(urunAdresId, that.urunAdresId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            urunAdresId,
            depoNo,
            toplamaGozu,
            geciciAdres,
            adres
        );
    }

    @Override
    public String toString() {
        return "AurDepoUrunAdresCriteria{" +
            (urunAdresId != null ? "urunAdresId=" + urunAdresId + "}" : "") +
            (depoNo != null ? "depoCode=" + depoNo + "}" : "}");

    }




}
