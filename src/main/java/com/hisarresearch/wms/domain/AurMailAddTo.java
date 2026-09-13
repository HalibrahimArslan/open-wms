package com.hisarresearch.wms.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aur_mail_add_to")
public class AurMailAddTo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurMailAddToGenerator")
    @SequenceGenerator(name = "aurMailAddToGenerator",sequenceName = "aur_mail_add_to_seq",allocationSize = 1)
    private Long id;

    @Column(name = "mail_adres")
    private String mailAdres;

    @Column(name = "company_code")
    private Integer companyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMailAdres() {
        return mailAdres;
    }

    public void setMailAdres(String mailAdres) {
        this.mailAdres = mailAdres;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }
}
