package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.Warehouse_;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.criteria.WarehouseCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class WarehouseQueryService extends QueryService<Warehouse> {
    private final Logger log = LoggerFactory.getLogger(WarehouseQueryService.class);


    private final WarehouseRepository warehouseRepository;

    public WarehouseQueryService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional(readOnly = true)
    public Page<Warehouse> findByCriteria(WarehouseCriteria criteria, Pageable page){
        log.debug("find By Criteria : {} ,page : {}",criteria,page);
        final Specification<Warehouse> specification = createSpecification(criteria);
        return warehouseRepository.findAll(specification,page);

    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WarehouseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Warehouse> specification = createSpecification(criteria);
        return warehouseRepository.count(specification);
    }

    private Specification<Warehouse> createSpecification(WarehouseCriteria criteria) {
        Specification<Warehouse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Warehouse_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Warehouse_.name));
            }
            if(criteria.getReal() != null){
                specification = specification.and(buildSpecification(criteria.getReal(), Warehouse_.isReal));
            }
            if(criteria.getCompanyCode() != null){
                specification = specification.and(buildStringSpecification(criteria.getCompanyCode(), Warehouse_.companyCode));
            }


        }
        return specification;
    }
}
