package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import jakarta.persistence.*;
@Entity
@Table(name = "process_leaf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProcessLeaf extends AbstractAuditingEntity{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processLeafGenerator")
    @SequenceGenerator(name = "processLeafGenerator",sequenceName = "process_leaf_seq",allocationSize = 1)
    private Long id;

    @ManyToOne
    private ProcessTree process;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "barcode")
    private String barcode;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AurDepoUrunAdres address;

    @Column(name =  "amount")
    private Double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessTree getProcess() {
        return process;
    }

    public void setProcess(ProcessTree process) {
        this.process = process;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
