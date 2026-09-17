package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.OrderRow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the OrderRow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRowRepository extends JpaRepository<OrderRow, Long> {
    List<OrderRow> findByOrder_Id(Long orderId);

    Optional<OrderRow> findByOrder_IdAndBarcodeAndStatus(Long orderId, String barcode, Boolean status);
}
