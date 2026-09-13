package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.domain.enumeration.ProcessType;
import com.hisarresearch.wms.domain.enumeration.TransactionType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;

import java.io.Serializable;

public class ProcessTreeCriteria implements Serializable, Criteria {

    public static class ProcessTypeFilter extends Filter<ProcessType> {

        public ProcessTypeFilter() {}

        public ProcessTypeFilter(ProcessTreeCriteria.ProcessTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProcessTreeCriteria.ProcessTypeFilter copy() {
            return new ProcessTreeCriteria.ProcessTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter status;

    private LongFilter processChildIdFilter;

    public ProcessTreeCriteria(){}


    public ProcessTreeCriteria(ProcessTreeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.processChildIdFilter = other.processChildIdFilter == null ? null : other.processChildIdFilter.copy();

    }

    @Override
    public ProcessTreeCriteria copy() {
        return new ProcessTreeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public LongFilter getProcessChildIdFilter() {
        return processChildIdFilter;
    }

    public void setProcessChildIdFilter(LongFilter processChildIdFilter) {
        this.processChildIdFilter = processChildIdFilter;
    }
}
