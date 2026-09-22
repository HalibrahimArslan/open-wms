package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

/**
 * A AurCompany.
 */
@Entity
@Table(name = "aur_company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurCompanyGenerator")
    @SequenceGenerator(name = "aurCompanyGenerator",sequenceName = "aur_company_seq", allocationSize = 1)
    private Long id;

    @Column(name = "company_code")
    private Integer companyCode;

    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "erp_type")
    private ErpConnectionType erpType;

    @Column(name = "api_endpoint")
    private String apiEndPoint;

    @Type(JsonType.class)
    @Column(name = "api_parameters")
    private ApiParameters apiParameters;

    public ErpConnectionType getErpType() {
        return erpType;
    }

    public void setErpType(ErpConnectionType erpType) {
        this.erpType = erpType;
    }

    public String getApiEndPoint() {
        return apiEndPoint;
    }

    public void setApiEndPoint(String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
    }

    public ApiParameters getApiParameters() {
        return apiParameters;
    }

    public void setApiParameters(ApiParameters apiParameters) {
        this.apiParameters = apiParameters;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurCompany id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCompanyCode() {
        return this.companyCode;
    }

    public AurCompany companyCode(Integer companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public AurCompany companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurCompany)) {
            return false;
        }
        return id != null && id.equals(((AurCompany) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurCompany{" +
            "id=" + getId() +
            ", companyCode=" + getCompanyCode() +
            ", companyName='" + getCompanyName() + "'" +
            "erpType= "+getErpType()+
            "apiEndPoint"+getApiEndPoint()+
            "apiParameters"+getApiParameters()+
            "}";
    }
}
