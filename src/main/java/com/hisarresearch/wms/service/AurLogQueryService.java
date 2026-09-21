package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurIntegrationLogs;
import com.hisarresearch.wms.domain.AurIntegrationLogs_;
import com.hisarresearch.wms.repository.AurIntegrationLogsRepository;
import com.hisarresearch.wms.service.criteria.AurLogCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AurLogQueryService extends QueryService<AurIntegrationLogs> {
    private final Logger log = LoggerFactory.getLogger(AurLogQueryService.class);

    private final AurIntegrationLogsRepository aurIntegrationLogsRepository;

    public AurLogQueryService(AurIntegrationLogsRepository aurIntegrationLogsRepository) {
        this.aurIntegrationLogsRepository = aurIntegrationLogsRepository;
    }

    @Transactional(readOnly = true)
    public List<AurIntegrationLogs> findByCriteria(AurLogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurIntegrationLogs> specification = createSpecification(criteria);
        return aurIntegrationLogsRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<AurIntegrationLogs> findByCriteria(AurLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurIntegrationLogs> specification = createSpecification(criteria);
        return aurIntegrationLogsRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(AurLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurIntegrationLogs> specification = createSpecification(criteria);
        return aurIntegrationLogsRepository.count(specification);
    }


    protected Specification<AurIntegrationLogs> createSpecification(AurLogCriteria criteria) {
        Specification<AurIntegrationLogs> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getLogId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLogId(), AurIntegrationLogs_.logId));
            }
            if(criteria.getServiceName() != null){
                specification = specification.and(buildStringSpecification(criteria.getServiceName(), AurIntegrationLogs_.serviceName));
            }
            if(criteria.getServiceUrl() != null){
                specification = specification.and(buildStringSpecification(criteria.getServiceUrl(), AurIntegrationLogs_.serviceUrl));
            }
            if(criteria.getRequestBody() != null){
                specification = specification.and(buildStringSpecification(criteria.getRequestBody(), AurIntegrationLogs_.requestBody));
            }
            if (criteria.getResponseBody() != null ){
                specification = specification.and(buildStringSpecification(criteria.getResponseBody(), AurIntegrationLogs_.responseBody));
            }

        }

        return specification;
    }


}
