package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.Order;
import com.hisarresearch.wms.domain.OrderStatus_;
import com.hisarresearch.wms.domain.Order_;
import com.hisarresearch.wms.repository.OrderRepository;
import com.hisarresearch.wms.service.criteria.OrderCriteria;
import com.hisarresearch.wms.service.dto.OrderDTO;
import com.hisarresearch.wms.service.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hisarresearch.wms.framework.service.QueryService;

import javax.persistence.criteria.JoinType;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {
    private final Logger log = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderQueryService(OrderRepository orderRepository,OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Return a {@link List} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findByCriteria(OrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderMapper.toDto(orderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO> findByCriteria(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page).map(orderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Order_.id));
            }
            if(criteria.getIsTransferred() != null){
                specification = specification.and(buildSpecification(criteria.getIsTransferred(),Order_.isTransferred));
            }
            if(criteria.getEntranceWarehosue() != null){
                specification = specification.and(buildRangeSpecification(criteria.getEntranceWarehosue(),Order_.entranceWarehosue));
            }
            if(criteria.getTransferWarehouse() != null){
                specification = specification.or(buildRangeSpecification(criteria.getTransferWarehouse(),Order_.transferWarehouse));
            }
            if(criteria.getOrderstatus() != null){
                specification = specification.and(buildSpecification(criteria.getOrderstatus(),root -> root.join(Order_.orderStatus, JoinType.INNER).get(OrderStatus_.id)));
            }
            if(criteria.getDocumentType() != null){
                specification = specification.and(buildRangeSpecification(criteria.getDocumentType(),Order_.documentType));
            }
        }
        return specification;
    }
}
