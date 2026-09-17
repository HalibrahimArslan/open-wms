package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.ProcessTree;
import com.hisarresearch.wms.domain.ProcessTree_;
import com.hisarresearch.wms.repository.ProcessTreeRepository;
import com.hisarresearch.wms.service.criteria.ProcessTreeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProcessTreeQueryService extends QueryService<ProcessTree> {
    private final Logger log = LoggerFactory.getLogger(ProcessTreeQueryService.class);

    private final ProcessTreeRepository processTreeRepository;

    public ProcessTreeQueryService(ProcessTreeRepository processTreeRepository) {
        this.processTreeRepository = processTreeRepository;
    }

    /**
     * Return a {@link List} of {@link ProcessTree} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessTree> findByCriteria(ProcessTreeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProcessTree> specification = createSpecification(criteria);
        return processTreeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProcessTree} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcessTree> findByCriteria(ProcessTreeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProcessTree> specification = createSpecification(criteria);
        return processTreeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProcessTreeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProcessTree> specification = createSpecification(criteria);
        return processTreeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProcessTreeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProcessTree> createSpecification(ProcessTreeCriteria criteria) {
        Specification<ProcessTree> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProcessTree_.id));
            }

        }
        return specification;
    }
}
