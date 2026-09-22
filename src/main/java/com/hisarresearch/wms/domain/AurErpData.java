package com.hisarresearch.wms.domain;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aur_erp_data")
public class AurErpData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurErpDataGenerator")
    @SequenceGenerator(name = "aurErpDataGenerator",sequenceName = "aur_erp_data_seq",allocationSize = 1)
    private Long id;

    @Column(name = "sip_guid")
    private String sipGuid;

    @Column(name = "sip_depo_no")
    private Short sipDepoNo;

    @Column(name = "sip_sube_no")
    private Integer sipSubeNo;

    @Column(name = "sip_tip")
    private Short sipTip;

    @Column(name = "sip_cins")
    private Short sipCins;

    @Column(name = "sip_evrak_seri")
    private String sipEvrakSeri;

    @Column(name = "sip_evrak_sira")
    private Integer sipEvrakSira;

    @Column(name = "sip_satir_no")
    private Integer sipSatirNo;

    @Column(name = "sip_belge_no")
    private String sipBelgeno;

    @Column(name = "sip_musteri_kod")
    private String sipMusteriKod;

    @Column(name = "sip_stok_kod")
    private String sipStokKod;

    @Column(name = "stok_adi")
    private String stokAdi;

    @Column(name = "barkod")
    private String barkod;

    @Column(name = "stok_birimi")
    private String stokBirimi;

    @Column(name = "sip_miktar")
    private Double sipMiktar;

    @Column(name = "sipTeslimMiktar")
    private Double sipTeslimMiktar;

    @Column(name = "sip_tarih")
    private String sipTarih;

    @Column(name = "teslim_tarihi")
    private String teslimTarihi;

    @Column(name = "planlanan_sevk_tarihi")
    private String planlananSevkTarihi;

    @Column(name = "cari_kod")
    private String cariKod;

    @Column(name = "cari_unvan")
    private String cariUnvan;

    @Column(name = "bolge_kodu")
    private String bolgeKodu;

    @Column(name = "bolge_adi")
    private String bolgeAdi;

    @Column(name = "cari_hareket_tipi")
    private String cariHareketTipi;

    @Column(name = "cari_baglanti_tipi")
    private String cariBaglantiTipi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSipGuid() {
        return sipGuid;
    }

    public void setSipGuid(String sipGuid) {
        this.sipGuid = sipGuid;
    }

    public Short getSipDepoNo() {
        return sipDepoNo;
    }

    public void setSipDepoNo(Short sipDepoNo) {
        this.sipDepoNo = sipDepoNo;
    }

    public Integer getSipSubeNo() {
        return sipSubeNo;
    }

    public void setSipSubeNo(Integer sipSubeNo) {
        this.sipSubeNo = sipSubeNo;
    }

    public Short getSipTip() {
        return sipTip;
    }

    public void setSipTip(Short sipTip) {
        this.sipTip = sipTip;
    }

    public Short getSipCins() {
        return sipCins;
    }

    public void setSipCins(Short sipCins) {
        this.sipCins = sipCins;
    }

    public String getSipEvrakSeri() {
        return sipEvrakSeri;
    }

    public void setSipEvrakSeri(String sipEvrakSeri) {
        this.sipEvrakSeri = sipEvrakSeri;
    }

    public Integer getSipEvrakSira() {
        return sipEvrakSira;
    }

    public void setSipEvrakSira(Integer sipEvrakSira) {
        this.sipEvrakSira = sipEvrakSira;
    }

    public Integer getSipSatirNo() {
        return sipSatirNo;
    }

    public void setSipSatirNo(Integer sipSatirNo) {
        this.sipSatirNo = sipSatirNo;
    }

    public String getSipBelgeno() {
        return sipBelgeno;
    }

    public void setSipBelgeno(String sipBelgeno) {
        this.sipBelgeno = sipBelgeno;
    }

    public String getSipMusteriKod() {
        return sipMusteriKod;
    }

    public void setSipMusteriKod(String sipMusteriKod) {
        this.sipMusteriKod = sipMusteriKod;
    }

    public String getSipStokKod() {
        return sipStokKod;
    }

    public void setSipStokKod(String sipStokKod) {
        this.sipStokKod = sipStokKod;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
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

    public Double getSipMiktar() {
        return sipMiktar;
    }

    public void setSipMiktar(Double sipMiktar) {
        this.sipMiktar = sipMiktar;
    }

    public Double getSipTeslimMiktar() {
        return sipTeslimMiktar;
    }

    public void setSipTeslimMiktar(Double sipTeslimMiktar) {
        this.sipTeslimMiktar = sipTeslimMiktar;
    }

    public String getSipTarih() {
        return sipTarih;
    }

    public void setSipTarih(String sipTarih) {
        this.sipTarih = sipTarih;
    }

    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    public void setTeslimTarihi(String teslimTarihi) {
        this.teslimTarihi = teslimTarihi;
    }

    public String getPlanlananSevkTarihi() {
        return planlananSevkTarihi;
    }

    public void setPlanlananSevkTarihi(String planlananSevkTarihi) {
        this.planlananSevkTarihi = planlananSevkTarihi;
    }

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

    public String getCariHareketTipi() {
        return cariHareketTipi;
    }

    public void setCariHareketTipi(String cariHareketTipi) {
        this.cariHareketTipi = cariHareketTipi;
    }

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
    }

    @Override
    public String toString() {
        return "AurErpData{" +
            "id=" + id +
            ", sipGuid='" + sipGuid + '\'' +
            ", sipDepoNo=" + sipDepoNo +
            ", sipSubeNo=" + sipSubeNo +
            ", sipTip=" + sipTip +
            ", sipCins=" + sipCins +
            ", sipEvrakSeri='" + sipEvrakSeri + '\'' +
            ", sipEvrakSira=" + sipEvrakSira +
            ", sipSatirNo=" + sipSatirNo +
            ", sipBelgeno='" + sipBelgeno + '\'' +
            ", sipMusteriKod='" + sipMusteriKod + '\'' +
            ", sipStokKod='" + sipStokKod + '\'' +
            ", stokAdi='" + stokAdi + '\'' +
            ", barkod='" + barkod + '\'' +
            ", stokBirimi='" + stokBirimi + '\'' +
            ", sipMiktar=" + sipMiktar +
            ", sipTeslimMiktar=" + sipTeslimMiktar +
            ", sipTarih='" + sipTarih + '\'' +
            ", teslimTarihi='" + teslimTarihi + '\'' +
            ", planlananSevkTarihi='" + planlananSevkTarihi + '\'' +
            ", cariKod='" + cariKod + '\'' +
            ", cariUnvan='" + cariUnvan + '\'' +
            ", bolgeKodu='" + bolgeKodu + '\'' +
            ", bolgeAdi='" + bolgeAdi + '\'' +
            ", cariHareketTipi='" + cariHareketTipi + '\'' +
            ", cariBaglantiTipi='" + cariBaglantiTipi + '\'' +
            '}';
    }
}
