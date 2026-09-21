package com.hisarresearch.wms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import jakarta.validation.constraints.*;

/**
 * A DTO for the {@link com.hisarresearch.wms.domain.AurPartialItem} entity.
 */
public class AurPartialItemDTO implements Serializable {

    private Long id;

    private Boolean status;

    @NotNull
    private String packageCode;

    private String packageName;

    private String packageBarcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageBarcode() {
        return packageBarcode;
    }

    public void setPackageBarcode(String packageBarcode) {
        this.packageBarcode = packageBarcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AurPartialItemDTO)) {
            return false;
        }

        AurPartialItemDTO aurPartialItemDTO = (AurPartialItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aurPartialItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AurPartialItemDTO{" +
            "id=" + getId() +
            ", packageCode='" + getPackageCode() + "'" +
            ", packageName='" + getPackageName() + "'" +
            ", packageBarcode='" + getPackageBarcode() + "'" +
            "}";
    }
}
