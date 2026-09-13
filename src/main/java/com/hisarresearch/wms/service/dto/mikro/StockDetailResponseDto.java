package com.hisarresearch.wms.service.dto.mikro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDetailResponseDto {

    private String stokKodu;
    private String stokAdi;
    private String barkod;
    private String stokBirimi;
    private String kategoriAdi;
    private String kategoriKodu;
    private String anagrupAdi;
    private String anagrupKodu;
    private Integer birimIciAdet;
    private String description;
    private Double depodakiMiktar;
    private double agirlik;
    private double genislik;
    private double derinlik;
    private double yukseklik;
    private double dara;

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

    public String getKategoriAdi() {
        return kategoriAdi;
    }

    public void setKategoriAdi(String kategoriAdi) {
        this.kategoriAdi = kategoriAdi;
    }

    public String getKategoriKodu() {
        return kategoriKodu;
    }

    public void setKategoriKodu(String kategoriKodu) {
        this.kategoriKodu = kategoriKodu;
    }

    public String getAnagrupAdi() {
        return anagrupAdi;
    }

    public void setAnagrupAdi(String anagrupAdi) {
        this.anagrupAdi = anagrupAdi;
    }

    public String getAnagrupKodu() {
        return anagrupKodu;
    }

    public void setAnagrupKodu(String anagrupKodu) {
        this.anagrupKodu = anagrupKodu;
    }

    public Integer getBirimIciAdet() {
        return birimIciAdet;
    }

    public void setBirimIciAdet(Integer birimIciAdet) {
        this.birimIciAdet = birimIciAdet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDepodakiMiktar() {
        return depodakiMiktar;
    }

    public void setDepodakiMiktar(Double depodakiMiktar) {
        this.depodakiMiktar = depodakiMiktar;
    }

    public void setAgirlik(double agirlik) {
        this.agirlik = agirlik;
    }

    public void setGenislik(double genislik) {
        this.genislik = genislik;
    }

    public void setDerinlik(double derinlik) {
        this.derinlik = derinlik;
    }

    public void setYukseklik(double yukseklik) {
        this.yukseklik = yukseklik;
    }

    public void setDara(double dara) {
        this.dara = dara;
    }

    public Map<String, Object> getPhysicalAttributes() {
        Map<String, Object> physicalAttributes = new LinkedHashMap<>();
        physicalAttributes.put("agirlik", agirlik);
        physicalAttributes.put("genislik", genislik);
        physicalAttributes.put("derinlik", derinlik);
        physicalAttributes.put("yukseklik", yukseklik);
        physicalAttributes.put("dara", dara);
        return physicalAttributes;
    }
}
