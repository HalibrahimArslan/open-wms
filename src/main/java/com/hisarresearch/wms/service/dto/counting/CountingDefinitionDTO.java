package com.hisarresearch.wms.service.dto.counting;

import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.domain.enumeration.SayimDurumu;


import jakarta.validation.constraints.NotNull;
import java.time.Instant;
public class CountingDefinitionDTO {
    private Long id;

    private Boolean status = true;

    private String sayimAdi;

    @NotNull
    private String depoNo;

    private Instant sayimTarihi;

    private SayimDurumu sayimDurumu;

    private String aciklama;

    private String sayimiBitirenKullanici;

    private String sayimiOnaylayanKullanici;

    @NotNull
    private String[] visibilityAuthorities;

    private CountingType countingType;

    @NotNull
    private String companyCode;

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

    public String getSayimAdi() {
        return sayimAdi;
    }

    public void setSayimAdi(String sayimAdi) {
        this.sayimAdi = sayimAdi;
    }

    public String getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(String depoNo) {
        this.depoNo = depoNo;
    }

    public Instant getSayimTarihi() {
        return sayimTarihi;
    }

    public void setSayimTarihi(Instant sayimTarihi) {
        this.sayimTarihi = sayimTarihi;
    }

    public SayimDurumu getSayimDurumu() {
        return sayimDurumu;
    }

    public void setSayimDurumu(SayimDurumu sayimDurumu) {
        this.sayimDurumu = sayimDurumu;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getSayimiBitirenKullanici() {
        return sayimiBitirenKullanici;
    }

    public void setSayimiBitirenKullanici(String sayimiBitirenKullanici) {
        this.sayimiBitirenKullanici = sayimiBitirenKullanici;
    }

    public String getSayimiOnaylayanKullanici() {
        return sayimiOnaylayanKullanici;
    }

    public void setSayimiOnaylayanKullanici(String sayimiOnaylayanKullanici) {
        this.sayimiOnaylayanKullanici = sayimiOnaylayanKullanici;
    }

    public String[] getVisibilityAuthorities() {
        return visibilityAuthorities;
    }

    public void setVisibilityAuthorities(String[] visibilityAuthorities) {
        this.visibilityAuthorities = visibilityAuthorities;
    }

    public CountingType getCountingType() {
        return countingType;
    }

    public void setCountingType(CountingType countingType) {
        this.countingType = countingType;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
