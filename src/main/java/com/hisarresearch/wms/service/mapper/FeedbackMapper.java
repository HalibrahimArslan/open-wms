package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Feedback;
import com.hisarresearch.wms.service.dto.feedback.FeedbackSaveDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {})
public interface FeedbackMapper extends EntityMapper<FeedbackSaveDTO, Feedback> {
}
