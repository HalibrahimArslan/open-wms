package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.FeedbackComment;
import com.hisarresearch.wms.repository.feedback.FeedbackCommentRepository;
import com.hisarresearch.wms.service.dto.feedback.FeedbackCommentSaveDTO;
import com.hisarresearch.wms.service.dto.feedback.FeedbackCommentUpdateDTO;
import com.hisarresearch.wms.service.mapper.FeedbackCommentMapper;
import com.hisarresearch.wms.exception.validation.InvalidIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class FeedbackCommentService {
    private final Logger log = LoggerFactory.getLogger(FeedbackCommentService.class);

    private final FeedbackCommentRepository feedbackCommentRepository;

    private final FeedbackCommentMapper feedbackCommentMapper;

    public FeedbackCommentService(FeedbackCommentRepository feedbackCommentRepository,
                                  FeedbackCommentMapper feedbackCommentMapper) {
        this.feedbackCommentRepository = feedbackCommentRepository;
        this.feedbackCommentMapper = feedbackCommentMapper;
    }

    public FeedbackComment save(FeedbackCommentSaveDTO feedbackCommentSaveDTO) {
        log.debug("Request to save FeedbackComment : {}", feedbackCommentSaveDTO);
        FeedbackComment saveOne = feedbackCommentMapper.toEntity(feedbackCommentSaveDTO);
        return feedbackCommentRepository.save(saveOne);
    }


    @Transactional(readOnly = true)
    public FeedbackComment findById(Long id) {
        log.debug("Request to get Feedback : {}", id);
        return feedbackCommentRepository.findById(id).orElseThrow(InvalidIdException::new);
    }

    public FeedbackComment updateFeedbackComment(FeedbackCommentUpdateDTO updateDTO) {
        log.debug("Request to update Feedback Comment: {}", updateDTO);
        FeedbackComment feedbackComment = findById(updateDTO.getId());
        if(updateDTO.getContent() != null){
            feedbackComment.setContent(updateDTO.getContent());
        }
        return feedbackCommentRepository.save(feedbackComment);
    }

    public void deleteFeedbackComment(Long id) {
       log.debug("Request to delete FeedbackComment : {}", id);
       FeedbackComment feedbackComment = findById(id);
       if(feedbackComment.getParentComment() != null){
           feedbackComment.getParentComment().removeReply(feedbackComment);
       }
       feedbackCommentRepository.delete(feedbackComment);
    }

}
