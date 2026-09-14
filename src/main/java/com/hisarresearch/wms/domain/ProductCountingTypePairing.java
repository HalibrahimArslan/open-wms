package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;

import javax.persistence.*;

@Entity
@Table(name = "product_counting_type_pairing")
public class ProductCountingTypePairing extends AbstractAuditingEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productCountingTypePairingGenerator")
    @SequenceGenerator(name = "productCountingTypePairingGenerator",sequenceName = "product_counting_type_pairing_seq",allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "counting_type")
    private CountingType countingType;

    @Column(name = "warehouse_code")
    private int warehouseCode;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public CountingType getCountingType() {
        return countingType;
    }

    public void setCountingType(CountingType countingType) {
        this.countingType = countingType;
    }

    public int getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(int warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
