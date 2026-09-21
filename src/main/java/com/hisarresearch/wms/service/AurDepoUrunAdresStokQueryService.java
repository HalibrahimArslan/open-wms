package com.hisarresearch.wms.service;



import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok_;
import com.hisarresearch.wms.repository.address.AurDepoUrunAdresStokRepository;
import com.hisarresearch.wms.service.criteria.AurDepoUrunAdresStokCriteria;
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
public class AurDepoUrunAdresStokQueryService extends QueryService<AurDepoUrunAdresStok> {
    private final Logger log = LoggerFactory.getLogger(AurDepoUrunAdresStokQueryService.class);

    private final AurDepoUrunAdresStokRepository aurDepoUrunAdresStokRepository;

    public AurDepoUrunAdresStokQueryService(AurDepoUrunAdresStokRepository aurDepoUrunAdresStokRepository){
        this.aurDepoUrunAdresStokRepository = aurDepoUrunAdresStokRepository;

    }

    @Transactional(readOnly = true)
    public List<AurDepoUrunAdresStok> findByCriteria(AurDepoUrunAdresStokCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurDepoUrunAdresStok> specification = createSpecification(criteria);
        return aurDepoUrunAdresStokRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<AurDepoUrunAdresStok> findByCriteria(AurDepoUrunAdresStokCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurDepoUrunAdresStok> specification = createSpecification(criteria);
        return aurDepoUrunAdresStokRepository.findAll(specification, page);
    }


    protected Specification<AurDepoUrunAdresStok> createSpecification(AurDepoUrunAdresStokCriteria criteria) {
        Specification<AurDepoUrunAdresStok> specification = Specification.unrestricted();
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AurDepoUrunAdresStok_.id));
            }
            if(criteria.getUrunAdresId() != null){
                specification = specification.and(buildRangeSpecification(criteria.getUrunAdresId(), AurDepoUrunAdresStok_.urunAdresId));
            }
            if(criteria.getDepoCode() != null){
                specification = specification.and(buildStringSpecification(criteria.getDepoCode(), AurDepoUrunAdresStok_.depoCode));
            }

        }

        return specification;
    }
}
