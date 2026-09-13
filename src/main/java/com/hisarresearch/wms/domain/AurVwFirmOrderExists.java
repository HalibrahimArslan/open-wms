package com.hisarresearch.wms.domain;

import javax.persistence.*;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getFirmOrderExists",
            query = "SELECT cari_kod,cari_unvan,cari_baglanti_tipi,cari_hareket_tipi,bolge_kodu,bolge_adi FROM aur_vw_firm_order_exists  WHERE sip_tip = :sipTip AND cari_baglanti_tipi = :cariBaglantiTipi AND sip_depo_no = :sipDepoNo",
            resultClass = AurVwFirmOrderExists.class
        ),
    }
)
public class AurVwFirmOrderExists {
    @Id
    private String cariKod;

    @Column(name = "cari_unvan")
    private String cariUnvan;

    @Column(name = "cari_baglanti_tipi")
    private String cariBaglantiTipi;

    @Column(name = "cari_hareket_tipi")
    private String cariHareketTipi;

    @Column(name = "bolge_kodu")
    private String bolgeKodu;

    @Column(name = "bolge_adi")
    private String bolgeAdi;



    public String getCariKod() {
        return cariKod;
    }

    public void setCariKod(String cariKod) {
        this.cariKod = cariKod;
    }

    public String getCariUnvan() {
        return cariUnvan;
    }

    public void setCariUnvan(String cariUnvan) {
        this.cariUnvan = cariUnvan;
    }

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
    }

    public String getCariHareketTipi() {
        return cariHareketTipi;
    }

    public void setCariHareketTipi(String cariHareketTipi) {
        this.cariHareketTipi = cariHareketTipi;
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
}
