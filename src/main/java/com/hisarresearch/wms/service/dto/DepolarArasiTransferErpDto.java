package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.service.dto.DepolarArasiTransferDetailDto;

import java.util.List;

public class DepolarArasiTransferErpDto {
    int girDepoNo;
    int cikDepoNo;
    String aciklama;
    String tarih;
    String erpUserCode;

    List<DepolarArasiTransferDetailDto> detailList;

    public int getGirDepoNo() {
        return girDepoNo;
    }

    public void setGirDepoNo(int girDepoNo) {
        this.girDepoNo = girDepoNo;
    }

    public int getCikDepoNo() {
        return cikDepoNo;
    }

    public void setCikDepoNo(int cikDepoNo) {
        this.cikDepoNo = cikDepoNo;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getErpUserCode() {
        return erpUserCode;
    }

    public void setErpUserCode(String erpUserCode) {
        this.erpUserCode = erpUserCode;
    }

    public List<DepolarArasiTransferDetailDto> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DepolarArasiTransferDetailDto> detailList) {
        this.detailList = detailList;
    }
}
