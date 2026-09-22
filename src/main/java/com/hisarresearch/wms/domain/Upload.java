package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "upload")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Upload extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uploadGenerator")
    @SequenceGenerator(name = "uploadGenerator", sequenceName = "upload_seq", allocationSize = 1)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "company_code")
    private String companyCode;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE})
    @JoinTable(name = "upload_tag_rel",
        joinColumns = { @JoinColumn(name = "upload_id",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_name", referencedColumnName = "name")}
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @OneToMany( mappedBy = "upload",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "upload" }, allowSetters = true)
    private Set<UploadComment> comments = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Set<UploadComment> getComments() {
        return comments;
    }

    public void setComments(Set<UploadComment> comments) {
        this.comments = comments;
    }
}
