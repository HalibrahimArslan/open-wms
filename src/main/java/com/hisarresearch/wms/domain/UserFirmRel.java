package com.hisarresearch.wms.domain;

import java.io.Serializable;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserFirmRel.
 */
@Entity
@Table(name = "user_firm_rel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFirmRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userFirmRelGenerator")
    @SequenceGenerator(name = "userFirmRelGenerator", sequenceName = "user_firm_rel_seq", allocationSize = 1)
    private Long id;

    @Column(name = "firm_code")
    private String firmCode;

    @Column(name = "firm_name")
    private String firmName;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserFirmRel id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirmCode() {
        return this.firmCode;
    }

    public UserFirmRel firmCode(String firmCode) {
        this.firmCode = firmCode;
        return this;
    }

    public void setFirmCode(String firmCode) {
        this.firmCode = firmCode;
    }

    public String getFirmName() {
        return this.firmName;
    }

    public UserFirmRel firmName(String firmName) {
        this.firmName = firmName;
        return this;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserFirmRel)) {
            return false;
        }
        return id != null && id.equals(((UserFirmRel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserFirmRel{" +
            "id=" + getId() +
            ", firmCode='" + getFirmCode() + "'" +
            ", firmName='" + getFirmName() + "'" +
            "}";
    }
}
