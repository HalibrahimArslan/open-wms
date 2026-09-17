package com.hisarresearch.wms.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderStatus.
 */
@Entity
@Table(name = "order_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderStatusGenerator")
    @SequenceGenerator(name = "orderStatusGenerator",sequenceName = "order_status_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus id(Long id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public OrderStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public OrderStatus description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderStatus)) {
            return false;
        }
        return id != null && id.equals(((OrderStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
