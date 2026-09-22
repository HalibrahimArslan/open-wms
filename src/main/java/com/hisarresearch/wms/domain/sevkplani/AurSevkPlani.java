package com.hisarresearch.wms.domain.sevkplani;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "aur_sevk_plani")
public class AurSevkPlani {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurSevkPlaniGenerator")
    @SequenceGenerator(name = "aurSevkPlaniGenerator", sequenceName = "aur_sevk_plani_seq", allocationSize = 1)
    private Long id;

    private String companyCode;

    private String cariKod;

    private Long addressNo;

    private String bolgeKodu;

    private String bolgeAdi;

    private Integer depoNo;

    private String durum;

    private String transGroupCode;

    private String transGroupName;

    private String approvedUser;

    private Instant createdDate;

    private String createdBy;

    private Instant lastModifiedDate;

    private String lastModifiedBy;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long uploadId;

    public enum Status {
        OPEN,
        CLOSE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public Long getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(Long addressNo) {
        this.addressNo = addressNo;
    }

    public String getBolgeKodu() {
        return bolgeKodu;
    }

    public void setBolgeKodu(String bolgeKodu) {
        this.bolgeKodu = bolgeKodu;
    }

    public String getBolgeAdi() {
        return bolgeAdi;
    }

    public void setBolgeAdi(String bolgeAdi) {
        this.bolgeAdi = bolgeAdi;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public String getTransGroupCode() {
        return transGroupCode;
    }

    public void setTransGroupCode(String transGroupCode) {
        this.transGroupCode = transGroupCode;
    }

    public String getTransGroupName() {
        return transGroupName;
    }

    public void setTransGroupName(String transGroupName) {
        this.transGroupName = transGroupName;
    }

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }
}
