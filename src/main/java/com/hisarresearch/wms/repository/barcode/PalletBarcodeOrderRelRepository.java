package com.hisarresearch.wms.repository.barcode;

import com.hisarresearch.wms.domain.barcode.PalletBarcodeOrderRel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PalletBarcodeOrderRelRepository extends JpaRepository<PalletBarcodeOrderRel, Long> {
    Optional<PalletBarcodeOrderRel> findByAurOrderIdAndAurTmpDetailIdAndStatus(Long aurOrderId, Long aurTmpDetailId, Boolean status);

    List<PalletBarcodeOrderRel> findByAurOrderIdAndPalletBarcodeIdAndStatus(Long aurOrderId, Long aurTmpDetailId, Boolean status);

    List<PalletBarcodeOrderRel> findByAurOrderIdAndStatus(Long aurOrderId, Boolean status);

    List<PalletBarcodeOrderRel> findByPalletBarcodeId(Long palletBarcodeId);

    List<PalletBarcodeOrderRel> findByPalletBarcodeIdAndStatus(Long palletBarcodeId, Boolean status);

}
