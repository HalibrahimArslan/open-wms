package com.hisarresearch.wms.service.dto;

public class DispatchingOrderLineItemDto {

    private String stokKodu;
    private Double kabulMiktar;
    private String sipUid;
    private String sth_parti_kod;

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public Double getKabulMiktar() {
        return kabulMiktar;
    }

    public void setKabulMiktar(Double kabulMiktar) {
        this.kabulMiktar = kabulMiktar;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public String getSth_parti_kod() {
        return sth_parti_kod;
    }

    public void setSth_parti_kod(String sth_parti_kod) {
        this.sth_parti_kod = sth_parti_kod;
    }
}
