package com.hisarresearch.wms.service.dto.uyumsoft;

public class IrsaliyeDetailRequestDTO {
    private String stokKodu;
    private Double hareketMiktar;
    private String sipUid;

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public Double getHareketMiktar() {
        return hareketMiktar;
    }

    public void setHareketMiktar(Double hareketMiktar) {
        this.hareketMiktar = hareketMiktar;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }
}
