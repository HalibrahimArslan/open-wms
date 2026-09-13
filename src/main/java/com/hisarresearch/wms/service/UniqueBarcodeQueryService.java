package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.barcode.UniqueBarcode;
import com.hisarresearch.wms.domain.barcode.UniqueBarcode_;
import com.hisarresearch.wms.repository.barcode.UniqueBarcodeRepository;
import com.hisarresearch.wms.service.criteria.UniqueBarcodeCriteria;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeResponseDTO;
import com.hisarresearch.wms.service.mapper.UniqueBarcodeMapper;
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
public class UniqueBarcodeQueryService extends QueryService<UniqueBarcode> {
    private final Logger log = LoggerFactory.getLogger(UniqueBarcodeQueryService.class);

    private final UniqueBarcodeRepository uniqueBarcodeRepository;
    private final UniqueBarcodeMapper uniqueBarcodeMapper;

    public UniqueBarcodeQueryService(UniqueBarcodeRepository uniqueBarcodeRepository, UniqueBarcodeMapper uniqueBarcodeMapper) {
        this.uniqueBarcodeRepository = uniqueBarcodeRepository;
        this.uniqueBarcodeMapper = uniqueBarcodeMapper;
    }

    @Transactional(readOnly = true)
    public Page<UniqueBarcodeResponseDTO> findByCriteria(UniqueBarcodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UniqueBarcode> specification = createSpecification(criteria);
        return uniqueBarcodeRepository.findAll(specification, page).map(uniqueBarcodeMapper::toDTO);
    }

    /**
     * Function to convert {@link UniqueBarcodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UniqueBarcode> createSpecification(UniqueBarcodeCriteria criteria) {
        Specification<UniqueBarcode> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getErpOrderInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getErpOrderInfo(), UniqueBarcode_.erpOrderInfo));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), UniqueBarcode_.barcode));
            }
        }
        return specification;
    }
}
