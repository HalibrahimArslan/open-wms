package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.security.ApiPasswordCipher;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@TypeDefs({ @TypeDef(name = "json", typeClass = JsonStringType.class), @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) })
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

    @Type(type = "jsonb")
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

    // ERP sifresi DB'de duz metin durmasin diye yazilmadan once sifrelenir,
    // okunduktan sonra cozulur; boylece uygulamanin geri kalani (ErpTokenService
    // dahil) her zaman duz metinle calisir, DB'de ise sadece sifreli hali bulunur.
    @PrePersist
    @PreUpdate
    private void encryptApiPassword() {
        if (apiParameters != null) {
            apiParameters.setPassword(ApiPasswordCipher.encrypt(apiParameters.getPassword()));
        }
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void decryptApiPassword() {
        if (apiParameters != null) {
            apiParameters.setPassword(ApiPasswordCipher.decrypt(apiParameters.getPassword()));
        }
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
