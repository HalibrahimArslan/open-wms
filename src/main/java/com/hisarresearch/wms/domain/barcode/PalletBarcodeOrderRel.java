package com.hisarresearch.wms.domain.barcode;

import com.hisarresearch.wms.domain.AbstractAuditingEntity;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pallet_barcode_order_rel")
public class PalletBarcodeOrderRel extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "palletBarcodeOrderRelGenerator")
    @SequenceGenerator(name = "palletBarcodeOrderRelGenerator",sequenceName = "pallet_barcode_order_rel_seq",allocationSize = 1)
    private Long id;

    @ManyToOne
    private AurOrderMaster aurOrder;

    @ManyToOne
    @JoinColumn(name = "aur_order_detail_id")
    private AurOrderDetail aurTmpDetail;

    @Column(name = "status")
    @NotNull
    private Boolean status;

    @ManyToOne
    private PalletBarcode palletBarcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurOrderMaster getAurOrder() {
        return aurOrder;
    }

    public void setAurOrder(AurOrderMaster aurOrder) {
        this.aurOrder = aurOrder;
    }

    public AurOrderDetail getAurTmpDetail() {
        return aurTmpDetail;
    }

    public void setAurTmpDetail(AurOrderDetail aurTmpDetail) {
        this.aurTmpDetail = aurTmpDetail;
    }

    public @NotNull Boolean getStatus() {
        return status;
    }

    public void setStatus(@NotNull Boolean status) {
        this.status = status;
    }

    public PalletBarcode getPalletBarcode() {
        return palletBarcode;
    }

    public void setPalletBarcode(PalletBarcode palletBarcode) {
        this.palletBarcode = palletBarcode;
    }
}
