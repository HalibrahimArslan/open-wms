package com.hisarresearch.wms.domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "aur_lookup_table")
public class AurLookupTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurLookupTableGenerator")
    @SequenceGenerator(name = "aurLookupTableGenerator",sequenceName = "aur_lookup_table_seq",allocationSize = 1)
    private Long id;

    @Column(name = "lookup_name")
    private String lookupName;

    @Column(name = "lookup_code")
    private String lookupCode;

    @Column(name = "lookup_description")
    private String lookupDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLookupName() {
        return lookupName;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getLookupDescription() {
        return lookupDescription;
    }

    public void setLookupDescription(String lookupDescription) {
        this.lookupDescription = lookupDescription;
    }
}
