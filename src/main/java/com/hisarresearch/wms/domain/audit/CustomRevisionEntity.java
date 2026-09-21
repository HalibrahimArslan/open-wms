package com.hisarresearch.wms.domain.audit;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "revinfo")
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "rev")),
    @AttributeOverride(name = "timestamp", column = @Column(name = "revtstmp"))})
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
