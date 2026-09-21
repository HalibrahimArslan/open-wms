package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*; // for static metamodels
import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.repository.AurSayimTanimRepository;
import com.hisarresearch.wms.service.criteria.AurSayimTanimCriteria;
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
 * Service for executing complex queries for {@link AurSayimTanim} entities in the database.
 * The main input is a {@link AurSayimTanimCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AurSayimTanim} or a {@link Page} of {@link AurSayimTanim} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AurSayimTanimQueryService extends QueryService<AurSayimTanim> {

    private final Logger log = LoggerFactory.getLogger(AurSayimTanimQueryService.class);

    private final AurSayimTanimRepository aurSayimTanimRepository;

    public AurSayimTanimQueryService(AurSayimTanimRepository aurSayimTanimRepository) {
        this.aurSayimTanimRepository = aurSayimTanimRepository;
    }

    /**
     * Return a {@link List} of {@link AurSayimTanim} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AurSayimTanim> findByCriteria(AurSayimTanimCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurSayimTanim> specification = createSpecification(criteria);
        return aurSayimTanimRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AurSayimTanim} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AurSayimTanim> findByCriteria(AurSayimTanimCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurSayimTanim> specification = createSpecification(criteria);
        return aurSayimTanimRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AurSayimTanimCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurSayimTanim> specification = createSpecification(criteria);
        return aurSayimTanimRepository.count(specification);
    }

    /**
     * Function to convert {@link AurSayimTanimCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AurSayimTanim> createSpecification(AurSayimTanimCriteria criteria) {
        Specification<AurSayimTanim> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AurSayimTanim_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AurSayimTanim_.status));
            }
            if (criteria.getSayimAdi() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSayimAdi(), AurSayimTanim_.sayimAdi));
            }
            if (criteria.getDepoNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepoNo(), AurSayimTanim_.depoNo));
            }
            if (criteria.getSayimTarihi() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSayimTarihi(), AurSayimTanim_.sayimTarihi));
            }
            if (criteria.getSayimDurumu() != null) {
                specification = specification.and(buildSpecification(criteria.getSayimDurumu(), AurSayimTanim_.sayimDurumu));
            }
            if (criteria.getCountingType() != null) {
                specification = specification.and(buildSpecification(criteria.getCountingType(), AurSayimTanim_.countingType));
            }
            if (criteria.getAurSayimUrunId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAurSayimUrunId(),
                            root -> root.join(AurSayimTanim_.aurSayimUruns, JoinType.LEFT).get(AurSayimUrun_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
