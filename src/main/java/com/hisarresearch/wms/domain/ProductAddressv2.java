package com.hisarresearch.wms.domain;


import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aur_depo_urun_adres_stok")
public class ProductAddressv2 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    private AurDepoUrunAdres urunAdres;

    @Column(name = "stok_kod")
    private String stokKod;

    @Column(name = "barkod_tipi")
    private String barkodTipi;

    @Column(name = "palet_barkod_id")
    private Long paletBarkodId;

    @Column(name = "company_code", insertable = false, updatable = false)
    private String companyCode;

    @Column(name = "depo_code", insertable = false, updatable = false)
    private String depoCode;

    @Column(name = "miktar")
    private Double miktar;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "barcode", referencedColumnName = "barkod"),
        @JoinColumn(name = "company_code", referencedColumnName = "company_code")
    })
    private Product product;

    @OneToMany(mappedBy = "productAddress", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"productAddress"},allowSetters = true)
    private Set<ProductAddressSkt> productAddressSktList = new HashSet<>();

    public ProductAddressv2(Long id) {
        this.id = id;
    }

    public ProductAddressv2() {

    }

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

    public AurDepoUrunAdres getUrunAdres() {
        return urunAdres;
    }

    public void setUrunAdres(AurDepoUrunAdres urunAdres) {
        this.urunAdres = urunAdres;
    }

    public String getStokKod() {
        return stokKod;
    }

    public void setStokKod(String stokKod) {
        this.stokKod = stokKod;
    }

    public String getBarkodTipi() {
        return barkodTipi;
    }

    public void setBarkodTipi(String barkodTipi) {
        this.barkodTipi = barkodTipi;
    }

    public Long getPaletBarkodId() {
        return paletBarkodId;
    }

    public void setPaletBarkodId(Long paletBarkodId) {
        this.paletBarkodId = paletBarkodId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }

    public Double getMiktar() {
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<ProductAddressSkt> getProductAddressSktList() {
        return productAddressSktList;
    }

    public void setProductAddressSktList(Set<ProductAddressSkt> productAddressSktList) {
        this.productAddressSktList = productAddressSktList;
    }
}
