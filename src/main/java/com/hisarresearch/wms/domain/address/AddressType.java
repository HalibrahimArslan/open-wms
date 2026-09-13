package com.hisarresearch.wms.domain.address;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "aur_adres_tip")
public class AddressType implements Serializable,AddressComponent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "code", nullable = false, length = 25)
    private String code;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "company_code",nullable = false)
    private String companyCode;

    @Column(name = "depo_code",nullable = false)
    private String depoCode;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
