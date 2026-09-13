package com.hisarresearch.wms.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.Serializable;

public class OrderMasterCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;
    private LongFilter id;
    private StringFilter siparisNo;
    private StringFilter stokAdi;
    private StringFilter stokKodu;
    private LongFilter userId;
    private StringFilter belgeNo;
    private IntegerFilter depoNo;
    private StringFilter status;
    private InstantFilter lastModifiedDate;



    public OrderMasterCriteria() {
    }

    public OrderMasterCriteria(OrderMasterCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.siparisNo = other.siparisNo == null ? null : other.siparisNo.copy();
        this.stokAdi = other.stokAdi == null ? null : other.stokAdi.copy();
        this.stokKodu = other.stokKodu == null ? null : other.stokKodu.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.belgeNo = other.belgeNo == null ? null : other.belgeNo.copy();
        this.depoNo = other.depoNo == null ? null : other.depoNo.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public OrderMasterCriteria copy() { return new OrderMasterCriteria(this);}

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    public StringFilter getSiparisNo() {
        return siparisNo;
    }
    public void setSiparisNo(StringFilter siparisNo) {this.siparisNo = siparisNo;}

    public StringFilter getStokAdi() {return stokAdi;}

    public void setStokAdi(StringFilter stokAdi) {this.stokAdi = stokAdi;}

    public StringFilter getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(StringFilter stokKodu) {
        this.stokKodu = stokKodu;
    }

    public InstantFilter getLastModifiedDate() {return lastModifiedDate;}
    public void setLastModifiedDate(InstantFilter lastModifiedDate) {this.lastModifiedDate = lastModifiedDate;}

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getBelgeNo() {
        return belgeNo;
    }
    public void setBelgeNo(StringFilter belgeNo) {
        this.belgeNo = belgeNo;
    }

    public IntegerFilter getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(IntegerFilter depoNo) {
        this.depoNo = depoNo;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }
}
