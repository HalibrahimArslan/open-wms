package com.hisarresearch.wms.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "product_address_skt")
public class ProductAddressSkt extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productAddressSktGenerator")
    @SequenceGenerator(name = "productAddressSktGenerator",sequenceName = "product_address_skt_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    private ProductAddressv2 productAddress;

    @Column(name = "skt_date")
    private Instant sktDate;

    @Column(name = "quantity")
    private double quantity;

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

    public ProductAddressv2 getProductAddress() {
        return productAddress;
    }

    public void setProductAddress(ProductAddressv2 productAddress) {
        this.productAddress = productAddress;
    }

    public Instant getSktDate() {
        return sktDate;
    }

    public void setSktDate(Instant sktDate) {
        this.sktDate = sktDate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductAddressSkt that = (ProductAddressSkt) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
