package com.hisarresearch.wms.service.dto.feedback;

import com.hisarresearch.wms.domain.enumeration.FeedbackStatus;

public class FeedbackUpdateDTO {
    private Long id;
    private FeedbackStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }
}
