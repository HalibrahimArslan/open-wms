package com.hisarresearch.wms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AurCariOrderDetailDto {
    private String barkod;
    private String durum;
    private double teslimMiktar;
    private String planlananSevkTarihi;
    private String sipUid;
    private double siparisMiktar;
    private String stokAdi;
    private String stokBirimi;
    private String stokKodu;
    private String teslimTarihi;
    private String onaylayanKullanici;
    private int depoNo;
    private int addressNo;
    private double stokMiktar;
    private String sevkAddress;
    private String sevkTel;
    private String sevkMuhatap;
    private String sevkAcikAdres;
    @JsonProperty("sipDurum")
    private String siparisDurum;
    private List<AurPartialResponseDto> partialList;
    private Boolean hasPiece = false;
    private double sevkHazirMiktar;
    private Boolean lotBasedTracking = false;
    private String kategoriAdi;
    private int birimIciAdet;
    private String anaGrupAdi;
    private String altGrupAdi;
    private double agirlik;
    private double genislik;
    private double derinlik;
    private double yukseklik;
    private double dara;


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

    public String getPlanlananSevkTarihi() {
        return planlananSevkTarihi;
    }

    public void setPlanlananSevkTarihi(String planlananSevkTarihi) {
        this.planlananSevkTarihi = planlananSevkTarihi;
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

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    public void setTeslimTarihi(String teslimTarihi) {
        this.teslimTarihi = teslimTarihi;
    }

    public String getOnaylayanKullanici() {
        return onaylayanKullanici;
    }

    public void setOnaylayanKullanici(String onaylayanKullanici) {
        this.onaylayanKullanici = onaylayanKullanici;
    }

    public List<AurPartialResponseDto> getPartialList() {
        return partialList;
    }

    public void setPartialList(List<AurPartialResponseDto> partialList) {
        this.partialList = partialList;
    }

    public Boolean getHasPiece() {
        return hasPiece;
    }

    public void setHasPiece(Boolean hasPiece) {
        this.hasPiece = hasPiece;
    }

    public int getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(int depoNo) {
        this.depoNo = depoNo;
    }

    public int getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(int addressNo) {
        this.addressNo = addressNo;
    }

    public double getStokMiktar() {
        return stokMiktar;
    }

    public void setStokMiktar(double stokMiktar) {
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

    public String getSiparisDurum() {
        return siparisDurum;
    }

    public void setSiparisDurum(String siparisDurum) {
        this.siparisDurum = siparisDurum;
    }

    public double getSevkHazirMiktar() {
        return sevkHazirMiktar;
    }

    public void setSevkHazirMiktar(double sevkHazirMiktar) {
        this.sevkHazirMiktar = sevkHazirMiktar;
    }

    public Boolean getLotBasedTracking() {
        return lotBasedTracking;
    }

    public void setLotBasedTracking(Boolean lotBasedTracking) {
        this.lotBasedTracking = lotBasedTracking;
    }

    public String getKategoriAdi() {
        return kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public int getBirimIciAdet() {
        return birimIciAdet;
    }

    public void setBirimIciAdet(int birimIciAdet) {
        this.birimIciAdet = birimIciAdet;
    }

    public String getAnaGrupAdi() {
        return anaGrupAdi;
    }

    public void setAnaGrupAdi(String anaGrupAdi) {
        this.anaGrupAdi = anaGrupAdi;
    }

    public String getAltGrupAdi() {
        return altGrupAdi;
    }

    public void setAltGrupAdi(String altGrupAdi) {
        this.altGrupAdi = altGrupAdi;
    }

    public void setAgirlik(double agirlik) {
        this.agirlik = agirlik;
    }

    public void setGenislik(double genislik) {
        this.genislik = genislik;
    }

    public void setDerinlik(double derinlik) {
        this.derinlik = derinlik;
    }

    public void setYukseklik(double yukseklik) {
        this.yukseklik = yukseklik;
    }

    public void setDara(double dara) {
        this.dara = dara;
    }

    public Map<String, Object> getPhysicalAttributes() {
        Map<String, Object> physicalAttributes = new LinkedHashMap<>();
        physicalAttributes.put("agirlik", agirlik);
        physicalAttributes.put("genislik", genislik);
        physicalAttributes.put("derinlik", derinlik);
        physicalAttributes.put("yukseklik", yukseklik);
        physicalAttributes.put("dara", dara);
        return physicalAttributes;
    }
}
