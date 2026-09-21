package com.hisarresearch.wms.service.dto;

import javax.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public class LookupDTO {
    private Long id;

    @NotBlank
    private String lookupName;

    @NotBlank
    private String lookupCode;

    @Nullable
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
