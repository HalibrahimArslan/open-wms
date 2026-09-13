package com.hisarresearch.wms.service.dto.feedback;

import com.hisarresearch.wms.domain.enumeration.FeedbackStatus;

public class FeedbackSaveDTO {
    private Long id;
    private String title;
    private String description;
    private FeedbackStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }
}
