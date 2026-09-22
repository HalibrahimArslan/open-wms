package com.hisarresearch.wms.domain;

import jakarta.persistence.*;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getFirmOrderDetail",
            query = "select id,stokkodu,stokadi,siparismiktar,teslimmiktar,barkod,stokbirimi from aur_vw_firm_order_detail " +
                "    where siptip = :sipTip and " +
                "          depono = :depoNo and " +
                "          musterikod = :musteriKod and " +
                "          caribaglantitipi = :cariBaglantiTipi",
            resultClass = AurVwFirmOrderDetail.class
        ),
    }
)
public class AurVwFirmOrderDetail {
    @Id
    private Long id;

    @Column(name = "stokkodu")
    private String stokKodu;

    @Column(name = "stokadi")
    private String stokAdi;

    @Column(name = "siparismiktar")
    private Double siparisMiktar;

    @Column(name = "teslimmiktar")
    private Double teslimMiktar;

    @Column(name = "barkod")
    private String barkod;

    @Column(name = "stokbirimi")
    private String stokBirimi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Double getSiparisMiktar() {
        return siparisMiktar;
    }

    public void setSiparisMiktar(Double siparisMiktar) {
        this.siparisMiktar = siparisMiktar;
    }

    public Double getTeslimMiktar() {
        return teslimMiktar;
    }

    public void setTeslimMiktar(Double teslimMiktar) {
        this.teslimMiktar = teslimMiktar;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }
}
