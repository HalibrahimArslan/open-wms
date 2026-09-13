package com.hisarresearch.wms.domain;

import javax.persistence.*;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.TransactionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderPickingTransaction.
 */
@Entity
@Table(name = "order_picking_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderPickingTransaction extends AbstractAuditingEntityWithoutJsonIgnore {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_picking_transaction_generator")
    @SequenceGenerator(name = "order_picking_transaction_generator",sequenceName = "order_picking_transaction_seq",allocationSize = 1)
    private Long id;

    @Column(name = "reference_id")
    private Long referenceId;

    @ManyToOne
    @JoinColumn(name = "urun_adres_id",referencedColumnName = "id")
    private AurDepoUrunAdres address;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "transaction_amount")
    private Double transactionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "description")
    private String description;

    @Column(name = "barcode")
    private String barcode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderPickingTransaction id(Long id) {
        this.id = id;
        return this;
    }

    public Long getReferenceId() {
        return this.referenceId;
    }

    public OrderPickingTransaction referenceId(Long referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public OrderPickingTransaction status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getTransactionAmount() {
        return this.transactionAmount;
    }

    public OrderPickingTransaction transactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderPickingTransaction)) {
            return false;
        }
        return id != null && id.equals(((OrderPickingTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderPickingTransaction{" +
            "id=" + getId() +
            ", referenceId=" + getReferenceId() +
            ", address=" + getAddress() +
            ", stockCode= " + getStockCode() +
            ", status='" + getStatus() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            ", transactionType=  " + getTransactionType() +
            ", description= " + getDescription() +
            ", barcode= " + getBarcode() +
            "}";
    }
}
