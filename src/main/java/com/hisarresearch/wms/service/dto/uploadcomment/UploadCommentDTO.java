package com.hisarresearch.wms.service.dto.uploadcomment;

import java.util.List;

public class UploadCommentDTO {
    private Long id;
    private String content;
    private List<UploadCommentDTO> replies;

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

    public List<UploadCommentDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<UploadCommentDTO> replies) {
        this.replies = replies;
    }
}
