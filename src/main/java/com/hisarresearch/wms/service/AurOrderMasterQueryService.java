package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.repository.AurOrderMasterRepository;
import com.hisarresearch.wms.repository.CustomerAddressRepository;
import com.hisarresearch.wms.service.criteria.OrderMasterCriteria;
import com.hisarresearch.wms.service.dto.AurOrderMasterQueryDTO;
import com.hisarresearch.wms.service.mapper.CustomerAddressMapper;
import com.hisarresearch.wms.service.mapper.AurOrderMasterQueryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

import jakarta.persistence.criteria.JoinType;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AurOrderMasterQueryService extends QueryService<AurOrderMaster> {

    private final Logger log = LoggerFactory.getLogger(AurOrderMasterQueryService.class);

    private final AurOrderMasterRepository orderMasterRepository;
    private final AurOrderMasterQueryMapper aurOrderMasterQueryMapper;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerAddressMapper customerAddressMapper;

    public AurOrderMasterQueryService(AurOrderMasterRepository orderMasterRepository,
                                      AurOrderMasterQueryMapper aurOrderMasterQueryMapper,
                                      CustomerAddressRepository customerAddressRepository,
                                      CustomerAddressMapper customerAddressMapper) {
        this.orderMasterRepository = orderMasterRepository;
        this.aurOrderMasterQueryMapper = aurOrderMasterQueryMapper;
        this.customerAddressRepository = customerAddressRepository;
        this.customerAddressMapper = customerAddressMapper;
    }

    @Transactional(readOnly = true)
    public List<AurOrderMasterQueryDTO> findByCriteria(OrderMasterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurOrderMaster> specification = createSpecification(criteria);

        return orderMasterRepository.findAll(specification).stream()
            .map(aurOrderMasterQueryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AurOrderMasterQueryDTO> findByCriteria(OrderMasterCriteria criteria, Pageable page) {

        log.debug("find by criteria : {}, page: {}", criteria, page);

        Specification<AurOrderMaster> specification = createSpecification(criteria);

        Specification<AurOrderMaster> distinctWrapper = (root, query, cb) -> {
            query.distinct(true);
            return specification.toPredicate(root, query, cb);
        };

        Page<AurOrderMaster> pageList = orderMasterRepository.findAll(distinctWrapper, page);

        List<Long> ids = pageList.getContent().stream()
            .map(AurOrderMaster::getId)
            .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return pageList.map(aurOrderMasterQueryMapper::toDto);
        }

        List<AurOrderMaster> ordersWithDetails = orderMasterRepository.findByIdIn(ids);
        Map<Long, AurOrderMaster> map = ordersWithDetails.stream()
            .collect(Collectors.toMap(AurOrderMaster::getId, x -> x));

        List<AurOrderMaster> orderedList = ids.stream()
            .map(map::get)
            .collect(Collectors.toList());

        List<AurOrderMasterQueryDTO> dtoList = orderedList.stream().map(order -> {

            AurOrderMasterQueryDTO dto = aurOrderMasterQueryMapper.toDto(order);

            customerAddressRepository
                .findByCariCodeAndAddressId(order.getFirmCode(), order.getSevkAddressId())
                .ifPresent(address -> dto.setCustomerAddress(customerAddressMapper.toDto(address)));

            return dto;

        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, page, pageList.getTotalElements());
    }

    @Transactional(readOnly = true)
    public long countByCriteria(OrderMasterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurOrderMaster> specification = createSpecification(criteria);
        return orderMasterRepository.count(specification);
    }

    protected Specification<AurOrderMaster> createSpecification(OrderMasterCriteria criteria) {

        Specification<AurOrderMaster> specification = Specification.where(null);

        if (criteria != null) {

            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AurOrderMaster_.id));
            }

            if (criteria.getSiparisNo() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSiparisNo(),
                        root -> root.join(AurOrderMaster_.details, JoinType.INNER)
                            .get(AurOrderDetail_.siparisNo))
                );
            }

            if (criteria.getStokAdi() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStokAdi(),
                        root -> root.join(AurOrderMaster_.details, JoinType.INNER)
                            .get(AurOrderDetail_.stokAdi))
                );
            }

            if(criteria.getStokKodu() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStokKodu(),
                        root -> root.join(AurOrderMaster_.details, JoinType.INNER)
                            .get(AurOrderDetail_.stokKodu))
                );
            }

            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), AurOrderMaster_.lastModifiedDate)
                );
            }

            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(),
                        root -> root.join(AurOrderMaster_.aurUser, JoinType.LEFT).get("id"))
                );
            }

            if (criteria.getBelgeNo() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getBelgeNo(), AurOrderMaster_.belgeNo)
                );
            }

            if (criteria.getDepoNo() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDepoNo(), AurOrderMaster_.depoNo)
                );
            }

            if (criteria.getStatus() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getStatus(), AurOrderMaster_.status)
                );
            }
        }

        return specification;
    }
}
