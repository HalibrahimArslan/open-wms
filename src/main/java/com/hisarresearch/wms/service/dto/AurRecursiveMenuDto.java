package com.hisarresearch.wms.service.dto;

import java.util.List;

public class AurRecursiveMenuDto {

    private String id;
    private String name;
    private String path;
    private Boolean index;
    private Integer companyCode;
    private String icon;
    private List<AurRecursiveMenuDto> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AurRecursiveMenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<AurRecursiveMenuDto> children) {
        this.children = children;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIndex() {
        return index;
    }

    public void setIndex(Boolean index) {
        this.index = index;
    }

    public Integer getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
