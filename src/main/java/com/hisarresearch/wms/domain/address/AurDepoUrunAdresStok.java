package com.hisarresearch.wms.domain.address;

import com.hisarresearch.wms.domain.AbstractAuditingEntity;
import com.hisarresearch.wms.domain.AurOrderMaster;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Audited(auditParents = {AbstractAuditingEntity.class})
@Table(name = "aur_depo_urun_adres_stok")
public class AurDepoUrunAdresStok extends AbstractAuditingEntity implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurDepoUrunAdresStokGenerator")
    @SequenceGenerator(name = "aurDepoUrunAdresStokGenerator",sequenceName = "aur_depo_urun_adres_stok_seq",allocationSize = 1)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "urun_adres_id")
    private Long urunAdresId;

    @Column(name = "stok_kod")
    private String stokKod;

    @Column(name = "barkod_tipi")
    private String barkodTipi;

    @Column(name = "palet_barkod_id")
    private Long paletBarkodId;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "depo_code")
    private String depoCode;

    @Column(name = "miktar")
    private Double miktar;

    @Column(name = "barcode")
    private String barcode;

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

    public Long getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(Long urunAdresId) {
        this.urunAdresId = urunAdresId;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public AurDepoUrunAdresStok() {
    }

    public AurDepoUrunAdresStok (Boolean status, Long urunAdresId, String stokKod, String barkodTipi, Long paletBarkodId, String companyCode, String depoCode, Double miktar, String barcode) {
        this.status = status;
        this.urunAdresId = urunAdresId;
        this.stokKod = stokKod;
        this.barkodTipi = barkodTipi;
        this.paletBarkodId = paletBarkodId;
        this.companyCode = companyCode;
        this.depoCode = depoCode;
        this.miktar = miktar;
        this.barcode = barcode;
    }

    @Override
    public AurDepoUrunAdresStok clone() throws CloneNotSupportedException {
        AurDepoUrunAdresStok clone = (AurDepoUrunAdresStok) super.clone();
        clone.setId(null);
        return clone;
    }
}
