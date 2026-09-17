package com.hisarresearch.wms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AurWaybillDto {

    private String evrakSeri;
    private Integer evrakSira;
    private String siparisNo;
    private String cariKod;
    private String cariUnvan;
    private Integer evrakTip;
    private String tarih;
    private String kullanici;
    private String kaynak;

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

    public String getSiparisNo() {
        return siparisNo;
    }

    public void setSiparisNo(String siparisNo) {
        this.siparisNo = siparisNo;
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

    public Integer getEvrakTip() {
        return evrakTip;
    }

    public void setEvrakTip(Integer evrakTip) {
        this.evrakTip = evrakTip;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getKullanici() {
        return kullanici;
    }

    public void setKullanici(String kullanici) {
        this.kullanici = kullanici;
    }

    public String getKaynak() {
        return kaynak;
    }

    public void setKaynak(String kaynak) {
        this.kaynak = kaynak;
    }
}
