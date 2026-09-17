package com.hisarresearch.wms.service.dto.uyumsoft;

public class OrderDetailDTO {
    private int id;
    private Double siparisMiktar;
    private String durum;
    private Double teslimMiktar;
    private String stokAdi;
    private String stokKodu;
    private String barkod;
    private String stokBirimi;
    private String sipUid;
    private String teslimTarihi;
    private Double stokMiktar;
    private String orderNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getSiparisMiktar() {
        return siparisMiktar;
    }

    public void setSiparisMiktar(Double siparisMiktar) {
        this.siparisMiktar = siparisMiktar;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public Double getTeslimMiktar() {
        return teslimMiktar;
    }

    public void setTeslimMiktar(Double teslimMiktar) {
        this.teslimMiktar = teslimMiktar;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    public void setTeslimTarihi(String teslimTarihi) {
        this.teslimTarihi = teslimTarihi;
    }

    public Double getStokMiktar() {
        return stokMiktar;
    }

    public void setStokMiktar(Double stokMiktar) {
        this.stokMiktar = stokMiktar;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
