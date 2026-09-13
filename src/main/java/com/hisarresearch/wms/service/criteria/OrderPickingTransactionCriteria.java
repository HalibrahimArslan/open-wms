package com.hisarresearch.wms.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import com.hisarresearch.wms.domain.enumeration.SayimDurumu;
import com.hisarresearch.wms.domain.enumeration.TransactionType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hisarresearch.wms.domain.OrderPickingTransaction} entity. This class is used
 * in {@link com.hisarresearch.wms.web.rest.OrderPickingTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-picking-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderPickingTransactionCriteria implements Serializable, Criteria {

    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {}

        public TransactionTypeFilter(OrderPickingTransactionCriteria.TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public OrderPickingTransactionCriteria.TransactionTypeFilter copy() {
            return new OrderPickingTransactionCriteria.TransactionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter referenceId;

    private LongFilter addressId;

    private BooleanFilter status;

    private DoubleFilter transactionAmount;

    private TransactionTypeFilter transactionType;

    private InstantFilter createdDate;

    private StringFilter createdBy;

    private InstantFilter lastModifiedDate;

    private StringFilter lastModifiedBy;

    public OrderPickingTransactionCriteria() {}

    public OrderPickingTransactionCriteria(OrderPickingTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.referenceId = other.referenceId == null ? null : other.referenceId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.transactionAmount = other.transactionAmount == null ? null : other.transactionAmount.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
    }

    @Override
    public OrderPickingTransactionCriteria copy() {
        return new OrderPickingTransactionCriteria(this);
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

    public LongFilter getReferenceId() {
        return referenceId;
    }

    public LongFilter referenceId() {
        if (referenceId == null) {
            referenceId = new LongFilter();
        }
        return referenceId;
    }

    public void setReferenceId(LongFilter referenceId) {
        this.referenceId = referenceId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public BooleanFilter status() {
        if (status == null) {
            status = new BooleanFilter();
        }
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public DoubleFilter getTransactionAmount() {
        return transactionAmount;
    }

    public DoubleFilter transactionAmount() {
        if (transactionAmount == null) {
            transactionAmount = new DoubleFilter();
        }
        return transactionAmount;
    }

    public void setTransactionAmount(DoubleFilter transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderPickingTransactionCriteria that = (OrderPickingTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(referenceId, that.referenceId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(transactionAmount, that.transactionAmount) &&
                Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            referenceId,
            addressId,
            status,
            transactionAmount,
            transactionType,
            createdDate,
            createdBy,
            lastModifiedDate,
            lastModifiedBy
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderPickingTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (referenceId != null ? "referenceId=" + referenceId + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (transactionAmount != null ? "transactionAmount=" + transactionAmount + ", " : "") +
            (createdDate != null ? "createDate=" + createdDate + ", " : "") +
            (createdBy != null ? "createUser=" + createdBy + ", " : "") +
            (lastModifiedDate != null ? "lastUpdateDate=" + lastModifiedDate + ", " : "") +
            (lastModifiedBy != null ? "lastUpdateUser=" + lastModifiedBy + ", " : "") +
            "}";
    }
}
