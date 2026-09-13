package com.hisarresearch.wms.service.dto.uyumsoft;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SevkiyatIrsaliyeRequestDTO {
    String depoNo;
    String tarih;
    String firmCode;
    String cariKod;
    String orderNo;
    String erpUserCode;
    String belgeNo;
    String soforAdi;
    String soforSoyadi;
    String soforTckn;
    String aracPlakaNo;
    String dorsePlakaNo;
    String aciklama;
    @NotNull
    String hareketKod;
    boolean proforma = false;

    String token;
    List<IrsaliyeDetailRequestDTO> orderDetailList;

    public String getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(String depoNo) {
        this.depoNo = depoNo;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getErpUserCode() {
        return erpUserCode;
    }

    public void setErpUserCode(String erpUserCode) {
        this.erpUserCode = erpUserCode;
    }

    public String getBelgeNo() {
        return belgeNo;
    }

    public void setBelgeNo(String belgeNo) {
        this.belgeNo = belgeNo;
    }

    public String getSoforAdi() {
        return soforAdi;
    }

    public void setSoforAdi(String soforAdi) {
        this.soforAdi = soforAdi;
    }

    public String getSoforSoyadi() {
        return soforSoyadi;
    }

    public void setSoforSoyadi(String soforSoyadi) {
        this.soforSoyadi = soforSoyadi;
    }

    public String getSoforTckn() {
        return soforTckn;
    }

    public void setSoforTckn(String soforTckn) {
        this.soforTckn = soforTckn;
    }

    public String getAracPlakaNo() {
        return aracPlakaNo;
    }

    public void setAracPlakaNo(String aracPlakaNo) {
        this.aracPlakaNo = aracPlakaNo;
    }

    public String getDorsePlakaNo() {
        return dorsePlakaNo;
    }

    public void setDorsePlakaNo(String dorsePlakaNo) {
        this.dorsePlakaNo = dorsePlakaNo;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public boolean isProforma() {
        return proforma;
    }

    public void setProforma(boolean proforma) {
        this.proforma = proforma;
    }

    public @NotNull String getHareketKod() {
        return hareketKod;
    }

    public void setHareketKod(@NotNull String hareketKod) {
        this.hareketKod = hareketKod;
    }

    public List<IrsaliyeDetailRequestDTO> getOrderDetailList() {
        return orderDetailList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOrderDetailList(List<IrsaliyeDetailRequestDTO> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
