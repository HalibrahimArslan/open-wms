package com.hisarresearch.wms.domain.sevkplani;


import com.hisarresearch.wms.domain.AbstractAuditingEntity;
import com.hisarresearch.wms.domain.enumeration.AurTmpSevkPlaniStatus;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "aur_tmp_sevk_plani")
public class AurTmpSevkPlani extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurTmpSevkPlaniGenerator")
    @SequenceGenerator(name = "aurTmpSevkPlaniGenerator", sequenceName = "aur_tmp_sevk_plani_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AurTmpSevkPlaniStatus status;

    @Column(name = "week", nullable = false)
    private int week;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "cari_kod", nullable = false)
    private String cariKod;

    @Column(name = "cari_unvan")
    private String cariUnvan;

    @Column(name = "cari_baglanti_tipi")
    private String cariBaglantiTipi;

    @Column(name = "bolge_kodu")
    private String bolgeKodu;

    @Column(name = "bolge_adi")
    private String bolgeAdi;

    @Column(name = "sip_uid")
    private String sipUid;

    @Column(name = "depo_no")
    private Integer depoNo;

    @Column(name = "evrak_seri")
    private String evrakSeri;

    @Column(name = "evrak_sira")
    private Integer evrakSira;

    @Column(name = "trans_group_code")
    private String transGroupCode;

    @Column(name = "trans_group_name")
    private String transGroupName;

    @Column(name = "onaylayan_kullanici")
    private String onaylayanKullanici;

    @Column(name = "durum")
    private String durum;

    @Column(name = "siparis_miktar")
    private Double siparisMiktar;

    @Column(name = "teslim_miktar")
    private Double teslimMiktar;

    @Column(name = "address_no")
    private Long addressNo;

    @Column(name = "barkod")
    private String barkod;

    @Column(name = "stok_adi")
    private String stokAdi;

    @Column(name = "stok_kodu")
    private String stokKodu;

    @Column(name = "stok_birimi")
    private String stokBirimi;

    @Column(name = "stok_miktar")
    private Double stokMiktar;

    @Column(name = "sevk_address")
    private String sevkAddress;

    @Column(name = "sevk_tel")
    private String sevkTel;

    @Column(name = "sevk_muhatap")
    private String sevkMuhatap;

    @Column(name = "sevk_acik_address")
    private String sevkAcikAddress;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
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

    public String getCariBaglantiTipi() {
        return cariBaglantiTipi;
    }

    public void setCariBaglantiTipi(String cariBaglantiTipi) {
        this.cariBaglantiTipi = cariBaglantiTipi;
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

    public String getSipUid() {
        return sipUid;
    }

    public void setSipUid(String sipUid) {
        this.sipUid = sipUid;
    }

    public Integer getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(Integer depoNo) {
        this.depoNo = depoNo;
    }

    public String getEvrakSeri() {
        return evrakSeri;
    }

    public void setEvrakSeri(String evrakSeri) {
        this.evrakSeri = evrakSeri;
    }

    public Integer getEvrakSira() {
        return evrakSira;
    }

    public void setEvrakSira(Integer evrakSira) {
        this.evrakSira = evrakSira;
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

    public String getOnaylayanKullanici() {
        return onaylayanKullanici;
    }

    public void setOnaylayanKullanici(String onaylayanKullanici) {
        this.onaylayanKullanici = onaylayanKullanici;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
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

    public Long getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(Long addressNo) {
        this.addressNo = addressNo;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getStokBirimi() {
        return stokBirimi;
    }

    public void setStokBirimi(String stokBirimi) {
        this.stokBirimi = stokBirimi;
    }

    public Double getStokMiktar() {
        return stokMiktar;
    }

    public void setStokMiktar(Double stokMiktar) {
        this.stokMiktar = stokMiktar;
    }

    public String getSevkAddress() {
        return sevkAddress;
    }

    public void setSevkAddress(String sevkAddress) {
        this.sevkAddress = sevkAddress;
    }

    public String getSevkTel() {
        return sevkTel;
    }

    public void setSevkTel(String sevkTel) {
        this.sevkTel = sevkTel;
    }

    public String getSevkMuhatap() {
        return sevkMuhatap;
    }

    public void setSevkMuhatap(String sevkMuhatap) {
        this.sevkMuhatap = sevkMuhatap;
    }

    public String getSevkAcikAddress() {
        return sevkAcikAddress;
    }

    public void setSevkAcikAddress(String sevkAcikAddress) {
        this.sevkAcikAddress = sevkAcikAddress;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setStatus(AurTmpSevkPlaniStatus status) {
        this.status = status;
    }

    public AurTmpSevkPlaniStatus getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
