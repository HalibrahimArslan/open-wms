package com.hisarresearch.wms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "feedback_comment")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FeedbackComment extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonIgnore
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    private FeedbackComment parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<FeedbackComment> replies;

    @Column(name = "leaf")
    private boolean leaf;

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

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public FeedbackComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(FeedbackComment parentComment) {
        this.parentComment = parentComment;
    }

    public List<FeedbackComment> getReplies() {
        return replies;
    }

    public void setReplies(List<FeedbackComment> replies) {
        this.replies = replies;
    }

    public void addReply(FeedbackComment reply) {
        replies.add(reply);
        reply.setParentComment(this);
    }

    public void removeReply(FeedbackComment reply) {
        replies.remove(reply);
        reply.setParentComment(null);
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
