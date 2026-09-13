package com.hisarresearch.wms.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AurMenu.
 */
@Entity
@Table(name = "aur_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurMenuGenerator")
    @SequenceGenerator(name = "aurMenuGenerator",sequenceName = "aur_menu_seq",allocationSize = 1)
    private Long id;


    @Column(name = "parent_menu_id")
    private Integer parentMenuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_type")
    private String menuType;

    @Column(name = "company_code")
    private Integer companyCode;

    @Column(name = "path")
    private String path;

    @Column(name = "index")
    private Boolean index;

    @Column(name = "icon")
    private String icon;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AurMenu id(Long id) {
        this.id = id;
        return this;
    }


    public Integer getParentMenuId() {
        return this.parentMenuId;
    }

    public AurMenu parentMenuId(Integer parentMenuId) {
        this.parentMenuId = parentMenuId;
        return this;
    }

    public void setParentMenuId(Integer parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public AurMenu menuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return this.menuType;
    }

    public AurMenu menuType(String menuType) {
        this.menuType = menuType;
        return this;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Integer getCompanyCode() {
        return this.companyCode;
    }

    public AurMenu companyCode(Integer companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurMenu)) {
            return false;
        }
        return id != null && id.equals(((AurMenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurMenu{" +
            "id=" + getId() +
            ", parentMenuId=" + getParentMenuId() +
            ", menuName='" + getMenuName() + "'" +
            ", menuType='" + getMenuType() + "'" +
            ", companyCode=" + getCompanyCode() +
            "}";
    }
}
