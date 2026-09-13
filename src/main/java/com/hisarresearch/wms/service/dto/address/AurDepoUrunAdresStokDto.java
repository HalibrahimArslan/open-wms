package com.hisarresearch.wms.service.dto.address;

public class AurDepoUrunAdresStokDto {
    private Long urunAdresId;
    private String stokKod;
    private Boolean status;
    private String barkodTipi;
    private Long paletBarkodId;
    private String companyCode;
    private String depoCode;
    private Double miktar;
    private String barcode;


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

    public String getDepoCode() {
        return depoCode;
    }

    public void setDepoCode(String depoCode) {
        this.depoCode = depoCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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
}
