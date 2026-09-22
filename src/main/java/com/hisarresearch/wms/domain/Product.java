package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductId id;

    @Column(name = "stok_kodu")
    private String stokKodu;

    @Column(name = "stok_adi")
    private String stokAdi;

    @Column(name = "ana_grup")
    private String anaGrup;

    @Column(name = "kategori_adi")
    private String kategoriAdi;

    @Column(name = "stok_birimi")
    private String stokBirimi;

    @Column(name = "miktar")
    private Double miktar;

    @Column(name = "skt_flag")
    private Boolean sktFlag = false;

    @Column(name = "lot_based_tracking")
    private Boolean lotBasedTracking = false;

    @Column(name = "description")
    private String description;

    @Type(JsonType.class)
    @Column(name = "physical_attributes", columnDefinition = "jsonb")
    private Map<String, Object> physicalAttributes;

    @Schema(hidden = true)
    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = {"product"}, allowSetters = true)
    private Set<ProductAddressv2> productAddresses = new HashSet<>();

    // Getters and setters
    public ProductId getId() {
        return id;
    }

    public void setId(ProductId id) {
        this.id = id;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getAnaGrup() {
        return anaGrup;
    }

    public void setAnaGrup(String anaGrup) {
        this.anaGrup = anaGrup;
    }

    public String getKategoriAdi() {
        return kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public Double getMiktar() {
        return miktar;
    }

    public void setMiktar(Double miktar) {
        this.miktar = miktar;
    }

    public Boolean getSktFlag() {
        return sktFlag;
    }

    public void setSktFlag(Boolean sktFlag) {
        this.sktFlag = sktFlag;
    }

    public Boolean getLotBasedTracking() {
        return lotBasedTracking;
    }

    public void setLotBasedTracking(Boolean lotBasedTracking) {
        this.lotBasedTracking = lotBasedTracking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getPhysicalAttributes() {
        return physicalAttributes;
    }

    public void setPhysicalAttributes(Map<String, Object> physicalAttributes) {
        this.physicalAttributes = physicalAttributes;
    }

    public Set<ProductAddressv2> getProductAddresses() {
        return productAddresses;
    }

    public void setProductAddresses(Set<ProductAddressv2> productAddresses) {
        this.productAddresses = productAddresses;
    }
}
