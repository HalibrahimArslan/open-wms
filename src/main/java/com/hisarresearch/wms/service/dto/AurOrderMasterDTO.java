package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;

import javax.annotation.Nullable;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class AurOrderMasterDTO {

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

    private List<AurOrderDetailDTO> aurTmpDetailList;

    @Nullable
    private Boolean complete;
    private Long sevkAddressId;
    private String sevkAddress;
    private String sevkTel;
    private String sevkMuhatap;
    private String sevkAcikAdres;
    private AurLookupTable transportationType;
    private AurLookupTable companyLogistics;
    private AurLookupTable carryType;
    private String bolgeKodu = "";
    private int orderDepoCode = 0;
    private AurDepoUrunAdres controlAddress;


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

    public List<AurOrderDetailDTO> getAurTmpDetailList() {
        return aurTmpDetailList;
    }

    public void setAurTmpDetailList(List<AurOrderDetailDTO> aurTmpDetailList) {
        this.aurTmpDetailList = aurTmpDetailList;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    @Nullable
    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(@Nullable Boolean complete) {
        this.complete = complete;
    }

    public Long getSevkAddressId() {
        return sevkAddressId;
    }

    public void setSevkAddressId(Long sevkAddressId) {
        this.sevkAddressId = sevkAddressId;
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

    public AurLookupTable getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(AurLookupTable transportationType) {
        this.transportationType = transportationType;
    }

    public AurLookupTable getCompanyLogistics() {
        return companyLogistics;
    }

    public void setCompanyLogistics(AurLookupTable companyLogistics) {
        this.companyLogistics = companyLogistics;
    }

    public AurLookupTable getCarryType() {
        return carryType;
    }

    public void setCarryType(AurLookupTable carryType) {
        this.carryType = carryType;
    }

    public String getBolgeKodu() {
        return bolgeKodu;
    }

    public void setBolgeKodu(String bolgeKodu) {
        this.bolgeKodu = bolgeKodu;
    }

    public int getOrderDepoCode() {
        return orderDepoCode;
    }

    public void setOrderDepoCode(int orderDepoCode) {
        this.orderDepoCode = orderDepoCode;
    }

    public List<String> getDistinctOrderNoList(){
        return this.aurTmpDetailList.stream().map(AurOrderDetailDTO::getSiparisNo).distinct().collect(Collectors.toList());
    }
    public List<String> getDistinctSipUidList(){
        return this.aurTmpDetailList.stream().map(AurOrderDetailDTO::getSipUid).distinct().collect(Collectors.toList());
    }

    public AurDepoUrunAdres getControlAddress() {
        return controlAddress;
    }

    public void setControlAddress(AurDepoUrunAdres controlAddress) {
        this.controlAddress = controlAddress;
    }
}
