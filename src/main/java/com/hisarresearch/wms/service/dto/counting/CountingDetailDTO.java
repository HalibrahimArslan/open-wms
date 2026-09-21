package com.hisarresearch.wms.service.dto.counting;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class CountingDetailDTO {
    private Long id;

    private AurDepoUrunAdres address;

    private String stokKod;

    @NotNull
    @Positive(message = "Miktar must be a positive value")
    private Double miktar;

    private Instant sktDate;

    private SayimDurumu status;

    private ProductWithoutAddressDTO product;

    private Long countingDefinitionId;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getCountingDefinitionId() {
        return countingDefinitionId;
    }

    public void setCountingDefinitionId(Long countingDefinitionId) {
        this.countingDefinitionId = countingDefinitionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurDepoUrunAdres getAddress() {
        return address;
    }

    public void setAddress(AurDepoUrunAdres address) {
        this.address = address;
    }

    public String getStokKod() {
        return stokKod;
    }

    public void setStokKod(String stokKod) {
        this.stokKod = stokKod;
    }

    public @NotNull @Positive(message = "Miktar must be a positive value") Double getMiktar() {
        return miktar;
    }

    public void setMiktar(@NotNull @Positive(message = "Miktar must be a positive value") Double miktar) {
        this.miktar = miktar;
    }

    public Instant getSktDate() {
        return sktDate;
    }

    public void setSktDate(Instant sktDate) {
        this.sktDate = sktDate;
    }

    public SayimDurumu getStatus() {
        return status;
    }

    public void setStatus(SayimDurumu status) {
        this.status = status;
    }

    public ProductWithoutAddressDTO getProduct() {
        return product;
    }

    public void setProduct(ProductWithoutAddressDTO product) {
        this.product = product;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
