package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.domain.AurUser;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class AurOrderMasterQueryDTO implements Serializable {
    private Long id;
    private AurUser aurUser;
    private String orderInfo;
    private String status;
    private Integer depoNo;
    private String firmCode;
    private String createdBy;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private String lastModifiedBy;
    private String opType;
    private String belgeNo;
    private Long observedUserId;
    private String soforAdi;
    private String soforTel;
    private String soforPlaka;
    private String soforTcNo;
    private String firmName;
    private String baglantiTipi;
    private String cariCode;
    private Long sevkAddressId;
    private AurLookupTable transportationType;
    private AurLookupTable companyLogistics;
    private AurLookupTable carryType;
    private String bolgeKodu;
    private Boolean assignedOrder;
    private int orderDepoCode;
    private List<AurOrderDetailDTO> details;
    private CustomerAddressDTO customerAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurUser getAurUser() {
        return aurUser;
    }

    public void setAurUser(AurUser aurUser) {
        this.aurUser = aurUser;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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

    public Long getObservedUserId() {
        return observedUserId;
    }

    public void setObservedUserId(Long observedUserId) {
        this.observedUserId = observedUserId;
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

    public String getBaglantiTipi() {
        return baglantiTipi;
    }

    public void setBaglantiTipi(String baglantiTipi) {
        this.baglantiTipi = baglantiTipi;
    }

    public String getCariCode() {
        return cariCode;
    }

    public void setCariCode(String cariCode) {
        this.cariCode = cariCode;
    }

    public Long getSevkAddressId() {
        return sevkAddressId;
    }

    public void setSevkAddressId(Long sevkAddressId) {
        this.sevkAddressId = sevkAddressId;
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

    public Boolean getAssignedOrder() {
        return assignedOrder;
    }

    public void setAssignedOrder(Boolean assignedOrder) {
        this.assignedOrder = assignedOrder;
    }

    public int getOrderDepoCode() {
        return orderDepoCode;
    }

    public void setOrderDepoCode(int orderDepoCode) {
        this.orderDepoCode = orderDepoCode;
    }

    public List<AurOrderDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<AurOrderDetailDTO> details) {
        this.details = details;
    }

    public CustomerAddressDTO getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddressDTO customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AurOrderMasterQueryDTO)) return false;
        AurOrderMasterQueryDTO that = (AurOrderMasterQueryDTO) o;
        return orderDepoCode == that.orderDepoCode && Objects.equals(id, that.id) && Objects.equals(aurUser, that.aurUser) && Objects.equals(orderInfo, that.orderInfo) && Objects.equals(status, that.status) && Objects.equals(depoNo, that.depoNo) && Objects.equals(firmCode, that.firmCode) && Objects.equals(createdBy, that.createdBy) && Objects.equals(createdDate, that.createdDate) && Objects.equals(lastModifiedDate, that.lastModifiedDate) && Objects.equals(lastModifiedBy, that.lastModifiedBy) && Objects.equals(opType, that.opType) && Objects.equals(belgeNo, that.belgeNo) && Objects.equals(observedUserId, that.observedUserId) && Objects.equals(soforAdi, that.soforAdi) && Objects.equals(soforTel, that.soforTel) && Objects.equals(soforPlaka, that.soforPlaka) && Objects.equals(soforTcNo, that.soforTcNo) && Objects.equals(firmName, that.firmName) && Objects.equals(baglantiTipi, that.baglantiTipi) && Objects.equals(cariCode, that.cariCode) && Objects.equals(sevkAddressId, that.sevkAddressId) && Objects.equals(transportationType, that.transportationType) && Objects.equals(companyLogistics, that.companyLogistics) && Objects.equals(carryType, that.carryType) && Objects.equals(bolgeKodu, that.bolgeKodu) && Objects.equals(assignedOrder, that.assignedOrder) && Objects.equals(details, that.details) && Objects.equals(customerAddress, that.customerAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aurUser, orderInfo, status, depoNo, firmCode, createdBy, createdDate, lastModifiedDate, lastModifiedBy, opType, belgeNo, observedUserId, soforAdi, soforTel, soforPlaka, soforTcNo, firmName, baglantiTipi, cariCode, sevkAddressId, transportationType, companyLogistics, carryType, bolgeKodu, assignedOrder, orderDepoCode, details, customerAddress);
    }
}
