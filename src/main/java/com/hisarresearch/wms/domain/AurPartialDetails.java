package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurPartialDetails.
 */
@Entity
@Table(name = "aur_partial_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurPartialDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurPartialDetailsGenerator")
    @SequenceGenerator(name = "aurPartialDetailsGenerator" , sequenceName = "aur_partial_details_seq",allocationSize = 1)
    private Long id;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "stock_name")
    private String stockName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "aurPartialDetails" }, allowSetters = true)
    private AurPartialItem aurPartialItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurPartialDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getStockCode() {
        return this.stockCode;
    }

    public AurPartialDetails stockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public AurPartialDetails quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public AurPartialItem getAurPartialItem() {
        return this.aurPartialItem;
    }

    public AurPartialDetails aurPartialItem(AurPartialItem aurPartialItem) {
        this.setAurPartialItem(aurPartialItem);
        return this;
    }

    public void setAurPartialItem(AurPartialItem aurPartialItem) {
        this.aurPartialItem = aurPartialItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurPartialDetails)) {
            return false;
        }
        return id != null && id.equals(((AurPartialDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurPartialDetails{" +
            "id=" + getId() +
            ", stockCode='" + getStockCode() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
