package com.hisarresearch.wms.service.dto.feedback;

import com.hisarresearch.wms.domain.FeedbackComment;


public class FeedbackCommentSaveDTO {
    private FeedbackSaveDTO feedback;
    private String content;
    private boolean leaf;
    private FeedbackComment parentComment;

    public FeedbackSaveDTO getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackSaveDTO feedback) {
        this.feedback = feedback;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FeedbackComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(FeedbackComment parentComment) {
        this.parentComment = parentComment;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

}
