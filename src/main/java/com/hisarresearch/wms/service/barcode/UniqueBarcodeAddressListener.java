package com.hisarresearch.wms.service.barcode;

import com.hisarresearch.wms.repository.barcode.UniqueBarcodeRepository;
import com.hisarresearch.wms.service.AurDepoUrunAdresStokService;
import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeState;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeAddressDTO;
import com.hisarresearch.wms.service.dto.event.UniqueBarcodeAddressUpdatedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;

@Component
public class UniqueBarcodeAddressListener {

    private final UniqueBarcodeRepository uniqueBarcodeRepository;
    private final AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    public UniqueBarcodeAddressListener(UniqueBarcodeRepository uniqueBarcodeRepository,
                                         AurDepoUrunAdresStokService aurDepoUrunAdresStokService) {
        this.uniqueBarcodeRepository = uniqueBarcodeRepository;
        this.aurDepoUrunAdresStokService = aurDepoUrunAdresStokService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAddressUpdated(UniqueBarcodeAddressUpdatedEvent event) {
        UniqueBarcodeAddressDTO dto = event.getDto();
        BigDecimal totalQuantity = uniqueBarcodeRepository.sumQuantityByProductAndAddressAndStatus(
            dto.getErpBarkod(), dto.getCompanyCode(), dto.getAddressId(), UniqueBarcodeState.IN_TEMPORARY_AREA
        );
        aurDepoUrunAdresStokService.upsertUniqueBarcodeTotalToAddress(dto, totalQuantity.doubleValue());
    }
}
