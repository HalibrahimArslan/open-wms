package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*; // for static metamodels
import com.hisarresearch.wms.domain.UserDepoRel;
import com.hisarresearch.wms.repository.UserDepoRelRepository;
import com.hisarresearch.wms.service.criteria.UserDepoRelCriteria;
import com.hisarresearch.wms.service.dto.UserDepoRelDTO;
import com.hisarresearch.wms.service.mapper.UserDepoRelMapper;
import java.util.List;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

/**
 * Service for executing complex queries for {@link UserDepoRel} entities in the database.
 * The main input is a {@link UserDepoRelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserDepoRelDTO} or a {@link Page} of {@link UserDepoRelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserDepoRelQueryService extends QueryService<UserDepoRel> {

    private final Logger log = LoggerFactory.getLogger(UserDepoRelQueryService.class);

    private final UserDepoRelRepository userDepoRelRepository;

    private final UserDepoRelMapper userDepoRelMapper;

    public UserDepoRelQueryService(UserDepoRelRepository userDepoRelRepository, UserDepoRelMapper userDepoRelMapper) {
        this.userDepoRelRepository = userDepoRelRepository;
        this.userDepoRelMapper = userDepoRelMapper;
    }

    /**
     * Return a {@link List} of {@link UserDepoRelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDepoRelDTO> findByCriteria(UserDepoRelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserDepoRel> specification = createSpecification(criteria);
        return userDepoRelMapper.toDto(userDepoRelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserDepoRelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDepoRel> findByCriteria(UserDepoRelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserDepoRel> specification = createSpecification(criteria);
        return userDepoRelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserDepoRelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserDepoRel> specification = createSpecification(criteria);
        return userDepoRelRepository.count(specification);
    }

    /**
     * Function to convert {@link UserDepoRelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserDepoRel> createSpecification(UserDepoRelCriteria criteria) {
        Specification<UserDepoRel> specification = Specification.unrestricted();
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserDepoRel_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserDepoRel_.user,JoinType.LEFT).get(User_.id)
                    ));
            }
        }
        return specification;
    }
}
