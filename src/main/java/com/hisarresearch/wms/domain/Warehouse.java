package com.hisarresearch.wms.domain;

import java.io.Serializable;
import jakarta.persistence.*;

import com.hisarresearch.wms.domain.enumeration.WarehousePickingRuleType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Depo.
 */
@Entity
@Table(name = "warehouse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouseGenerator")
    @SequenceGenerator(name = "warehouseGenerator",sequenceName = "warehouse_seq",allocationSize = 1)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "is_real")
    private Boolean isReal;

    @Column(name = "countable")
    private Boolean countable;

    @Column(name = "transfer_code")
    private String transferCode;

    @Column(name = "autoScan", nullable = false)
    private Boolean autoScan;

    @Column(name = "unique_picking_address", nullable = false)
    private Boolean uniquePickingAddress = false;

    @Enumerated(EnumType.STRING)
    private WarehousePickingRuleType pickingRuleType;

    @Column(name = "receiving_code", nullable = false)
    private String receivingCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Warehouse id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyCode() {
        return this.companyCode;
    }

    public Warehouse companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getReal() {
        return isReal;
    }

    public void setReal(Boolean real) {
        isReal = real;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = countable;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public Boolean getAutoScan() {return autoScan;}

    public void setAutoScan(Boolean autoScan) {this.autoScan = autoScan;}

    public Boolean getUniquePickingAddress() {
        return uniquePickingAddress;
    }

    public void setUniquePickingAddress(Boolean uniquePickingAddress) {
        this.uniquePickingAddress = uniquePickingAddress;
    }

    public WarehousePickingRuleType getPickingRuleType() {
        return pickingRuleType;
    }

    public void setPickingRuleType(WarehousePickingRuleType pickingRuleType) {
        this.pickingRuleType = pickingRuleType;
    }

    public String getReceivingCode() {
        return receivingCode;
    }

    public void setReceivingCode(String receivingCode) {
        this.receivingCode = receivingCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse)) {
            return false;
        }
        return id != null && id.equals(((Warehouse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Depo{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", name='" + getName() + "'" +
            ", companyCode=" + getCompanyCode() +
            "}";
    }
}
