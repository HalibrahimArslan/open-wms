package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.domain.enumeration.FeedbackStatus;
import com.hisarresearch.wms.domain.enumeration.FeedbackTitle;
import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.Filter;
import com.hisarresearch.wms.framework.service.filter.InstantFilter;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

public class FeedbackCriteria implements Serializable, Criteria {
    /**
     * Class for filtering FeedbackStatus
     */
    public static class FeedbackStatusFilter extends Filter<FeedbackStatus> {

        public FeedbackStatusFilter() {}

        public FeedbackStatusFilter(FeedbackCriteria.FeedbackStatusFilter filter) {
            super(filter);
        }

        @Override
        public FeedbackCriteria.FeedbackStatusFilter copy() {
            return new FeedbackCriteria.FeedbackStatusFilter(this);
        }
    }
    /**
     * Class for filtering FeedbackStatus
     */
    public static class FeedbackTitleFilter extends Filter<FeedbackTitle> {

        public FeedbackTitleFilter() {}

        public FeedbackTitleFilter(FeedbackCriteria.FeedbackTitleFilter filter) {
            super(filter);
        }

        @Override
        public FeedbackCriteria.FeedbackTitleFilter copy() {
            return new FeedbackCriteria.FeedbackTitleFilter(this);
        }
    }
    private LongFilter id;
    private InstantFilter createdDate;
    private InstantFilter lastModifiedDate;
    private StringFilter createdBy;
    private StringFilter lastModifiedBy;
    private StringFilter description;
    private FeedbackStatusFilter status;
    private FeedbackTitleFilter title;


    public FeedbackCriteria() {}

    public FeedbackCriteria(FeedbackCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.title = other.title == null ? null : other.title.copy();
    }

    @Override
    public FeedbackCriteria copy() {
        return new FeedbackCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public FeedbackStatusFilter getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatusFilter status) {
        this.status = status;
    }

    public FeedbackTitleFilter getTitle() {
        return title;
    }

    public void setTitle(FeedbackTitleFilter title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackCriteria that = (FeedbackCriteria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FeedbackCriteria{" +
            "id=" + id +
            '}';
    }
}
