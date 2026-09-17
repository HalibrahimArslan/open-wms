package com.hisarresearch.wms.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getAurAddressStockList",
            query = "select id, urunadresid, stokkodu, lastupdatedate, lastupdateuser, depocode, miktar, barcode, adres,status,stockname from aur_vw_depo_stok_adres " +
                "    where urunadresid = :urunAdresId and" +
                "          status = true  ",
            resultClass = AurVwDepoStokAdres.class
        ),
    }
)
public class AurVwDepoStokAdres {
    @Id
    private Long id;

    @Column(name = "urunadresid")
    private Long urunAdresId;

    @Column(name = "stokkodu")
    private String stokKodu;

    @Column(name = "lastupdatedate")
    private Date lastUpdateDate;

    @Column(name = "lastupdateuser")
    private String lastUpdateUser;

    @Column(name = "depocode")
    private String depoCode;

    @Column(name = "miktar")
    private Double miktar;

    @Column(name = "barcode")
    private String barkod;

    @Column(name = "adres")
    private String adres;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "stockname")
    private String stockName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUrunAdresId() {
        return urunAdresId;
    }

    public void setUrunAdresId(Long urunAdresId) {
        this.urunAdresId = urunAdresId;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
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

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
