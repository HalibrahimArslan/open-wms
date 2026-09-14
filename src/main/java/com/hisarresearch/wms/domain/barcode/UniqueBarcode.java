package com.hisarresearch.wms.domain.barcode;

import com.hisarresearch.wms.domain.AbstractAuditingEntity;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeState;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Audited
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(
    name = "unique_barcode",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_unique_barcode_barcode", columnNames = {"barcode"})
    }
)
public class UniqueBarcode extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uniqueBarcodeGenerator")
    @SequenceGenerator(
        name = "uniqueBarcodeGenerator",
        sequenceName = "unique_barcode_seq",
        allocationSize = 1
    )
    private Long id;

    @NotAudited
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
        @JoinColumn(name = "erp_barcode", referencedColumnName = "barkod"),
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")
    })
    private Product product;

    @NotAudited
    @Column(name = "barcode", length = 150, nullable = false, unique = true)
    private String barcode;

    @NotAudited
    @Column(name = "receiving_date")
    private Instant receivingDate;

    @NotAudited
    @Column(name = "customer_code", length = 50)
    private String customerCode;

    @NotAudited
    @Column(name = "erp_order_info", length = 25)
    private String erpOrderInfo;

    @NotAudited
    @Column(name = "parti_code", length = 20)
    private String partiCode;

    @NotAudited
    @Column(name = "lot_number", nullable = false)
    private Long lotNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private UniqueBarcodeState status = UniqueBarcodeState.CREATED;

    @NotAudited
    @Column(name = "quantity")
    private BigDecimal quantity;

    @NotAudited
    @Type(type = "jsonb")
    @Column(name = "description", columnDefinition = "jsonb")
    private Map<String, Object> description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "aur_order_detail_id")
    private AurOrderDetail aurOrderDetail;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "address_id")
    private AurDepoUrunAdres address;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Instant getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(Instant receivingDate) {
        this.receivingDate = receivingDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getErpOrderInfo() {
        return erpOrderInfo;
    }

    public void setErpOrderInfo(String erpOrderInfo) {
        this.erpOrderInfo = erpOrderInfo;
    }

    public String getPartiCode() {
        return partiCode;
    }

    public void setPartiCode(String partiCode) {
        this.partiCode = partiCode;
    }

    public Long getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(Long lotNumber) {
        this.lotNumber = lotNumber;
    }

    public UniqueBarcodeState getStatus() {
        return status;
    }

    public void setStatus(UniqueBarcodeState status) {
        this.status = status;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Object> description) {
        this.description = description;
    }

    public AurOrderDetail getAurOrderDetail() {
        return aurOrderDetail;
    }

    public void setAurOrderDetail(AurOrderDetail aurOrderDetail) {
        this.aurOrderDetail = aurOrderDetail;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

}
