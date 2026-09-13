package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.ProcessType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "process_tree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProcessTree extends AbstractAuditingEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processTreeSequenceGenerator")
    @SequenceGenerator(name = "processTreeSequenceGenerator",sequenceName = "process_tree_seq",allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AurUser user;
    @Enumerated(EnumType.STRING)
    @Column(name = "process_type")
    ProcessType processType;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AurDepoUrunAdres targetAddress;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "process_child_id")
    private Long processChildId;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "company_code")
    private Long companyCode;

    @Column(name = "depo_code")
    private Long depoCode;

    @OneToMany(mappedBy = "process")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "process" }, allowSetters = true)
    private Set<ProcessLeaf> processLeaves = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurUser getUser() {
        return user;
    }

    public void setUser(AurUser user) {
        this.user = user;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public AurDepoUrunAdres getTargetAddress() {
        return targetAddress;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setTargetAddress(AurDepoUrunAdres targetAddress) {
        this.targetAddress = targetAddress;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getProcessChildId() {
        return processChildId;
    }

    public void setProcessChildId(Long processChildId) {
        this.processChildId = processChildId;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public Long getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(Long depoCode) {
        this.depoCode = depoCode;
    }

    public ProcessTree processLeaves(Set<ProcessLeaf> processLeaves){
        this.setProcessLeaves(processLeaves);
        return this;
    }

    public Set<ProcessLeaf> getProcessLeaves() {
        return this.processLeaves;
    }

    public void setProcessLeaves(Set<ProcessLeaf> processLeaves) {
        if (this.processLeaves != null) {
            this.processLeaves.forEach(i -> i.setProcess(null));
        }
        if (processLeaves != null) {
            processLeaves.forEach(i -> i.setProcess(this));
        }
        this.processLeaves = processLeaves;
    }

    public ProcessTree addProcessLeaf(ProcessLeaf processLeaf) {
        this.processLeaves.add(processLeaf);
        processLeaf.setProcess(this);
        return this;
    }

    public ProcessTree removeProcessLeaf(ProcessLeaf processLeaf) {
        this.processLeaves.remove(processLeaf);
        processLeaf.setProcess(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessTree)) {
            return false;
        }
        return id != null && id.equals(((ProcessTree) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, processType, status, targetAddress, amount, processChildId, isPublic, companyCode, depoCode);
    }
}
