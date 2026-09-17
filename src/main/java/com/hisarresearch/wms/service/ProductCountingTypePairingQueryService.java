package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.repository.ProductCountingTypePairingRepository;
import com.hisarresearch.wms.service.criteria.ProductCountingTypePairingCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
@Transactional
public class ProductCountingTypePairingQueryService extends QueryService<ProductCountingTypePairing> {
    private final Logger log = LoggerFactory.getLogger(ProductCountingTypePairingQueryService.class);

    private final ProductCountingTypePairingRepository productCountingTypePairingRepository;

    public ProductCountingTypePairingQueryService(ProductCountingTypePairingRepository productCountingTypePairingRepository) {
        this.productCountingTypePairingRepository = productCountingTypePairingRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductCountingTypePairing> findByCriteria(ProductCountingTypePairingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductCountingTypePairing> specification = createSpecification(criteria);
        return productCountingTypePairingRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<ProductCountingTypePairing> findByCriteria(ProductCountingTypePairingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductCountingTypePairing> specification = createSpecification(criteria);
        return productCountingTypePairingRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(ProductCountingTypePairingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductCountingTypePairing> specification = createSpecification(criteria);
        return productCountingTypePairingRepository.count(specification);
    }


    protected Specification<ProductCountingTypePairing> createSpecification(ProductCountingTypePairingCriteria criteria) {
        Specification<ProductCountingTypePairing> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getWarehouseCode() != null) {
                specification = specification.and(buildSpecification(criteria.getWarehouseCode(), ProductCountingTypePairing_.warehouseCode));
            }
            if (criteria.getCountingType() != null) {
                specification = specification.and(buildSpecification(criteria.getCountingType(), ProductCountingTypePairing_.countingType));
            }
            if (criteria.getBarcode() != null && criteria.getCompanyCode() != null) {
                specification = specification.and((root, query, criteriaBuilder) -> {
                    Join<ProductCountingTypePairing, Product> productJoin = root.join(ProductCountingTypePairing_.product, JoinType.LEFT);
                    String barcodeValue = criteria.getBarcode().getEquals();
                    String companyCodeValue = criteria.getCompanyCode().getEquals();

                    Predicate barcodePredicate = criteriaBuilder.equal(productJoin.get(Product_.id).get(ProductId_.barkod), barcodeValue);
                    Predicate companyCodePredicate = criteriaBuilder.equal(productJoin.get(Product_.id).get(ProductId_.companyCode), companyCodeValue);

                    return criteriaBuilder.and(barcodePredicate, companyCodePredicate);
                });
            }
        }
        return specification;
    }

}
