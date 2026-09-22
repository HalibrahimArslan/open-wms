package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name = "aur_order_master")
public class AurOrderMaster extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurOrderMasterGenerator")
    @SequenceGenerator(name = "aurOrderMasterGenerator",sequenceName = "aur_order_master_seq",allocationSize = 1)
    private Long id;

    @ManyToOne
    private AurUser aurUser;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "status")
    private String status;

    @Column(name = "depo_no")
    private Integer depoNo;

    @Column(name = "firm_code")
    private String firmCode;

    @Column(name = "op_type")
    private String opType;

    @Column(name = "belgeNo")
    private String belgeNo;

    @Column(name = "observed_user_id")
    private Long observedUserId;

    @Column(name = "sofor_adi")
    private String soforAdi;

    @Column(name = "sofor_tel")
    private String soforTel;

    @Column(name = "sofor_plaka")
    private String soforPlaka;

    @Column(name = "sofor_tc_no")
    private String soforTcNo;

    @Column(name = "firm_name")
    private String firmName;

    @Column(name = "baglanti_tipi")
    private String baglantiTipi;

    @Column(name = "cari_code")
    private String cariCode;

    @Column(name = "sevk_address_id")
    private Long sevkAddressId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "cari_code", referencedColumnName = "cari_code", insertable = false, updatable = false),
        @JoinColumn(name = "sevk_address_id", referencedColumnName = "address_id", insertable = false, updatable = false)
    })
    private CustomerAddress customerAddress;

    @ManyToOne
    @JoinColumn(name = "transportation_id",referencedColumnName = "id")
    @Nullable
    private AurLookupTable transportationType;

    @ManyToOne
    @JoinColumn(name = "company_logistic_id",referencedColumnName = "id")
    @Nullable
    private AurLookupTable companyLogistics;

    @ManyToOne
    @JoinColumn(name = "carry_id",referencedColumnName = "id")
    @Nullable
    private AurLookupTable carryType;

    @Column(name = "bolge_kodu")
    private String bolgeKodu;

    @Column(name = "assigned_order")
    private Boolean assignedOrder = true;

    @Column(name = "order_depo_code")
    private int orderDepoCode;

    @ManyToOne
    @JoinColumn(name = "control_address_id", referencedColumnName = "id")
    private AurDepoUrunAdres controlAddress;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<AurOrderDetail> details = new HashSet<>();

    public AurOrderMaster() {
    }

    public AurOrderMaster(Long id) {
        this.id = id;
    }

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

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Nullable
    public AurLookupTable getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(@Nullable AurLookupTable transportationType) {
        this.transportationType = transportationType;
    }

    @Nullable
    public AurLookupTable getCompanyLogistics() {
        return companyLogistics;
    }

    public void setCompanyLogistics(@Nullable AurLookupTable companyLogistics) {
        this.companyLogistics = companyLogistics;
    }

    @Nullable
    public AurLookupTable getCarryType() {
        return carryType;
    }

    public void setCarryType(@Nullable AurLookupTable carryType) {
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

    public AurDepoUrunAdres getControlAddress() {
        return controlAddress;
    }

    public void setControlAddress(AurDepoUrunAdres controlAddress) {
        this.controlAddress = controlAddress;
    }

    public Set<AurOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<AurOrderDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AurOrderMaster{" +
            "id=" + id +
            ", aurUser=" + aurUser +
            ", orderInfo='" + orderInfo + '\'' +
            ", status='" + status + '\'' +
            ", depoNo=" + depoNo +
            ", firmCode='" + firmCode + '\'' +
            ", opType='" + opType + '\'' +
            ", belgeNo='" + belgeNo + '\'' +
            ", observedUserId=" + observedUserId +
            ", soforAdi='" + soforAdi + '\'' +
            ", soforTel='" + soforTel + '\'' +
            ", soforPlaka='" + soforPlaka + '\'' +
            ", soforTcNo='" + soforTcNo + '\'' +
            ", firmName='" + firmName + '\'' +
            ", baglantiTipi='" + baglantiTipi + '\'' +
            ", cariCode='" + cariCode + '\'' +
            ", sevkAddressId=" + sevkAddressId +
            ", customerAddress=" + customerAddress +
            ", transportationType=" + transportationType +
            ", companyLogistics=" + companyLogistics +
            ", carryType=" + carryType +
            ", bolgeKodu='" + bolgeKodu + '\'' +
            ", assignedOrder=" + assignedOrder +
            ", orderDepoCode=" + orderDepoCode +
            '}';
    }

    @Override
    public AurOrderMaster clone() throws CloneNotSupportedException {
        AurOrderMaster clone = (AurOrderMaster) super.clone();
        clone.setId(null);
        clone.setDetails(new HashSet<>());
        return clone;
    }
}
