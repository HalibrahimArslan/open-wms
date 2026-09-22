package com.hisarresearch.wms.service.dto.tag;

import jakarta.validation.constraints.NotNull;

public class TagDTO {
    @NotNull
    private String name;

    public TagDTO() {}

    public TagDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
