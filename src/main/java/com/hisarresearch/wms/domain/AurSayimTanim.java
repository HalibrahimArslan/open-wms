package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AurSayimTanim.
 */
@Entity
@Table(name = "aur_sayim_tanim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurSayimTanim extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurSayimTanimGenerator")
    @SequenceGenerator(name = "aurSayimTanimGenerator",sequenceName = "aur_sayim_tanim_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "sayim_adi")
    private String sayimAdi;

    @Column(name = "depo_no")
    private String depoNo;

    @Column(name = "sayim_tarihi")
    private Instant sayimTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "sayim_durumu")
    private SayimDurumu sayimDurumu;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "sayimi_bitiren_kullanici")
    private String sayimiBitirenKullanici;

    @Column(name = "sayimi_onaylayan_kullanici")
    private String sayimiOnaylayanKullanici;

    @Type(StringArrayType.class)
    @Column(name = "visibility_authorities",columnDefinition = "text[]")
    private String[] visibilityAuthorities;

    @Enumerated(EnumType.STRING)
    @Column(name = "counting_type")
    private CountingType countingType;

    @Column(name = "company_code")
    private String companyCode;

    @OneToMany(mappedBy = "aurSayimTanim")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aurSayimTanim" }, allowSetters = true)
    private Set<AurSayimUrun> aurSayimUruns = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public AurSayimTanim() {

    }

    public AurSayimTanim id(Long id) {
        this.id = id;
        return this;
    }

    public AurSayimTanim( String depoNo,Boolean status, SayimDurumu sayimDurumu, String[] visibilityAuthorities) {
        this.status = status;
        this.depoNo = depoNo;
        this.sayimDurumu = sayimDurumu;
        this.visibilityAuthorities = visibilityAuthorities;
    }



    public Boolean getStatus() {
        return this.status;
    }

    public AurSayimTanim status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSayimAdi() {
        return this.sayimAdi;
    }

    public AurSayimTanim sayimAdi(String sayimAdi) {
        this.sayimAdi = sayimAdi;
        return this;
    }

    public void setSayimAdi(String sayimAdi) {
        this.sayimAdi = sayimAdi;
    }

    public String getDepoNo() {
        return this.depoNo;
    }

    public AurSayimTanim depoNo(String depoNo) {
        this.depoNo = depoNo;
        return this;
    }

    public void setDepoNo(String depoNo) {
        this.depoNo = depoNo;
    }

    public Instant getSayimTarihi() {
        return this.sayimTarihi;
    }

    public AurSayimTanim sayimTarihi(Instant sayimTarihi) {
        this.sayimTarihi = sayimTarihi;
        return this;
    }

    public void setSayimTarihi(Instant sayimTarihi) {
        this.sayimTarihi = sayimTarihi;
    }

    public SayimDurumu getSayimDurumu() {
        return this.sayimDurumu;
    }

    public AurSayimTanim sayimDurumu(SayimDurumu sayimDurumu) {
        this.sayimDurumu = sayimDurumu;
        return this;
    }

    public void setSayimDurumu(SayimDurumu sayimDurumu) {
        this.sayimDurumu = sayimDurumu;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public AurSayimTanim aciklama(String aciklama) {
        this.aciklama = aciklama;
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getSayimiBitirenKullanici() {
        return this.sayimiBitirenKullanici;
    }

    public AurSayimTanim sayimiBitirenKullanici(String sayimiBitirenKullanici) {
        this.sayimiBitirenKullanici = sayimiBitirenKullanici;
        return this;
    }

    public void setSayimiBitirenKullanici(String sayimiBitirenKullanici) {
        this.sayimiBitirenKullanici = sayimiBitirenKullanici;
    }

    public String getSayimiOnaylayanKullanici() {
        return this.sayimiOnaylayanKullanici;
    }

    public AurSayimTanim sayimiOnaylayanKullanici(String sayimiOnaylayanKullanici) {
        this.sayimiOnaylayanKullanici = sayimiOnaylayanKullanici;
        return this;
    }

    public void setSayimiOnaylayanKullanici(String sayimiOnaylayanKullanici) {
        this.sayimiOnaylayanKullanici = sayimiOnaylayanKullanici;
    }

    public CountingType getCountingType() {
        return countingType;
    }

    public void setCountingType(CountingType countingType) {
        this.countingType = countingType;
    }

    public String[] getVisibilityAuthorities() {
        return visibilityAuthorities;
    }

    public void setVisibilityAuthorities(String[] visibilityAuthorities) {
        this.visibilityAuthorities = visibilityAuthorities;
    }

    public Set<AurSayimUrun> getAurSayimUruns() {
        return this.aurSayimUruns;
    }

    public AurSayimTanim aurSayimUruns(Set<AurSayimUrun> aurSayimUruns) {
        this.setAurSayimUruns(aurSayimUruns);
        return this;
    }

    public AurSayimTanim addAurSayimUrun(AurSayimUrun aurSayimUrun) {
        this.aurSayimUruns.add(aurSayimUrun);
        aurSayimUrun.setAurSayimTanim(this);
        return this;
    }

    public AurSayimTanim removeAurSayimUrun(AurSayimUrun aurSayimUrun) {
        this.aurSayimUruns.remove(aurSayimUrun);
        aurSayimUrun.setAurSayimTanim(null);
        return this;
    }

    public void setAurSayimUruns(Set<AurSayimUrun> aurSayimUruns) {
        if (this.aurSayimUruns != null) {
            this.aurSayimUruns.forEach(i -> i.setAurSayimTanim(null));
        }
        if (aurSayimUruns != null) {
            aurSayimUruns.forEach(i -> i.setAurSayimTanim(this));
        }
        this.aurSayimUruns = aurSayimUruns;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurSayimTanim)) {
            return false;
        }
        return id != null && id.equals(((AurSayimTanim) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurSayimTanim{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", sayimAdi='" + getSayimAdi() + "'" +
            ", depoNo='" + getDepoNo() + "'" +
            ", sayimTarihi='" + getSayimTarihi() + "'" +
            ", sayimDurumu='" + getSayimDurumu() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", sayimiBitirenKullanici='" + getSayimiBitirenKullanici() + "'" +
            ", sayimiOnaylayanKullanici='" + getSayimiOnaylayanKullanici() + "'" +
            "}";
    }
}
