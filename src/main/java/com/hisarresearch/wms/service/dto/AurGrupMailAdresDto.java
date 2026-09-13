package com.hisarresearch.wms.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class AurGrupMailAdresDto {
    private Long id;
    @NotNull
    private Integer grupKodu;
    @Email
    @NotNull
    private String mailAdres;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Integer getGrupKodu() {return grupKodu;}
    public void setGrupKodu(Integer grupKodu) {this.grupKodu = grupKodu;}
    public String getMailAdres() {return mailAdres;}
    public void setMailAdres(String mailAdres) {this.mailAdres = mailAdres;}
}
