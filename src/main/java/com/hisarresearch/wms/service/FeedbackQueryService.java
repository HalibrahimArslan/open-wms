package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Feedback;
import com.hisarresearch.wms.domain.Feedback_;
import com.hisarresearch.wms.repository.feedback.FeedbackRepository;
import com.hisarresearch.wms.service.criteria.FeedbackCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FeedbackQueryService extends QueryService<Feedback> {
    private final Logger log = LoggerFactory.getLogger(FeedbackQueryService.class);

    private final FeedbackRepository feedbackRepository;

    public FeedbackQueryService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional(readOnly = true)
    public List<Feedback> findByCriteria(FeedbackCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<Feedback> findByCriteria(FeedbackCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(FeedbackCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.count(specification);
    }


    protected Specification<Feedback> createSpecification(FeedbackCriteria criteria) {
        Specification<Feedback> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Feedback_.id ));
            }
            if(criteria.getCreatedDate() != null){
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Feedback_.createdDate ));
            }
            if(criteria.getCreatedBy() != null){
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Feedback_.createdBy));
            }
            if(criteria.getLastModifiedDate() != null){
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Feedback_.lastModifiedDate ));
            }
            if(criteria.getLastModifiedBy() != null){
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Feedback_.lastModifiedBy));
            }
            if(criteria.getDescription() != null){
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Feedback_.description));
            }
            if(criteria.getStatus() != null){
                specification = specification.and(buildSpecification(criteria.getStatus(), Feedback_.status));
            }
            if(criteria.getTitle() != null){
                specification = specification.and(buildSpecification(criteria.getTitle(), Feedback_.title));
            }
        }

        return specification;
    }


}
