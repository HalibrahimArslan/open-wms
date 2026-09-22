package com.hisarresearch.wms.service.dto;

import jakarta.persistence.*;

public class AurErpDataDto {

    private String sipGuid;
    private Short sipDepoNo;
    private Integer sipSubeNo;
    private Short sipTip;
    private Short sipCins;
    private String sipEvrakSeri;
    private Integer sipEvrakSira;
    private Integer sipSatirNo;
    private String sipBelgeno;
    private String sipMusteriKod;
    private String sipStokKod;
    private String stokAdi;
    private String barkod;
    private String stokBirimi;
    private Double sipMiktar;
    private Double sipTeslimMiktar;
    private String sipTarih;
    private String teslimTarihi;
    private String planlananSevkTarihi;
    private String cariKod;
    private String cariUnvan;
    private String bolgeKodu;
    private String bolgeAdi;
    private String cariHareketTipi;
    private String cariBaglantiTipi;

    public String getSipGuid() {
        return sipGuid;
    }

    public void setSipGuid(String sipGuid) {
        this.sipGuid = sipGuid;
    }

    public Short getSipDepoNo() {
        return sipDepoNo;
    }

    public void setSipDepoNo(Short sipDepoNo) {
        this.sipDepoNo = sipDepoNo;
    }

    public Integer getSipSubeNo() {
        return sipSubeNo;
    }

    public void setSipSubeNo(Integer sipSubeNo) {
        this.sipSubeNo = sipSubeNo;
    }

    public Short getSipTip() {
        return sipTip;
    }

    public void setSipTip(Short sipTip) {
        this.sipTip = sipTip;
    }

    public Short getSipCins() {
        return sipCins;
    }

    public void setSipCins(Short sipCins) {
        this.sipCins = sipCins;
    }

    public String getSipEvrakSeri() {
        return sipEvrakSeri;
    }

    public void setSipEvrakSeri(String sipEvrakSeri) {
        this.sipEvrakSeri = sipEvrakSeri;
    }

    public Integer getSipEvrakSira() {
        return sipEvrakSira;
    }

    public void setSipEvrakSira(Integer sipEvrakSira) {
        this.sipEvrakSira = sipEvrakSira;
    }

    public Integer getSipSatirNo() {
        return sipSatirNo;
    }

    public void setSipSatirNo(Integer sipSatirNo) {
        this.sipSatirNo = sipSatirNo;
    }

    public String getSipBelgeno() {
        return sipBelgeno;
    }

    public void setSipBelgeno(String sipBelgeno) {
        this.sipBelgeno = sipBelgeno;
    }

    public String getSipMusteriKod() {
        return sipMusteriKod;
    }

    public void setSipMusteriKod(String sipMusteriKod) {
        this.sipMusteriKod = sipMusteriKod;
    }

    public String getSipStokKod() {
        return sipStokKod;
    }

    public void setSipStokKod(String sipStokKod) {
        this.sipStokKod = sipStokKod;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
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

    public Double getSipMiktar() {
        return sipMiktar;
    }

    public void setSipMiktar(Double sipMiktar) {
        this.sipMiktar = sipMiktar;
    }

    public Double getSipTeslimMiktar() {
        return sipTeslimMiktar;
    }

    public void setSipTeslimMiktar(Double sipTeslimMiktar) {
        this.sipTeslimMiktar = sipTeslimMiktar;
    }

    public String getSipTarih() {
        return sipTarih;
    }

    public void setSipTarih(String sipTarih) {
        this.sipTarih = sipTarih;
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

    public String getCariHareketTipi() {
        return cariHareketTipi;
    }

    public void setCariHareketTipi(String cariHareketTipi) {
        this.cariHareketTipi = cariHareketTipi;
    }

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
    }
}
