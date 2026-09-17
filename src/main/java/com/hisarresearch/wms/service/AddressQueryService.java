package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres_;
import com.hisarresearch.wms.repository.address.AurDepoAdresRepository;
import com.hisarresearch.wms.service.criteria.AurDepoUrunAdresCriteria;
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
public class AddressQueryService extends QueryService<AurDepoUrunAdres> {

    private final Logger log = LoggerFactory.getLogger(AddressQueryService.class);

    private final AurDepoAdresRepository aurDepoAdresRepository;

    public AddressQueryService(AurDepoAdresRepository aurDepoAdresRepository) {
        this.aurDepoAdresRepository = aurDepoAdresRepository;
    }

    @Transactional(readOnly = true)
    public List<AurDepoUrunAdres> findByCriteria(AurDepoUrunAdresCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurDepoUrunAdres> specification = createSpecification(criteria);
        return aurDepoAdresRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<AurDepoUrunAdres> findByCriteria(AurDepoUrunAdresCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurDepoUrunAdres> specification = createSpecification(criteria);
        return aurDepoAdresRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(AurDepoUrunAdresCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurDepoUrunAdres> specification = createSpecification(criteria);
        return aurDepoAdresRepository.count(specification);
    }


    protected Specification<AurDepoUrunAdres> createSpecification(AurDepoUrunAdresCriteria criteria) {
        Specification<AurDepoUrunAdres> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getUrunAdresId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUrunAdresId(), AurDepoUrunAdres_.urunAdresId));
            }
            if (criteria.getDepoNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepoNo(), AurDepoUrunAdres_.depoNo));
            }
            if (criteria.getToplamaGozu() != null) {
                specification = specification.and(buildSpecification(criteria.getToplamaGozu(), AurDepoUrunAdres_.toplamaGozu));
            }
            if (criteria.getGeciciAdres() != null) {
                specification = specification.and(buildSpecification(criteria.getGeciciAdres(), AurDepoUrunAdres_.geciciAdres));
            }
            if (criteria.getAdres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdres(), AurDepoUrunAdres_.adres));
            }
            if (criteria.getCountable() != null) {
                specification = specification.and(buildSpecification(criteria.getCountable(), AurDepoUrunAdres_.countable));
            }
            if (criteria.getKontrolAdres() != null) {
                specification = specification.and(buildSpecification(criteria.getKontrolAdres(), AurDepoUrunAdres_.kontrolAdres));
            }
            if (criteria.getKoridor() != null) {
                specification = specification.and(buildSpecification(criteria.getKoridor(), AurDepoUrunAdres_.reyon));
            }
            if (criteria.getKat() != null) {
                specification = specification.and(buildSpecification(criteria.getKoridor(), AurDepoUrunAdres_.kat));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AurDepoUrunAdres_.status));
            }

        }

        return specification;
    }
}
