package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurSayimTanim_;
import com.hisarresearch.wms.domain.CountingAddressException;
import com.hisarresearch.wms.domain.CountingAddressException_;
import com.hisarresearch.wms.domain.Order_;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres_;
import com.hisarresearch.wms.repository.CountingAddressExceptionRepository;
import com.hisarresearch.wms.service.criteria.CountingAddressExceptionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

import javax.persistence.criteria.JoinType;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountingAddressExceptionQueryService extends QueryService<CountingAddressException> {
    private final Logger log = LoggerFactory.getLogger(CountingAddressExceptionQueryService.class);

    private final CountingAddressExceptionRepository countingAddressExceptionRepository;

    public CountingAddressExceptionQueryService(CountingAddressExceptionRepository countingAddressExceptionRepository) {
        this.countingAddressExceptionRepository = countingAddressExceptionRepository;
    }

    /**
     * Return a {@link List} of {@link CountingAddressException} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountingAddressException> findByCriteria(CountingAddressExceptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountingAddressException> specification = createSpecification(criteria);
        return countingAddressExceptionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CountingAddressException} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountingAddressException> findByCriteria(CountingAddressExceptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountingAddressException> specification = createSpecification(criteria);
        return countingAddressExceptionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountingAddressExceptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountingAddressException> specification = createSpecification(criteria);
        return countingAddressExceptionRepository.count(specification);
    }

    /**
     * Function to convert {@link CountingAddressExceptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountingAddressException> createSpecification(CountingAddressExceptionCriteria criteria) {
        Specification<CountingAddressException> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getCountingDefinitionId() != null) {
                specification = specification.and(buildSpecification(criteria.getCountingDefinitionId(),root -> root.join(CountingAddressException_.countingDefinition, JoinType.INNER).get(AurSayimTanim_.id)));
            }

            if(criteria.getStatus() != null){
                specification = specification.and(buildSpecification(criteria.getStatus(), CountingAddressException_.status));
            }

            if(criteria.getAddress() != null){
                specification = specification.and(buildSpecification(criteria.getAddress(),root -> root.join(CountingAddressException_.address,JoinType.LEFT).get(AurDepoUrunAdres_.adres)));
            }


        }
        return specification;
    }

}

