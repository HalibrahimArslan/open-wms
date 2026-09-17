package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByOrderNoAndEntranceWarehosueAndTransferWarehouse(String orderNo,int entranceWm, int transferWm);
    Optional<Order> findByErpDocumentInfoAndEntranceWarehosueAndTransferWarehouse(String erpInfo,int entranceWm, int transferWm);
}
