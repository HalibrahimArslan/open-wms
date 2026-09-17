package com.hisarresearch.wms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AurCariOrderDetailListDto {
    private Integer id;
    private String orderNo;
    private String orderDate;
    private Integer orderLineItemCount;
    private String barkod;
    private String durum;
    private double teslimMiktar;
    private String sipUid;
    private double siparisMiktar;
    private String stokAdi;
    private String stokKodu;
    private String stokBirimi;
    private String teslimTarihi;
    private String planlananSevkTarihi;
    private int depoNo;
    private Integer addressNo;
    private Double stokMiktar;
    private String sevkAddress;
    private String sevkTel;
    private String sevkMuhatap;
    private String sevkAcikAdres;
    private String kullaniciAdi;
    private boolean aktif;
    private String sipDurum;
    private double sevkHazirMiktar;
    private String onayDurum;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderLineItemCount() {
        return orderLineItemCount;
    }

    public void setOrderLineItemCount(Integer orderLineItemCount) {
        this.orderLineItemCount = orderLineItemCount;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public double getTeslimMiktar() {
        return teslimMiktar;
    }

    public void setTeslimMiktar(double teslimMiktar) {
        this.teslimMiktar = teslimMiktar;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public double getSiparisMiktar() {
        return siparisMiktar;
    }

    public void setSiparisMiktar(double siparisMiktar) {
        this.siparisMiktar = siparisMiktar;
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

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    public void setTeslimTarihi(String teslimTarihi) {
        this.teslimTarihi = teslimTarihi;
    }

    public String getPlanlananSevkTarihi() {
        return planlananSevkTarihi;
    }

    public void setPlanlananSevkTarihi(String planlananSevkTarihi) {
        this.planlananSevkTarihi = planlananSevkTarihi;
    }

    public int getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(int depoNo) {
        this.depoNo = depoNo;
    }

    public Integer getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(Integer addressNo) {
        this.addressNo = addressNo;
    }

    public Double getStokMiktar() {
        return stokMiktar;
    }

    public void setStokMiktar(Double stokMiktar) {
        this.stokMiktar = stokMiktar;
    }

    public String getSevkAddress() {
        return sevkAddress;
    }

    public void setSevkAddress(String sevkAddress) {
        this.sevkAddress = sevkAddress;
    }

    public String getSevkTel() {
        return sevkTel;
    }

    public void setSevkTel(String sevkTel) {
        this.sevkTel = sevkTel;
    }

    public String getSevkMuhatap() {
        return sevkMuhatap;
    }

    public void setSevkMuhatap(String sevkMuhatap) {
        this.sevkMuhatap = sevkMuhatap;
    }

    public String getSevkAcikAdres() {
        return sevkAcikAdres;
    }

    public void setSevkAcikAdres(String sevkAcikAdres) {
        this.sevkAcikAdres = sevkAcikAdres;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSipDurum() {
        return sipDurum;
    }

    public void setSipDurum(String sipDurum) {
        this.sipDurum = sipDurum;
    }

    public double getSevkHazirMiktar() {
        return sevkHazirMiktar;
    }

    public void setSevkHazirMiktar(double sevkHazirMiktar) {
        this.sevkHazirMiktar = sevkHazirMiktar;
    }

    public String getOnayDurum() {
        return onayDurum;
    }

    public void setOnayDurum(String onayDurum) {
        this.onayDurum = onayDurum;
    }
}
