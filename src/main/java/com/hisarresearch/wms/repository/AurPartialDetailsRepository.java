package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurPartialDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AurPartialDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurPartialDetailsRepository extends JpaRepository<AurPartialDetails, Long> {
    List<AurPartialDetails> findByAurPartialItem_Id(Long aurPartialItemId);

    Optional<AurPartialDetails> findByBarcodeAndAurPartialItem_Id(String barcode,Long aurPartialItemId);

    List<AurPartialDetails> findByBarcodeIn(List<String> barcodeList);

    @Modifying
    @Query("delete from AurPartialDetails d where d.aurPartialItem.id = :partialItemId")
    int deleteByPartialItemId(@Param("partialItemId") Long partialItemId);
}
