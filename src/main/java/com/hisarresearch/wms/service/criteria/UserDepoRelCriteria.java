package com.hisarresearch.wms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.BooleanFilter;
import com.hisarresearch.wms.framework.service.filter.DoubleFilter;
import com.hisarresearch.wms.framework.service.filter.Filter;
import com.hisarresearch.wms.framework.service.filter.FloatFilter;
import com.hisarresearch.wms.framework.service.filter.IntegerFilter;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hisarresearch.wms.domain.UserDepoRel} entity. This class is used
 * in {@link com.hisarresearch.wms.web.rest.UserDepoRelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-depo-rels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserDepoRelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private StringFilter code;

    private StringFilter name;

    public UserDepoRelCriteria() {}

    public UserDepoRelCriteria(UserDepoRelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
    }

    @Override
    public UserDepoRelCriteria copy() {
        return new UserDepoRelCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserDepoRelCriteria that = (UserDepoRelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, code, name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDepoRelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            "}";
    }
}
