package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*; // for static metamodels
import com.hisarresearch.wms.domain.AurSayimUrun;
import com.hisarresearch.wms.repository.AurSayimUrunRepository;
import com.hisarresearch.wms.service.criteria.AurSayimUrunCriteria;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres_;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import com.hisarresearch.wms.service.mapper.CountingDetailMapper;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

/**
 * Service for executing complex queries for {@link AurSayimUrun} entities in the database.
 * The main input is a {@link AurSayimUrunCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AurSayimUrun} or a {@link Page} of {@link AurSayimUrun} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AurSayimUrunQueryService extends QueryService<AurSayimUrun> {

    private final Logger log = LoggerFactory.getLogger(AurSayimUrunQueryService.class);

    private final AurSayimUrunRepository aurSayimUrunRepository;

    private final AurPartialItemService aurPartialItemService;


    public AurSayimUrunQueryService(AurSayimUrunRepository aurSayimUrunRepository, AurPartialItemService aurPartialItemService) {
        this.aurSayimUrunRepository = aurSayimUrunRepository;
        this.aurPartialItemService = aurPartialItemService;
    }

    /**
     * Return a {@link List} of {@link AurSayimUrun} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AurSayimUrun> findByCriteria(AurSayimUrunCriteria criteria) throws BadRequestException {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurSayimUrun> specification = createSpecification(criteria);
        return aurSayimUrunRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AurSayimUrun} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AurSayimUrun> findByCriteria(AurSayimUrunCriteria criteria, Pageable page) throws BadRequestException {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurSayimUrun> specification = createSpecification(criteria);
        return aurSayimUrunRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AurSayimUrunCriteria criteria) throws BadRequestException {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurSayimUrun> specification = createSpecification(criteria);
        return aurSayimUrunRepository.count(specification);
    }

    /**
     * Function to convert {@link AurSayimUrunCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AurSayimUrun> createSpecification(AurSayimUrunCriteria criteria) throws BadRequestException {
        Specification<AurSayimUrun> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AurSayimUrun_.id));
            }
            if (criteria.getSayimUrunId() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getSayimUrunId(),
                    root -> root.join(AurSayimUrun_.address, JoinType.LEFT)
                        .get(AurDepoUrunAdres_.urunAdresId)
                ));
            }

            if (criteria.getStokAdi() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getStokAdi(),
                    root -> root.join(AurSayimUrun_.product, JoinType.LEFT)
                        .get(Product_.stokAdi)
                ));
            }

            if (criteria.getCompanyCode() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getCompanyCode(),
                    root -> root.join(AurSayimUrun_.product, JoinType.LEFT)
                        .get(Product_.id)
                        .get(ProductId_.companyCode)
                ));
            }

            if (criteria.getBarkod() != null) {
                specification = specification.and(buildSpecification(
                    criteria.getBarkod(),
                    root -> root.join(AurSayimUrun_.product, JoinType.LEFT)
                        .get(Product_.id)
                        .get(ProductId_.barkod)
                ));
                if (criteria.getCheckPartialItem() != null && criteria.getCheckPartialItem().getEquals() == true) {
                    String barcode = criteria.getBarkod().getEquals().toString();
                    Optional<AurPartialItemDTO> partialItem = aurPartialItemService.findOneByBarcode(barcode);
                    if (partialItem.isPresent()) {
                        throw new BadRequestException("Barcode " + barcode + "is a partial item. You can not read to count it");
                    }
                }
            }

            if (criteria.getStokKod() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStokKod(), AurSayimUrun_.stokKod));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AurSayimUrun_.status));
            }

            if (criteria.getAurSayimTanimId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAurSayimTanimId(),
                            root -> root.join(AurSayimUrun_.aurSayimTanim, JoinType.LEFT).get(AurSayimTanim_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
