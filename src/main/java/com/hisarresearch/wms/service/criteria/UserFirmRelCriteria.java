package com.hisarresearch.wms.service.criteria;


import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.LongFilter;

import java.io.Serializable;
import java.util.Objects;

public class UserFirmRelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;


    public UserFirmRelCriteria() {}


    public UserFirmRelCriteria(UserFirmRelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
    }

    @Override
    public UserFirmRelCriteria copy() {
        return new UserFirmRelCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserFirmRelCriteria that = (UserFirmRelCriteria) o;
        return (
            Objects.equals(id, that.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDepoRelCriteria{" +
            (id != null ? "id=" + id + ", " : "") + "}";
    }
}
