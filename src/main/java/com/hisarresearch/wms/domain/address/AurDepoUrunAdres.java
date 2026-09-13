package com.hisarresearch.wms.domain.address;

import com.hisarresearch.wms.domain.AbstractAuditingEntity;
import com.hisarresearch.wms.domain.ProductAddressv2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "aur_depo_urun_adres")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurDepoUrunAdres extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurDepoUrunAdresGenerator")
    @SequenceGenerator(name = "aurDepoUrunAdresGenerator",sequenceName = "aur_depo_urun_adres_seq",allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long urunAdresId;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "adres", length = 100)
    private String adres;

    @Column(name = "depo_no", nullable = false)
    private String depoNo;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "adres_tipi", length = 10)
    private String adresTipi;

    @Column(name = "bolum", nullable = false, length = 2)
    private String bolum;

    @Column(name = "reyon",  length = 2)
    private String reyon;

    @Column(name = "unite",  length = 2)
    private String unite;

    @Column(name = "kat", nullable = false, length = 3)
    private String kat;

    @Column(name = "oda", nullable = false, length = 2)
    private String oda;

    @Column(name = "gecici_adres")
    private Boolean geciciAdres;

    @Column(name = "toplama_gozu")
    private Boolean toplamaGozu;

    @Column(name = "kontrol_adres")
    private Boolean kontrolAdres;

    @Column(name = "countable")
    private Boolean countable;

    @OneToMany(mappedBy = "urunAdres")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ProductAddressv2> productAddressList = new HashSet<>();

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Long getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(Long urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(String depoNo) {
        this.depoNo = depoNo;
    }

    public String getAdresTipi() {
        return adresTipi;
    }

    public void setAdresTipi(String adresTipi) {
        this.adresTipi = adresTipi;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getReyon() {
        return reyon;
    }

    public void setReyon(String reyon) {
        this.reyon = reyon;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getKat() {
        return kat;
    }

    public void setKat(String kat) {
        this.kat = kat;
    }

    public Boolean getGeciciAdres() {
        return geciciAdres;
    }

    public void setGeciciAdres(Boolean geciciAdres) {
        this.geciciAdres = geciciAdres;
    }

    public Boolean getToplamaGozu() {
        return toplamaGozu;
    }

    public void setToplamaGozu(Boolean toplamaGozu) {
        this.toplamaGozu = toplamaGozu;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Boolean getKontrolAdres() {
        return kontrolAdres;
    }

    public void setKontrolAdres(Boolean kontrolAdres) {
        this.kontrolAdres = kontrolAdres;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = countable;
    }

    public String getOda() {
        return oda;
    }

    public void setOda(String oda) {
        this.oda = oda;
    }

    public Set<ProductAddressv2> getProductAddressList() {
        return productAddressList;
    }

    public void setProductAddressList(Set<ProductAddressv2> productAddressList) {
        this.productAddressList = productAddressList;
    }

    @Override
    public String toString() {
        return "AurDepoUrunAdres{" +
            "urunAdresId=" + urunAdresId +
            ", status=" + status +
            ", adres='" + adres + '\'' +
            ", depoNo=" + depoNo +
            ", companyCode='" + companyCode + '\'' +
            ", adresTipi='" + adresTipi + '\'' +
            ", bolum='" + bolum + '\'' +
            ", reyon='" + reyon + '\'' +
            ", unite='" + unite + '\'' +
            ", kat=" + kat +
            ", geciciAdres=" + geciciAdres +
            ", toplamaGozu=" + toplamaGozu +
            ", kontrolAdres=" + kontrolAdres +
            '}';
    }

    public AurDepoUrunAdres() {

    }

    public AurDepoUrunAdres(Long urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public AurDepoUrunAdres(AurDepoUrunAdres aurDepoUrunAdres) {
        this.urunAdresId = aurDepoUrunAdres.getUrunAdresId();
        this.status = aurDepoUrunAdres.getStatus();
        this.adres = aurDepoUrunAdres.getAdres();
        this.depoNo = aurDepoUrunAdres.getDepoNo();
        this.companyCode = aurDepoUrunAdres.getCompanyCode();
        this.adresTipi = aurDepoUrunAdres.getAdresTipi();
        this.bolum = aurDepoUrunAdres.getBolum();
        this.reyon = aurDepoUrunAdres.getReyon();
        this.unite = aurDepoUrunAdres.getUnite();
        this.kat = aurDepoUrunAdres.getKat();
        this.geciciAdres = aurDepoUrunAdres.getGeciciAdres();
        this.toplamaGozu = aurDepoUrunAdres.getToplamaGozu();
        this.kontrolAdres = aurDepoUrunAdres.getKontrolAdres();
        this.oda = aurDepoUrunAdres.getOda();
    }
}
