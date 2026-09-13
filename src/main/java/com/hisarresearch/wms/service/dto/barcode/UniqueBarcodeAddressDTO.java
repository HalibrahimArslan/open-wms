package com.hisarresearch.wms.service.dto.barcode;

public class UniqueBarcodeAddressDTO {

    private String erpBarkod;
    private String companyCode;
    private Long addressId;
    private String depoCode;
    private String stokKod;
    private String stokAdi;

    public String getErpBarkod() { return erpBarkod; }
    public void setErpBarkod(String erpBarkod) { this.erpBarkod = erpBarkod; }

    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public String getDepoCode() { return depoCode; }
    public void setDepoCode(String depoCode) { this.depoCode = depoCode; }

    public String getStokKod() { return stokKod; }
    public void setStokKod(String stokKod) { this.stokKod = stokKod; }

    public String getStokAdi() { return stokAdi; }
    public void setStokAdi(String stokAdi) { this.stokAdi = stokAdi; }
}
