package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.AddressMovementType;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aur_address_placement_history")
public class AddressMovementHistory extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressMovementHistoryGenerator")
    @SequenceGenerator(name = "addressMovementHistoryGenerator", sequenceName = "aur_address_placement_history_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AurUser aurUser;

    @Column(name = "stok_kodu")
    private String stokKodu;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "process_amount")
    private Double processAmount;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AurDepoUrunAdres placementAddress;

    @Column(name = "placement_updated_amount")
    private Double placementUpdatedAmount;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AurDepoUrunAdres originAddress;

    @Column(name = "origin_updated_amount")
    private Double originUpdatedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type")
    private AddressMovementType movementType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurUser getAurUser() {
        return aurUser;
    }

    public void setAurUser(AurUser aurUser) {
        this.aurUser = aurUser;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getProcessAmount() {
        return processAmount;
    }

    public void setProcessAmount(Double processAmount) {
        this.processAmount = processAmount;
    }

    public AurDepoUrunAdres getPlacementAddress() {
        return placementAddress;
    }

    public void setPlacementAddress(AurDepoUrunAdres placementAddress) {
        this.placementAddress = placementAddress;
    }

    public Double getPlacementUpdatedAmount() {
        return placementUpdatedAmount;
    }

    public void setPlacementUpdatedAmount(Double placementUpdatedAmount) {
        this.placementUpdatedAmount = placementUpdatedAmount;
    }

    public AurDepoUrunAdres getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(AurDepoUrunAdres originAddress) {
        this.originAddress = originAddress;
    }

    public Double getOriginUpdatedAmount() {
        return originUpdatedAmount;
    }

    public void setOriginUpdatedAmount(Double originUpdatedAmount) {
        this.originUpdatedAmount = originUpdatedAmount;
    }

    public AddressMovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(AddressMovementType movementType) {
        this.movementType = movementType;
    }
}
