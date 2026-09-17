package com.hisarresearch.wms.service.dto;


import com.hisarresearch.wms.domain.enumeration.AurTmpSevkPlaniStatus;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class AurTmpSevkPlaniDTO {

    private Long id;
    private AurTmpSevkPlaniStatus status;
    private int week;
    private int year;
    private String cariKod;
    private String cariUnvan;
    private String cariBaglantiTipi;
    private String bolgeKodu;
    private String bolgeAdi;
    private String sipUid;
    private Integer depoNo;
    private String evrakSeri;
    private Integer evrakSira;
    private String transGroupCode;
    private String transGroupName;
    private String onaylayanKullanici;
    private String durum;
    private Double siparisMiktar;
    private Double teslimMiktar;
    private Long addressNo;
    private String barkod;
    private String stokAdi;
    private String stokKodu;
    private String stokBirimi;
    private Double stokMiktar;
    private String sevkAddress;
    private String sevkTel;
    private String sevkMuhatap;
    private String sevkAcikAddress;
    private int version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurTmpSevkPlaniStatus getStatus() {
        return status;
    }

    public void setStatus(AurTmpSevkPlaniStatus status) {
        this.status = status;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public String getCariUnvan() {
        return cariUnvan;
    }

    public void setCariUnvan(String cariUnvan) {
        this.cariUnvan = cariUnvan;
    }

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
    }

    public String getBolgeKodu() {
        return bolgeKodu;
    }

    public void setBolgeKodu(String bolgeKodu) {
        this.bolgeKodu = bolgeKodu;
    }

    public String getBolgeAdi() {
        return bolgeAdi;
    }

    public void setBolgeAdi(String bolgeAdi) {
        this.bolgeAdi = bolgeAdi;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getEvrakSeri() {
        return evrakSeri;
    }

    public void setEvrakSeri(String evrakSeri) {
        this.evrakSeri = evrakSeri;
    }

    public Integer getEvrakSira() {
        return evrakSira;
    }

    public void setEvrakSira(Integer evrakSira) {
        this.evrakSira = evrakSira;
    }

    public String getTransGroupCode() {
        return transGroupCode;
    }

    public void setTransGroupCode(String transGroupCode) {
        this.transGroupCode = transGroupCode;
    }

    public String getTransGroupName() {
        return transGroupName;
    }

    public void setTransGroupName(String transGroupName) {
        this.transGroupName = transGroupName;
    }

    public String getOnaylayanKullanici() {
        return onaylayanKullanici;
    }

    public void setOnaylayanKullanici(String onaylayanKullanici) {
        this.onaylayanKullanici = onaylayanKullanici;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public Double getSiparisMiktar() {
        return siparisMiktar;
    }

    public void setSiparisMiktar(Double siparisMiktar) {
        this.siparisMiktar = siparisMiktar;
    }

    public Double getTeslimMiktar() {
        return teslimMiktar;
    }

    public void setTeslimMiktar(Double teslimMiktar) {
        this.teslimMiktar = teslimMiktar;
    }

    public Long getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(Long addressNo) {
        this.addressNo = addressNo;
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

    public String getSevkAcikAddress() {
        return sevkAcikAddress;
    }

    public void setSevkAcikAddress(String sevkAcikAddress) {
        this.sevkAcikAddress = sevkAcikAddress;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
