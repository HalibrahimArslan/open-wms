package com.hisarresearch.wms.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurRole.
 */
@Entity
@Table(name = "aur_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AurRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurRoleGenerator")
    @SequenceGenerator(name = "aurRoleGenerator",sequenceName = "aur_role_seq",allocationSize = 1)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "company_code")
    private Integer companyCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurRole id(Long id) {
        this.id = id;
        return this;
    }


    public String getRoleName() {
        return this.roleName;
    }

    public AurRole roleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getCompanyCode() {
        return this.companyCode;
    }

    public AurRole companyCode(Integer companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurRole)) {
            return false;
        }
        return id != null && id.equals(((AurRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurRole{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", companyCode=" + getCompanyCode() +
            "}";
    }
}
