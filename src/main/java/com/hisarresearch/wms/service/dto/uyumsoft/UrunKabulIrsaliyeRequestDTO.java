package com.hisarresearch.wms.service.dto.uyumsoft;

import java.util.List;

public class UrunKabulIrsaliyeRequestDTO {
    String belgeNo;
    String depoNo;
    String belgeTarih;
    String firmCode;
    String orderNo;
    String erpUserCode;
    Boolean fatura;

    List<IrsaliyeDetailRequestDTO> orderDetailList;

    public String getBelgeNo() {
        return belgeNo;
    }

    public void setBelgeNo(String belgeNo) {
        this.belgeNo = belgeNo;
    }

    public String getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(String depoNo) {
        this.depoNo = depoNo;
    }

    public String getBelgeTarih() {
        return belgeTarih;
    }

    public void setBelgeTarih(String belgeTarih) {
        this.belgeTarih = belgeTarih;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
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

    public List<IrsaliyeDetailRequestDTO> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<IrsaliyeDetailRequestDTO> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public Boolean getFatura() {
        return fatura;
    }

    public void setFatura(Boolean fatura) {
        this.fatura = fatura;
    }
}
