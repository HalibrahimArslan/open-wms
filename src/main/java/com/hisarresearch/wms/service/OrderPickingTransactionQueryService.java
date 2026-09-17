package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.OrderPickingTransaction;
import com.hisarresearch.wms.domain.OrderPickingTransaction_;
import com.hisarresearch.wms.repository.OrderPickingTransactionRepository;
import com.hisarresearch.wms.service.criteria.OrderPickingTransactionCriteria;


import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceDTO;
import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceDetailDTO;
import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrderPickingTransaction} entities in the database.
 * The main input is a {@link OrderPickingTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderPickingTransaction} or a {@link Page} of {@link OrderPickingTransaction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderPickingTransactionQueryService extends QueryService<OrderPickingTransaction> {

    private final Logger log = LoggerFactory.getLogger(OrderPickingTransactionQueryService.class);

    private final OrderPickingTransactionRepository orderPickingTransactionRepository;



    public OrderPickingTransactionQueryService(OrderPickingTransactionRepository orderPickingTransactionRepository) {
        this.orderPickingTransactionRepository = orderPickingTransactionRepository;
    }

    /**
     * Return a {@link List} of {@link OrderPickingTransaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderPickingTransaction> findByCriteria(OrderPickingTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderPickingTransaction> specification = createSpecification(criteria);
        return orderPickingTransactionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrderPickingTransaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderPickingTransaction> findByCriteria(OrderPickingTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderPickingTransaction> specification = createSpecification(criteria);
        return orderPickingTransactionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderPickingTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderPickingTransaction> specification = createSpecification(criteria);
        return orderPickingTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderPickingTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderPickingTransaction> createSpecification(OrderPickingTransactionCriteria criteria) {
        Specification<OrderPickingTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderPickingTransaction_.id));
            }
            if (criteria.getReferenceId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReferenceId(), OrderPickingTransaction_.referenceId));
            }

            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderPickingTransaction_.status));
            }
            if (criteria.getTransactionAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionAmount(), OrderPickingTransaction_.transactionAmount));
            }
            if (criteria.getTransactionType() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTransactionType(), OrderPickingTransaction_.transactionType));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), OrderPickingTransaction_.createdDate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), OrderPickingTransaction_.createdBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), OrderPickingTransaction_.lastModifiedDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), OrderPickingTransaction_.lastModifiedBy));
            }
        }
        return specification;
    }

    public List<UserPerformanceResponseDTO> getUserPerformance(String startDate, String endDate, String createdBy, Integer companyCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startDate == null || startDate.isBlank()) {
            startDate = LocalDateTime.now().minusMonths(1).format(formatter);
        }

        if (endDate == null || endDate.isBlank()) {
            endDate = LocalDateTime.now().format(formatter);
        }

        if (createdBy == null) {
            createdBy = "";
        }
        List<UserPerformanceDTO> sum = orderPickingTransactionRepository.getUserPerformance(startDate, endDate, createdBy, companyCode);
        List<UserPerformanceDetailDTO> detay = orderPickingTransactionRepository.getUserPerformanceDetail(startDate, endDate, createdBy, companyCode);

        Map<String, List<UserPerformanceDetailDTO>> detailMap = detay.stream()
            .collect(Collectors.groupingBy(dto ->
                Optional.ofNullable(dto.getKullanici()).orElse("Unknown")));

        List<UserPerformanceResponseDTO> responseList = sum.stream()
            .map(dto -> {
                UserPerformanceResponseDTO responseDTO = new UserPerformanceResponseDTO(dto);
                String key = Optional.ofNullable(dto.getKullanici()).orElse("Unknown");
                List<UserPerformanceDetailDTO> userDetails =
                    new ArrayList<>(detailMap.getOrDefault(key, Collections.emptyList()));
                responseDTO.getDetaylar().addAll(userDetails);
                return responseDTO;
            })
            .collect(Collectors.toList());

        return responseList;
    }

    public List<UserPerformanceDTO> getAllUserPerformance(Integer companyCode) {
        String start = String.valueOf(LocalDateTime.now().minusMonths(1));
        String end = String.valueOf(LocalDateTime.now());
        return orderPickingTransactionRepository.getUserPerformance(start, end, "", companyCode);
    }
}
