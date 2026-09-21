package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurSayimUrun.
 */
@Entity
@Table(name = "aur_sayim_urun")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurSayimUrun extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurSayimUrunGenerator")
    @SequenceGenerator(name = "aurSayimUrunGenerator",sequenceName = "aur_sayim_urun_seq",allocationSize = 1)
    private Long id;

    @ManyToOne
    private AurDepoUrunAdres address;

    @Column(name = "stok_kod")
    private String stokKod;

    @Column(name="miktar")
    private Double miktar;

    @Column(name="skt_date")
    private Instant sktDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SayimDurumu status;


    @ManyToOne
    @JsonIgnoreProperties(value = { "aurSayimUruns" }, allowSetters = true)
    private AurSayimTanim aurSayimTanim;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumns({
        @JoinColumn(name = "barkod", referencedColumnName = "barkod"),
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")
    })
    private Product product;


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurSayimUrun id(Long id) {
        this.id = id;
        return this;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public String getStokKod() {
        return this.stokKod;
    }

    public AurSayimUrun stokKod(String stokKod) {
        this.stokKod = stokKod;
        return this;
    }

    public void setStokKod(String stokKod) {
        this.stokKod = stokKod;
    }

    public Instant getSktDate() {
        return sktDate;
    }

    public void setSktDate(Instant sktDate) {
        this.sktDate = sktDate;
    }

    public SayimDurumu getStatus() {
        return this.status;
    }

    public AurSayimUrun status(SayimDurumu status) {
        this.status = status;
        return this;
    }

    public void setStatus(SayimDurumu status) {
        this.status = status;
    }

    public Double getMiktar() {
        if (miktar == null) {
            miktar = Double.valueOf("0");
        }
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public AurSayimTanim getAurSayimTanim() {
        return this.aurSayimTanim;
    }

    public AurSayimUrun aurSayimTanim(AurSayimTanim aurSayimTanim) {
        this.setAurSayimTanim(aurSayimTanim);
        return this;
    }

    public void setAurSayimTanim(AurSayimTanim aurSayimTanim) {
        this.aurSayimTanim = aurSayimTanim;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurSayimUrun)) {
            return false;
        }
        return id != null && id.equals(((AurSayimUrun) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurSayimUrun{" +
            "id=" + getId() +
            ", address=" + getAddress() +
            ", stokKod='" + getStokKod() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
