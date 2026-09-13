package com.hisarresearch.wms.service.dto;

import java.time.Instant;
import java.util.List;

public class AurOrderDetailWithPartialDTO {
    private Long id;
    private Long aurUserId;
    private String orderInfo;
    private String status;
    private Integer depoNo;
    private String firmCode;
    private String opType;
    private String belgeNo;
    private String soforAdi;
    private String soforTel;
    private String soforPlaka;
    private String soforTcNo;
    private String firmName;
    private Instant createdDate;
    private String cariBaglantiTipi;
    private String cariCode;
    private String addressId;
    private List<PartialDetailDto> aurTmpDetailList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAurUserId() {
        return aurUserId;
    }

    public void setAurUserId(Long aurUserId) {
        this.aurUserId = aurUserId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getFirmCode() {
        return firmCode;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
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

    public String getSoforTel() {
        return soforTel;
    }

    public void setSoforTel(String soforTel) {
        this.soforTel = soforTel;
    }

    public String getSoforPlaka() {
        return soforPlaka;
    }

    public void setSoforPlaka(String soforPlaka) {
        this.soforPlaka = soforPlaka;
    }

    public String getSoforTcNo() {
        return soforTcNo;
    }

    public void setSoforTcNo(String soforTcNo) {
        this.soforTcNo = soforTcNo;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
    }

    public String getCariCode() {
        return cariCode;
    }

    public void setCariCode(String cariCode) {
        this.cariCode = cariCode;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<PartialDetailDto> getAurTmpDetailList() {
        return aurTmpDetailList;
    }

    public void setAurTmpDetailList(List<PartialDetailDto> aurTmpDetailList) {
        this.aurTmpDetailList = aurTmpDetailList;
    }
}
