package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AddressMovementHistory;
import com.hisarresearch.wms.domain.AddressMovementHistory_;
import com.hisarresearch.wms.repository.AddressMovementHistoryRepository;
import com.hisarresearch.wms.service.criteria.AddressMovementHistoryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AddressMovementHistoryQueryService extends QueryService<AddressMovementHistory> {
    private final Logger log = LoggerFactory.getLogger(AurDepoUrunAdresStokQueryService.class);
    private final AddressMovementHistoryRepository addressMovementHistoryRepository;

    public AddressMovementHistoryQueryService(AddressMovementHistoryRepository addressMovementHistoryRepository){
        this.addressMovementHistoryRepository = addressMovementHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<AddressMovementHistory> findByCriteria(AddressMovementHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AddressMovementHistory> specification = createSpecification(criteria);
        return addressMovementHistoryRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<AddressMovementHistory> findByCriteria(AddressMovementHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AddressMovementHistory> specification = createSpecification(criteria);
        return addressMovementHistoryRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(AddressMovementHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AddressMovementHistory> specification = createSpecification(criteria);
        return addressMovementHistoryRepository.count(specification);
    }

    protected Specification<AddressMovementHistory> createSpecification(AddressMovementHistoryCriteria criteria) {
        Specification<AddressMovementHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getAddressMovementType() != null) {
                specification = specification.and(buildSpecification(criteria.getAddressMovementType(), AddressMovementHistory_.movementType));
            }
            if(criteria.getCreatedDate() != null){
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(),AddressMovementHistory_.createdDate));
            }
            if(criteria.getMultiSearch() != null){
                specification = specification.and(
                    buildStringSpecification(criteria.getMultiSearch(), AddressMovementHistory_.barcode)
                        .or(buildStringSpecification(criteria.getMultiSearch(), AddressMovementHistory_.stokKodu))
                );
            }
        }

        return specification;
    }



}
