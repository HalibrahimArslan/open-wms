package com.hisarresearch.wms.domain;

import javax.persistence.*;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getAurSayimResult",
            query = "select row_number() over (order by asu.stok_kod) as id,asu.stok_kod,asu.barkod,sum(asu.miktar) as total_amount,esi.stok_adi " +
                "from aur_sayim_urun asu " +
                "left join product esi " +
                "on asu.barkod = esi.barkod " +
                "where asu.aur_sayim_tanim_id = :aurSayimTanimId group by asu.stok_kod,asu.barkod,esi.stok_adi",
            resultClass = AurSayimResult.class
        ),
    }
)
public class AurSayimResult {

    @Id
    private Long id;

    @Column(name = "stok_kod")
    private String stokKod;

    @Column(name = "barkod")
    private String barcode;

    @Column(name = "total_amount")
    private Double amount;

    @Column(name = "stok_adi")
    private String stokAdi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStokKod() {
        return stokKod;
    }

    public void setStokKod(String stokKod) {
        this.stokKod = stokKod;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }
}
