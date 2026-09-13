package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.FeedbackStatus;
import com.hisarresearch.wms.domain.enumeration.FeedbackTitle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "feedback")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Feedback extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "title")
    private FeedbackTitle title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FeedbackStatus status;

    @Column(name = "description")
    private String description;

    @OneToMany( mappedBy = "feedback",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "feedback" }, allowSetters = true)
    private Set<Upload> uploads = new HashSet<>();

    @OneToMany( mappedBy = "feedback",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "feedback" }, allowSetters = true)
    private Set<FeedbackComment> comments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackTitle getTitle() {
        return title;
    }

    public void setTitle(FeedbackTitle title) {
        this.title = title;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Upload> getUploads() {
        return uploads;
    }

    public void setUploads(Set<Upload> uploads) {
        this.uploads = uploads;
    }

    public Set<FeedbackComment> getComments() {
        return comments;
    }

    public void setComments(Set<FeedbackComment> comments) {
        this.comments = comments;
    }
}
