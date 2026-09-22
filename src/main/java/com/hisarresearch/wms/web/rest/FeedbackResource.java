package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Feedback;
import com.hisarresearch.wms.domain.FeedbackComment;
import com.hisarresearch.wms.service.FeedbackCommentService;
import com.hisarresearch.wms.service.FeedbackQueryService;
import com.hisarresearch.wms.service.FeedbackService;
import com.hisarresearch.wms.service.criteria.FeedbackCriteria;
import com.hisarresearch.wms.service.dto.feedback.FeedbackCommentSaveDTO;
import com.hisarresearch.wms.service.dto.feedback.FeedbackCommentUpdateDTO;
import com.hisarresearch.wms.service.dto.feedback.FeedbackUpdateDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import com.hisarresearch.wms.framework.web.util.PaginationUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
public class FeedbackResource {
    private final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

    private static final String ENTITY_NAME = "feedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackService feedbackService;

    private final FeedbackCommentService feedbackCommentService;

    private final FeedbackQueryService feedbackQueryService;

    public FeedbackResource(FeedbackService feedbackService, FeedbackQueryService feedbackQueryService,
                            FeedbackCommentService feedbackCommentService) {
        this.feedbackService = feedbackService;
        this.feedbackQueryService = feedbackQueryService;
        this.feedbackCommentService = feedbackCommentService;
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable(value = "id") Long id) {
        log.debug("REST request to get Feedback : {}", id);
        return ResponseEntity.ok().body(feedbackService.findById(id));
    }

    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) throws URISyntaxException {
        log.debug("REST request to save Feedback : {}", feedback);
        if (feedback.getId() != null) {
            throw new BadRequestAlertException("Feedback already exists", ENTITY_NAME, "idexists");
        }
        Feedback result = feedbackService.save(feedback);
        return ResponseEntity.created(new URI("/api/feedback/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, String.valueOf(feedback.getTitle())))
            .body(result);

    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<Feedback>> getAllFeedbacks(
        FeedbackCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of Feedbacks");
        Page<Feedback> page = feedbackQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/feedbacks/count")
    public ResponseEntity<Long> countFeedbacks(FeedbackCriteria criteria) {
        log.debug("REST request to count Feedbacks");
        return ResponseEntity.ok().body(feedbackQueryService.countByCriteria(criteria));
    }

    @PostMapping("/feedback-comment")
    public ResponseEntity<FeedbackComment> saveFeedbackComment(@RequestBody FeedbackCommentSaveDTO feedbackCommentSaveDTO) throws URISyntaxException {
        log.debug("REST request to save FeedbackComment : {}", feedbackCommentSaveDTO);
        if (feedbackCommentSaveDTO.getFeedback().getId() == null) {
            throw new BadRequestAlertException("Feedback has a id property", ENTITY_NAME, "notExistsFeedback");
        }
        FeedbackComment result = feedbackCommentService.save(feedbackCommentSaveDTO);
        return ResponseEntity.created(new URI("/api/feedback-comment" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName,true,ENTITY_NAME,result.getId().toString()))
            .body(result);

    }

    @PutMapping("/feedbacks")
    public ResponseEntity<Feedback> updateFeedback(@RequestBody FeedbackUpdateDTO updateDTO) {
        log.debug("REST request to update Feedback : {}", updateDTO);
        if (updateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseEntity.ok().body(feedbackService.update(updateDTO));
    }

    @PutMapping("/feedback-comments")
    public ResponseEntity<FeedbackComment> updateFeedbackComment(@RequestBody FeedbackCommentUpdateDTO updateDTO) {
        log.debug("REST request to update Feedback Comment : {}", updateDTO);
        if (updateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return ResponseEntity.ok().body(feedbackCommentService.updateFeedbackComment(updateDTO));
    }

    @DeleteMapping("/feedback-comment/{id}")
    public ResponseEntity<Void> deleteFeedbackComment(@PathVariable(value = "id") Long id) {
        log.debug("REST request to delete FeedbackComment : {}", id);
        feedbackCommentService.deleteFeedbackComment(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
