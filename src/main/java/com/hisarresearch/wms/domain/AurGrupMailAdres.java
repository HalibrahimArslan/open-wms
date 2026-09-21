package com.hisarresearch.wms.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aur_grup_mail_adres")
public class AurGrupMailAdres implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurGrupMailAdresGenerator")
    @SequenceGenerator(name = "aurGrupMailAdresGenerator",sequenceName = "aur_grup_mail_adres_seq",allocationSize = 1)
    private Long id;

    @Column(name = "grup_kodu")
    private Integer grupKodu;

    @Column(name = "mail_adres")
    private String mailAdres;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGrupKodu() {
        return grupKodu;
    }

    public void setGrupKodu(Integer grupKodu) {
        this.grupKodu = grupKodu;
    }

    public String getMailAdres() {
        return mailAdres;
    }

    public void setMailAdres(String mailAdres) {
        this.mailAdres = mailAdres;
    }

    @Override
    public String toString() {
        return "AurGrupMailAdres{" +
            "id=" + id +
            ", grupKodu=" + grupKodu +
            ", mailAdres='" + mailAdres + '\'' +
            '}';
    }
}
