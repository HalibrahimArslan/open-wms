package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovy.transform.AutoClone;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "aur_tmp_detail_skt")
@AutoClone
public class AurOrderDetailSkt implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurTmpDetailSktGenerator")
    @SequenceGenerator(name = "aurTmpDetailSktGenerator",sequenceName = "aur_tmp_detail_skt_seq",allocationSize = 1)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private AurOrderDetail aurTmpDetail;

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

    public AurOrderDetail getAurTmpDetail() {
        return aurTmpDetail;
    }

    public void setAurTmpDetail(AurOrderDetail aurTmpDetail) {
        this.aurTmpDetail = aurTmpDetail;
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
}
