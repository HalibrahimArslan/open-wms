package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.FirmConnectionType;
import com.hisarresearch.wms.domain.enumeration.FirmConnectionTypeConverter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerGenerator")
    @SequenceGenerator(name = "customerGenerator", sequenceName = "customer_seq", allocationSize = 1)
    private Long id;

    @Column(name = "customer_code",length = 25,nullable = false)
    private String customerCode;

    @Column(name = "district_code",nullable = false)
    private int districtCode;

    @Column(name = "mail",length = 100,nullable = false)
    private String mail;

    @Convert(converter = FirmConnectionTypeConverter.class)
    @Column(name = "connection_type", nullable = true)
    private FirmConnectionType connectionType;

    @Column(name = "customer_name",nullable = true)
    private String customerName;

    @Column(name = "district_name",nullable = true)
    private String districtName;

    @Column(name = "company_code",nullable = true)
    private String companyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public FirmConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(FirmConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
