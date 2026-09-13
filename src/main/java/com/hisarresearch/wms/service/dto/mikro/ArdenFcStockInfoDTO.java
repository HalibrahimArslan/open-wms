package com.hisarresearch.wms.service.dto.mikro;

public class ArdenFcStockInfoDTO {
    private int stokMiktar;
    private String stokAdi;
    private int depoNo;
    private String stokKodu;
    private String barkod;
    private String stokBirimi;
    private int koliIciAdet;
    private int rafOmru;
    private boolean sktFlag;

    public int getStokMiktar() {
        return stokMiktar;
    }

    public void setStokMiktar(int stokMiktar) {
        this.stokMiktar = stokMiktar;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public int getDepoNo() {
        return depoNo;
    }

    public void setDepoNo(int depoNo) {
        this.depoNo = depoNo;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
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

    public int getKoliIciAdet() {
        return koliIciAdet;
    }

    public void setKoliIciAdet(int koliIciAdet) {
        this.koliIciAdet = koliIciAdet;
    }

    public int getRafOmru() {
        return rafOmru;
    }

    public void setRafOmru(int rafOmru) {
        this.rafOmru = rafOmru;
    }

    public boolean isSktFlag() {
        return sktFlag;
    }

    public void setSktFlag(boolean sktFlag) {
        this.sktFlag = sktFlag;
    }
}
