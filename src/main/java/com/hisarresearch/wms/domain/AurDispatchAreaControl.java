package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
@Entity
@Table(name = "aur_dispatch_area_control")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurDispatchAreaControl extends AbstractAuditingEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurDispatchAreaControlGenerator")
    @SequenceGenerator(name = "aurDispatchAreaControlGenerator",sequenceName = "aur_dispatch_area_control_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "aurDispatchAreaControl" }, allowSetters = true)
    @JoinColumn(name = "aur_order_detail_id")
    private AurOrderDetail aurTmpDetail;

    @Column(name = "observed_amount")
    private Double observedAmount;

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

    public AurOrderDetail getAurTmpDetail() {
        return aurTmpDetail;
    }

    public void setAurTmpDetail(AurOrderDetail aurTmpDetail) {
        this.aurTmpDetail = aurTmpDetail;
    }

    public Double getObservedAmount() {
        return observedAmount;
    }

    public void setObservedAmount(Double observedAmount) {
        this.observedAmount = observedAmount;
    }
}
