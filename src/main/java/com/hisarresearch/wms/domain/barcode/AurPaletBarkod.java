package com.hisarresearch.wms.domain.barcode;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aur_palet_barkod")
public class AurPaletBarkod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "palet_barkod_id", nullable = false)
    private Long paletBarkodId;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "palet_barkod")
    private String paletBarkod;

    @Column(name = "koli_miktari")
    private Short koliMiktari;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "depo_code")
    private String depoCode;

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

    public Long getPaletBarkodId() {
        return paletBarkodId;
    }

    public void setPaletBarkodId(Long paletBarkodId) {
        this.paletBarkodId = paletBarkodId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPaletBarkod() {
        return paletBarkod;
    }

    public void setPaletBarkod(String paletBarkod) {
        this.paletBarkod = paletBarkod;
    }

    public Short getKoliMiktari() {
        return koliMiktari;
    }

    public void setKoliMiktari(Short koliMiktari) {
        this.koliMiktari = koliMiktari;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
