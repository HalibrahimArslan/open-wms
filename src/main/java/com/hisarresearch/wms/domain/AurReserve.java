package com.hisarresearch.wms.domain;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "aur_reserve")
public class AurReserve extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurReserveGenerator")
    @SequenceGenerator(name = "aurReserveGenerator", sequenceName = "aur_reserve_seq", allocationSize = 1)
    private Long id;

    @Column(name = "order_no", nullable = false, length = 25)
    private String orderNo;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 25)
    private AurReserveStatus status;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "barcode", referencedColumnName = "barkod"),
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")
    })
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AurReserveStatus getStatus() {
        return status;
    }

    public void setStatus(AurReserveStatus status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
