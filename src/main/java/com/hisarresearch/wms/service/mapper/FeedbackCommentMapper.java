package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.FeedbackComment;
import com.hisarresearch.wms.service.dto.feedback.FeedbackCommentSaveDTO;
import org.mapstruct.*;


@Mapper(componentModel = "spring",uses = {FeedbackMapper.class})
public interface FeedbackCommentMapper extends EntityMapper<FeedbackCommentSaveDTO, FeedbackComment> {
}
