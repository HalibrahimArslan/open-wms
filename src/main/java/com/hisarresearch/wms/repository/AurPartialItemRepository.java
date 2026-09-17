package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurPartialItem;
import com.hisarresearch.wms.domain.OrderPickingTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AurPartialItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurPartialItemRepository extends JpaRepository<AurPartialItem, Long>,  JpaSpecificationExecutor<AurPartialItem> {
    Optional<AurPartialItem> findAllByPackageCodeAndStatusTrue(String packageCode);

    Optional<AurPartialItem> findAllByPackageBarcodeAndStatusTrue(String packageBarcode);

}
