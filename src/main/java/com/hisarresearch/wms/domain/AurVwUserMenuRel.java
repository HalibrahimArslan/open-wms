package com.hisarresearch.wms.domain;

import javax.persistence.*;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getUserMenu",
            query = "select menu_id, parent_menu_id, menu_name , menu_type,path,index,icon  " + "  from aur_vw_user_menu_rel " +
                " where user_name = :userName and (company_code = :companyCode or company_code is null) order by menu_id",
            resultClass = AurVwUserMenuRel.class
        ),
    }
)
public class AurVwUserMenuRel {

    @Id
    private Integer menuId;

    @Column(name = "parent_menu_id")
    private Integer parentMenuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_type")
    private String menuType;

    @Column(name = "path")
    private String path;

    @Column(name = "index")
    private Boolean index;

    @Column(name = "icon")
    private String icon;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Integer parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
