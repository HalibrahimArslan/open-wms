package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Feedback;
import com.hisarresearch.wms.domain.Upload;
import com.hisarresearch.wms.repository.UploadRepository;
import com.hisarresearch.wms.repository.feedback.FeedbackRepository;
import com.hisarresearch.wms.service.dto.feedback.FeedbackUpdateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.exception.validation.InvalidIdException;

import java.util.List;

@Service
@Transactional
public class FeedbackService {
    private final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    private final UploadService uploadService;

    private final UploadRepository uploadRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           UploadService uploadService,UploadRepository uploadRepository) {
        this.feedbackRepository = feedbackRepository;
        this.uploadService = uploadService;
        this.uploadRepository = uploadRepository;
    }

    @Transactional
    public Feedback save(Feedback feedback) {
        log.debug("Request to save Feedback : {}", feedback);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        for(Upload upload : feedback.getUploads()) {
            upload.setFeedback(savedFeedback);
            uploadRepository.save(upload);
        }
        return savedFeedback;
    }

    @Transactional(readOnly = true)
    public List<Feedback> findAll() {
        log.debug("Request to get all Feedbacks");
        return feedbackRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Feedback findById(Long id) {
        log.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id).orElseThrow(InvalidIdException::new);
    }

    public Feedback update(FeedbackUpdateDTO feedbackUpdateDTO) {
        log.debug("Request to update Feedback : {}", feedbackUpdateDTO);
        Feedback feedback = findById(feedbackUpdateDTO.getId());
        if(feedbackUpdateDTO.getStatus() != null){
            feedback.setStatus(feedbackUpdateDTO.getStatus());
        }
        return feedbackRepository.save(feedback);
    }

}
