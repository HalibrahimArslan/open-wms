package com.hisarresearch.wms.service.dto;

public class OrderLineItemDto {

    private String stokKodu;
    private Double kabulMiktar;
    private String sipUid;
    private String barkod;
    private String stokAdi;
    private String isReserve;
    private String reserveNo;
    private String description;



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

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getIsReserve() {
        return isReserve;
    }

    public void setIsReserve(String isReserve) {
        this.isReserve = isReserve;
    }

    public String getReserveNo() {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo) {
        this.reserveNo = reserveNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
