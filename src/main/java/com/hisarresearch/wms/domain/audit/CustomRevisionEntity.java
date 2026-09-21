package com.hisarresearch.wms.domain.audit;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "revinfo")
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * rev numarasi eskiden oldugu gibi hibernate_sequence'tan birer birer gelir. Hibernate 5'te
     * bunu DefaultRevisionEntity'nin ortuk AUTO uretimi sagliyordu; Hibernate 6+ ortuk sekansin
     * adini ve artis miktarini degistirdigi (ve 7'de DefaultRevisionEntity final oldugu) icin
     * alanlar ve generator burada acikca yazildi.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customRevisionEntityGenerator")
    @SequenceGenerator(name = "customRevisionEntityGenerator", sequenceName = "hibernate_sequence", allocationSize = 1)
    @RevisionNumber
    @Column(name = "rev")
    private int id;

    @RevisionTimestamp
    @Column(name = "revtstmp")
    private long timestamp;

    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

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
