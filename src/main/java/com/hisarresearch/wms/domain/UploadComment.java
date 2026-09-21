package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "upload_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UploadComment extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uploadCommentGenerator")
    @SequenceGenerator(name = "uploadCommentGenerator", sequenceName = "upload_comment_seq", allocationSize = 1)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "upload_id")
    @JsonIgnore
    private Upload upload;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    private UploadComment parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<UploadComment> replies;

    public UploadComment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public UploadComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(UploadComment parentComment) {
        this.parentComment = parentComment;
    }

    public List<UploadComment> getReplies() {
        return replies;
    }

    public void setReplies(List<UploadComment> replies) {
        this.replies = replies;
    }
}
