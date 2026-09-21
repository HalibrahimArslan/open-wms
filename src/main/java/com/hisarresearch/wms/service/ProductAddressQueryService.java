package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.ProductAddressv2;
import com.hisarresearch.wms.domain.ProductAddressv2_;
import com.hisarresearch.wms.domain.ProductId_;
import com.hisarresearch.wms.domain.Product_;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres_;
import com.hisarresearch.wms.repository.ProductAddressv2Repository;
import com.hisarresearch.wms.service.criteria.ProductAddressCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

import jakarta.persistence.criteria.JoinType;
import java.util.List;

@Service
@Transactional
public class ProductAddressQueryService extends QueryService<ProductAddressv2> {
    private final Logger log = LoggerFactory.getLogger(ProductAddressQueryService.class);

    private final ProductAddressv2Repository productAddressv2Repository;

    public ProductAddressQueryService(ProductAddressv2Repository productAddressv2Repository) {
        this.productAddressv2Repository = productAddressv2Repository;
    }

    @Transactional(readOnly = true)
    public List<ProductAddressv2> findByCriteria(ProductAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductAddressv2> specification = createSpecification(criteria);
        return productAddressv2Repository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<ProductAddressv2> findByCriteria(ProductAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductAddressv2> specification = createSpecification(criteria);
        return productAddressv2Repository.findAll(specification, page);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(ProductAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductAddressv2> specification = createSpecification(criteria);
        return productAddressv2Repository.count(specification);
    }

    protected Specification<ProductAddressv2> createSpecification(ProductAddressCriteria criteria) {
        Specification<ProductAddressv2> specification = Specification.unrestricted();
        if (criteria != null) {
            if (criteria.getDepoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepoCode(), ProductAddressv2_.depoCode));
            }
            if (criteria.getCompanyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyCode(), ProductAddressv2_.companyCode));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getAddressId(),
                    root -> root.join(ProductAddressv2_.urunAdres, JoinType.LEFT)
                        .get(AurDepoUrunAdres_.urunAdresId)
                ));
            }
            if (criteria.getStokKod() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStokKod(), ProductAddressv2_.stokKod));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getBarcode(),
                    root -> root.join(ProductAddressv2_.product, JoinType.LEFT)
                        .get(Product_.id).get(ProductId_.barkod)
                ));
            }
            if (criteria.getStokAdi() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getStokAdi(),
                    root -> root.join(ProductAddressv2_.product, JoinType.LEFT)
                        .get(Product_.stokAdi)
                ));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getStatus(), ProductAddressv2_.status
                ));
            }
            if (criteria.getTemporaryAddress() != null) {
                if (!getAllAddressType(criteria)) {
                    specification = specification.and(buildSpecification(
                        criteria.getTemporaryAddress(),
                        root -> root.join(ProductAddressv2_.urunAdres, JoinType.INNER).get(AurDepoUrunAdres_.geciciAdres)));
                }
            }
            if (criteria.getPickingAddress() != null) {
                if (!getAllAddressType(criteria)) {
                    specification = specification.and(buildSpecification(
                        criteria.getPickingAddress(),
                        root -> root.join(ProductAddressv2_.urunAdres, JoinType.INNER).get(AurDepoUrunAdres_.toplamaGozu)));
                }
            }
            if (criteria.getControlAddress() != null) {
                if (!getAllAddressType(criteria)) {
                    specification = specification.and(buildSpecification(
                        criteria.getControlAddress(),
                        root -> root.join(ProductAddressv2_.urunAdres, JoinType.INNER).get(AurDepoUrunAdres_.kontrolAdres))
                    );
                }

            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getAddress(),
                    root -> root.join(ProductAddressv2_.urunAdres, JoinType.INNER).get(AurDepoUrunAdres_.adres)));
            }
            if (criteria.getMultiSearch() != null) {
                specification = specification.and(
                    Specification.where(
                        buildStringSpecification(criteria.getMultiSearch(), ProductAddressv2_.stokKod)
                            .or(buildSpecification(
                                criteria.getMultiSearch(),
                                root -> root.join(ProductAddressv2_.product, JoinType.LEFT)
                                    .get(Product_.stokAdi)
                            ))
                            .or(buildSpecification(
                                criteria.getMultiSearch(),
                                root -> root.join(ProductAddressv2_.product, JoinType.LEFT)
                                    .get(Product_.id).get(ProductId_.barkod)
                            ))
                            .or(buildSpecification(
                                criteria.getMultiSearch(),
                                root -> root.join(ProductAddressv2_.urunAdres, JoinType.LEFT)
                                    .get(AurDepoUrunAdres_.adres)
                            ))
                    )
                );
            }

        }
        return specification;
    }

    boolean getAllAddressType(ProductAddressCriteria criteria) {
        if (criteria != null) {
            if (criteria.getAllAddressType() != null && criteria.getAllAddressType().getEquals() != null) {
                return criteria.getAllAddressType().getEquals();
            }
        }
        return false;
    }


}
