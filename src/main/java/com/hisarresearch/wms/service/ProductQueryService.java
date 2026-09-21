package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres_;
import com.hisarresearch.wms.repository.ProductRepository;
import com.hisarresearch.wms.service.criteria.ProductCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<Product> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.unrestricted();
        if (criteria != null) {
            if (criteria.getBarkod() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarkod(), root -> root.get(Product_.id).get(ProductId_.barkod)));
            }
            if(criteria.getDepoCode() != null){
                specification = specification.and((root, query, builder) -> {
                    query.distinct(true);
                    Join<Product, ProductAddressv2> productAddressJoin = root.join(Product_.productAddresses, JoinType.LEFT);
                    return builder.equal(productAddressJoin.get(ProductAddressv2_.depoCode), criteria.getDepoCode().getEquals());
                });
            }
            if(criteria.getStokKodu() != null){
                specification = specification.and(buildStringSpecification(criteria.getStokKodu(), root -> root.get(Product_.stokKodu)));
            }
            if(criteria.getStokAdi() != null){
                specification = specification.and(buildStringSpecification(criteria.getStokAdi(), root -> root.get(Product_.stokAdi)));
            }

            if (criteria.getCompanyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyCode(), root -> root.get(Product_.id).get(ProductId_.companyCode)));
            }
            if (criteria.getLotBasedTracking() != null) {
                specification = specification.and(buildSpecification(criteria.getLotBasedTracking(), Product_.lotBasedTracking));
            }
            if (criteria.getMultiSearch() != null) {
                specification = specification.and(
                    Specification.where(buildStringSpecification(
                        criteria.getMultiSearch(), Product_.stokKodu))
                        .or(buildStringSpecification(
                            criteria.getMultiSearch(),
                            root -> root.get(Product_.id).get(ProductId_.barkod)))
                        .or(buildStringSpecification(
                            criteria.getMultiSearch(), Product_.stokAdi))

                );
            }
        }
        return specification;
    }

    private Specification<Product> buildStringSpecification(StringFilter filter, Function<Root<Product>, Path<String>> pathFunction) {
        return (root, query, criteriaBuilder) -> {
            if (filter.getContains() != null) {
                return criteriaBuilder.like(
                    criteriaBuilder.lower(pathFunction.apply(root)),
                    "%" + filter.getContains().toLowerCase(new Locale("tr")) + "%"
                );
            } else if (filter.getEquals() != null) {
                return criteriaBuilder.equal(pathFunction.apply(root), filter.getEquals());
            }

            return null;
        };
    }
}
