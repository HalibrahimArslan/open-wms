package com.hisarresearch.wms.repository.barcode;

import com.hisarresearch.wms.domain.barcode.PalletBarcode;
import com.hisarresearch.wms.domain.enumeration.PalletBarcodeStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PalletBarcodeRepository extends JpaRepository<PalletBarcode, Long> {

    Optional<PalletBarcode> findByBarcode(String barcode);

    Optional<PalletBarcode> findTopByOrderByIdDesc();

    @EntityGraph(attributePaths = {"details", "details.aurTmpDetail","details.aurOrder","details.aurOrder.customerAddress"})
    List<PalletBarcode> findByPalletBarcodeStatus(PalletBarcodeStatus palletBarcodeStatus);
}
