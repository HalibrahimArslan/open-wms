package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.OrderRow;
import com.hisarresearch.wms.domain.enumeration.WmOperationType;
import com.hisarresearch.wms.repository.OrderRowRepository;
import com.hisarresearch.wms.service.dto.OrderRowDTO;
import com.hisarresearch.wms.service.mapper.OrderRowMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderRow}.
 */
@Service
@Transactional
public class OrderRowService {

    private final Logger log = LoggerFactory.getLogger(OrderRowService.class);

    private final OrderRowRepository orderRowRepository;

    private final OrderRowMapper orderRowMapper;

    private final CacheManager cacheManager;


    public OrderRowService(OrderRowRepository orderRowRepository, OrderRowMapper orderRowMapper,
                           CacheManager cacheManager) {
        this.orderRowRepository = orderRowRepository;
        this.orderRowMapper = orderRowMapper;
        this.cacheManager = cacheManager;
    }

    /**
     * Save a orderRow.
     *
     * @param orderRowDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderRowDTO save(OrderRowDTO orderRowDTO) {
        log.debug("Request to save OrderRow : {}", orderRowDTO);
        OrderRow orderRow = orderRowMapper.toEntity(orderRowDTO);
        orderRow = orderRowRepository.save(orderRow);
        return orderRowMapper.toDto(orderRow);
    }

    /**
     * Partially update a orderRow.
     *
     * @param orderRowDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderRowDTO> partialUpdate(OrderRowDTO orderRowDTO) {
        log.debug("Request to partially update OrderRow : {}", orderRowDTO);

        return orderRowRepository
            .findById(orderRowDTO.getId())
            .map(
                existingOrderRow -> {
                    orderRowMapper.partialUpdate(existingOrderRow, orderRowDTO);
                    return existingOrderRow;
                }
            )
            .map(orderRowRepository::save)
            .map(orderRowMapper::toDto);
    }

    /**
     * Get all the orderRows.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderRowDTO> findAll() {
        log.debug("Request to get all OrderRows");
        return orderRowRepository.findAll().stream().map(orderRowMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get related orderRows by orderId.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderRowDTO> findByOrderId(Long orderId) {
        log.debug("Request to get related OrderRows by orderId");
        return orderRowRepository.findByOrder_Id(orderId).stream().map(orderRowMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orderRow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderRowDTO> findOne(Long id) {
        log.debug("Request to get OrderRow : {}", id);
        return orderRowRepository.findById(id).map(orderRowMapper::toDto);
    }

    private void clearOrderRowCaches(OrderRow orderRow){
        Objects.requireNonNull(cacheManager.getCache("com.hisarresearch.wms.domain.OrderRow")).evict(orderRow);
    }

    /**
     * Delete the orderRow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderRow : {}", id);
        Optional<OrderRow> orderRow = orderRowRepository.findById(id);
        orderRowRepository.deleteById(id);
        orderRowRepository.flush();
        cacheManager.getCache("com.hisarresearch.wms.domain.Order.orderRows").clear();
        this.clearOrderRowCaches(orderRow.get());
    }

    /**
     *
     *
     */
    public void updateOrderRowByBarcodeAndOrderId(Long orderId,String barcode,Double updateAmount,String operationType){
        Optional<OrderRow> searchOrderRow = orderRowRepository.findByOrder_IdAndBarcodeAndStatus(orderId,barcode,true);
        if(searchOrderRow.isPresent()){
            if(WmOperationType.DEPOLAR_ARASI_SEVKIYAT.getOperationType().equals(operationType)){
                searchOrderRow.get().setTransferAmount(updateAmount);
            }
            if(WmOperationType.DEPOLAR_ARASI_KABUL.getOperationType().equals(operationType)){
                searchOrderRow.get().setReceivingAmount(updateAmount);
            }
        }

    }
}
