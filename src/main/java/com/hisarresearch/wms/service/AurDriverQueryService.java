package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurDriver;
import com.hisarresearch.wms.domain.AurDriver_;
import com.hisarresearch.wms.repository.AurDriverRepository;
import com.hisarresearch.wms.service.criteria.AurDriverCriteria;
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
public class AurDriverQueryService extends QueryService<AurDriver> {

    private final Logger log = LoggerFactory.getLogger(AurDriverQueryService.class);

    private final AurDriverRepository aurDriverRepository;

    public AurDriverQueryService(AurDriverRepository aurDriverRepository) {
        this.aurDriverRepository = aurDriverRepository;
    }

    public Page<AurDriver> findByCriteria(AurDriverCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurDriver> specification = createSpecification(criteria);
        return aurDriverRepository.findAll(specification, page);
    }

    protected Specification<AurDriver> createSpecification(AurDriverCriteria criteria) {
        Specification<AurDriver> specification = Specification.unrestricted();

        if (criteria != null) {

            if (criteria.getId() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getId(), AurDriver_.id)
                );
            }

            if (criteria.getDriverName() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDriverName(), AurDriver_.driverName)
                );
            }

            if (criteria.getIdentityNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getIdentityNumber(), AurDriver_.identityNumber)
                );
            }

            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPhoneNumber(), AurDriver_.phoneNumber)
                );
            }

            if (criteria.getLicensePlate() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLicensePlate(), AurDriver_.licensePlate)
                );
            }

            if (criteria.getTrailerPlate() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTrailerPlate(), AurDriver_.trailerPlate)
                );
            }

            if (criteria.getQuery() != null) {
                specification = specification.and(
                    Specification.where(
                        buildStringSpecification(criteria.getQuery(), AurDriver_.driverName)
                            .or(buildStringSpecification(criteria.getQuery(), AurDriver_.identityNumber))
                            .or(buildStringSpecification(criteria.getQuery(), AurDriver_.phoneNumber))
                            .or(buildStringSpecification(criteria.getQuery(), AurDriver_.licensePlate))
                            .or(buildStringSpecification(criteria.getQuery(), AurDriver_.trailerPlate))
                    )
                );
            }
        }

        return specification;
    }
}
