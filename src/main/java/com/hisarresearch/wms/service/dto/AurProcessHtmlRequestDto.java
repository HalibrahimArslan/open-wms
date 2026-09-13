package com.hisarresearch.wms.service.dto;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class AurProcessHtmlRequestDto {

    private int id;
    private Instant irsaliyeTarihi;
    private String irsaliyeNumarasi;
    private String kayitNo;
    private String depo;
    private String sipTip;
    private String siparisNumarasi;
    private String cariKod;
    private String cariAdi;
    private String sipUid;
    private String musteriTel;
    private String altCariAdi;
    private String stokKodu;
    private List<String> stokMailGrup;
    private String stokAdi;
    private String siparisMiktari;
    private String teslimMiktari;
    private String kalanMiktar;
    private String sevkiyatAdresi;
    private String irtibatTel;
    private String soforAdSoyad = "";
    private String soforPlaka;
    private String soforTc;
    private String stokBirimi;

    private String atanan;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getIrsaliyeTarihi() {
        return irsaliyeTarihi;
    }

    public void setIrsaliyeTarihi(Instant irsaliyeTarihi) {
        this.irsaliyeTarihi = irsaliyeTarihi;
    }

    public String getIrsaliyeNumarasi() {
        return irsaliyeNumarasi;
    }

    public void setIrsaliyeNumarasi(String irsaliyeNumarasi) {
        this.irsaliyeNumarasi = irsaliyeNumarasi;
    }

    public String getKayitNo() {
        return kayitNo;
    }

    public void setKayitNo(String kayitNo) {
        this.kayitNo = kayitNo;
    }

    public String getDepo() {
        return depo;
    }

    public void setDepo(String depo) {
        this.depo = depo;
    }

    public String getSipTip() {
        return sipTip;
    }

    public void setSipTip(String sipTip) {
        this.sipTip = sipTip;
    }

    public String getSiparisNumarasi() {
        return siparisNumarasi;
    }

    public void setSiparisNumarasi(String siparisNumarasi) {
        this.siparisNumarasi = siparisNumarasi;
    }

    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public String getCariAdi() {
        return cariAdi;
    }

    public void setCariAdi(String cariAdi) {
        this.cariAdi = cariAdi;
    }

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public String getAltCariAdi() {
        return altCariAdi;
    }

    public void setAltCariAdi(String altCariAdi) {
        this.altCariAdi = altCariAdi;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public List<String> getStokMailGrup() {
        return stokMailGrup;
    }

    public void setStokMailGrup(List<String> stokMailGrup) {
        this.stokMailGrup = stokMailGrup;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getSiparisMiktari() {
        return siparisMiktari;
    }

    public void setSiparisMiktari(String siparisMiktari) {
        this.siparisMiktari = siparisMiktari;
    }

    public String getTeslimMiktari() {
        return teslimMiktari;
    }

    public void setTeslimMiktari(String teslimMiktari) {
        this.teslimMiktari = teslimMiktari;
    }

    public String getKalanMiktar() {
        return kalanMiktar;
    }

    public void setKalanMiktar(String kalanMiktar) {
        this.kalanMiktar = kalanMiktar;
    }

    public String getSevkiyatAdresi() {
        return sevkiyatAdresi;
    }

    public void setSevkiyatAdresi(String sevkiyatAdresi) {
        this.sevkiyatAdresi = sevkiyatAdresi;
    }

    public String getIrtibatTel() {
        return irtibatTel;
    }

    public void setIrtibatTel(String irtibatTel) {
        this.irtibatTel = irtibatTel;
    }

    public String getSoforAdSoyad() {
        return soforAdSoyad;
    }

    public void setSoforAdSoyad(String soforAdSoyad) {
        this.soforAdSoyad = soforAdSoyad;
    }

    public String getSoforPlaka() {
        return soforPlaka;
    }

    public void setSoforPlaka(String soforPlaka) {
        this.soforPlaka = soforPlaka;
    }

    public String getSoforTc() {
        return soforTc;
    }

    public void setSoforTc(String soforTc) {
        this.soforTc = soforTc;
    }

    public String getMusteriTel() {
        return musteriTel;
    }

    public void setMusteriTel(String musteriTel) {
        this.musteriTel = musteriTel;
    }

    public String getAtanan() {
        return atanan;
    }

    public void setAtanan(String atanan) {
        this.atanan = atanan;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    @Override
    public String toString() {
        return "AurProcessHtmlRequestDto{" +
            "irsaliyeTarihi=" + irsaliyeTarihi +
            ", irsaliyeNumarasi='" + irsaliyeNumarasi + '\'' +
            ", kayitNo='" + kayitNo + '\'' +
            ", depo='" + depo + '\'' +
            ", sipTip='" + sipTip + '\'' +
            ", siparisNumarasi='" + siparisNumarasi + '\'' +
            ", cariKod='" + cariKod + '\'' +
            ", cariAdi='" + cariAdi + '\'' +
            ", stokKodu='" + stokKodu + '\'' +
            ", stokMailGrup=" + stokMailGrup +
            ", stokAdi='" + stokAdi + '\'' +
            ", siparisMiktari='" + siparisMiktari + '\'' +
            ", teslimMiktari='" + teslimMiktari + '\'' +
            ", kalanMiktar='" + kalanMiktar + '\'' +
            ", sevkiyatAdresi='" + sevkiyatAdresi + '\'' +
            ", irtibatTel='" + irtibatTel + '\'' +
            ", soforAdSoyad='" + soforAdSoyad + '\'' +
            ", soforPlaka='" + soforPlaka + '\'' +
            ", soforTc='" + soforTc + '\'' +
            '}';
    }
}
