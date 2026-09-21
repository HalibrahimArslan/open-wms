package com.hisarresearch.wms.domain.barcode;


import com.hisarresearch.wms.domain.AbstractAuditingEntity;
import com.hisarresearch.wms.domain.enumeration.PalletBarcodeStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pallet_barcode")
public class PalletBarcode extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "palletBarcodeGenerator")
    @SequenceGenerator(name = "palletBarcodeGenerator",sequenceName = "pallet_barcode_seq",allocationSize = 1)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PalletBarcodeStatus palletBarcodeStatus;

    @Column(name = "barcode")
    private String barcode;

    @JsonIgnore
    @OneToMany(mappedBy = "palletBarcode")
    private Set<PalletBarcodeOrderRel> details = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PalletBarcodeStatus getPalletBarcodeStatus() {
        return palletBarcodeStatus;
    }

    public void setPalletBarcodeStatus(PalletBarcodeStatus palletBarcodeStatus) {
        this.palletBarcodeStatus = palletBarcodeStatus;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Set<PalletBarcodeOrderRel> getDetails() {
        return details;
    }

    public void setDetails(Set<PalletBarcodeOrderRel> details) {
        this.details = details;
    }
}
