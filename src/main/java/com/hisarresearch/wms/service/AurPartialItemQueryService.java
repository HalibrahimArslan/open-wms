package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.domain.AurPartialItem_;
import com.hisarresearch.wms.domain.AurSayimUrun_;
import com.hisarresearch.wms.repository.AurPartialItemRepository;
import com.hisarresearch.wms.service.criteria.AurPartialItemCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

@Service
@Transactional(readOnly = true)
public class AurPartialItemQueryService extends QueryService<AurPartialItem> {

    private final Logger log = LoggerFactory.getLogger(AurPartialItemQueryService.class);

    private final AurPartialItemRepository aurPartialItemRepository;


    public AurPartialItemQueryService(AurPartialItemRepository aurPartialItemRepository) {
        this.aurPartialItemRepository = aurPartialItemRepository;
    }

    /**
     * Return a {@link Page} of {@link AurPartialItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AurPartialItem> findByCriteria(AurPartialItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurPartialItem> specification = createSpecification(criteria);
        return aurPartialItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AurPartialItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurPartialItem> specification = createSpecification(criteria);
        return aurPartialItemRepository.count(specification);
    }

    /**
     * Function to convert {@link AurPartialItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AurPartialItem> createSpecification(AurPartialItemCriteria criteria) {
        Specification<AurPartialItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getPackageBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPackageBarcode(), AurPartialItem_.packageBarcode));
            }
            if (criteria.getPackageCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPackageCode(), AurPartialItem_.packageCode));
            }
            if (criteria.getPackageName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPackageName(), AurPartialItem_.packageName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AurPartialItem_.status));
            }


        }
        return specification;
    }

}
