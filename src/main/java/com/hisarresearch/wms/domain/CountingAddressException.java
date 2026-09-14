package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "counting_address_exception")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CountingAddressException {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countingAddressExceptionGenerator")
    @SequenceGenerator(name = "countingAddressExceptionGenerator", sequenceName = "counting_address_exception_seq", allocationSize = 1)
    private Long id;


    @Column(name = "status")
    Boolean status;

    @ManyToOne
    AurDepoUrunAdres address;

    @ManyToOne
    AurSayimTanim countingDefinition;

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

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public AurSayimTanim getCountingDefinition() {
        return countingDefinition;
    }

    public void setCountingDefinition(AurSayimTanim countingDefinition) {
        this.countingDefinition = countingDefinition;
    }
}

