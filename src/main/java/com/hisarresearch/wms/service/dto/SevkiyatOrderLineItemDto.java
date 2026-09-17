package com.hisarresearch.wms.service.dto;

public class SevkiyatOrderLineItemDto {

    private String stokKodu;
    private Double kabulMiktar;
    private String sipUid;
    private String sth_paket_kod;

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

    public String getSth_paket_kod() {
        return sth_paket_kod;
    }

    public void setSth_paket_kod(String sth_paket_kod) {
        this.sth_paket_kod = sth_paket_kod;
    }
}
