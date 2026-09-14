package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurPartialItem.
 */
@Entity
@Table(name = "aur_partial_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurPartialItem extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurPartialItemGenerator")
    @SequenceGenerator(name = "aurPartialItemGenerator" , sequenceName = "aur_partial_item_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @NotNull
    @Column(name = "package_code", nullable = false)
    private String packageCode;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "package_barcode", unique = true)
    private String packageBarcode;

    @OneToMany(mappedBy = "aurPartialItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aurPartialItem" }, allowSetters = true)
    private Set<AurPartialDetails> aurPartialDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurPartialItem id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPackageCode() {
        return this.packageCode;
    }

    public AurPartialItem packageCode(String packageCode) {
        this.packageCode = packageCode;
        return this;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public AurPartialItem packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageBarcode() {
        return packageBarcode;
    }

    public void setPackageBarcode(String packageBarcode) {
        this.packageBarcode = packageBarcode;
    }

    public Set<AurPartialDetails> getAurPartialDetails() {
        return this.aurPartialDetails;
    }

    public AurPartialItem aurPartialDetails(Set<AurPartialDetails> aurPartialDetails) {
        this.setAurPartialDetails(aurPartialDetails);
        return this;
    }

    public AurPartialItem addAurPartialDetails(AurPartialDetails aurPartialDetails) {
        this.aurPartialDetails.add(aurPartialDetails);
        aurPartialDetails.setAurPartialItem(this);
        return this;
    }

    public AurPartialItem removeAurPartialDetails(AurPartialDetails aurPartialDetails) {
        this.aurPartialDetails.remove(aurPartialDetails);
        aurPartialDetails.setAurPartialItem(null);
        return this;
    }

    public void setAurPartialDetails(Set<AurPartialDetails> aurPartialDetails) {
        /*if (this.aurPartialDetails != null) {
            this.aurPartialDetails.forEach(i -> i.setAurPartialItem(null));
        }

        if (aurPartialDetails != null) {
            aurPartialDetails.forEach(i -> i.setAurPartialItem(this));
        }*/
        this.aurPartialDetails = aurPartialDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurPartialItem)) {
            return false;
        }
        return id != null && id.equals(((AurPartialItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurPartialItem{" +
            "id=" + getId() +
            ", packageCode='" + getPackageCode() + "'" +
            ", packageName='" + getPackageName() + "'" +
            "}";
    }
}
