package com.hisarresearch.wms.service.barcode.statemachine;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.repository.barcode.UniqueBarcodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class UniqueBarcodeSMConfig
    extends EnumStateMachineConfigurerAdapter<UniqueBarcodeState, UniqueBarcodeEvent> {

    private static final Logger log = LoggerFactory.getLogger(UniqueBarcodeSMConfig.class);

    private final UniqueBarcodeRepository repository;

    public UniqueBarcodeSMConfig(UniqueBarcodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void configure(StateMachineStateConfigurer<UniqueBarcodeState, UniqueBarcodeEvent> states) throws Exception {
        states.withStates()
            .initial(UniqueBarcodeState.CREATED)
            .states(EnumSet.allOf(UniqueBarcodeState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<UniqueBarcodeState, UniqueBarcodeEvent> transitions) throws Exception {
        transitions
            .withExternal()
                .source(UniqueBarcodeState.CREATED)
                .target(UniqueBarcodeState.RECEIVING_SCANNED)
                .event(UniqueBarcodeEvent.RECEIVE_SCAN)
                .guard(receiveScanGuard())
                .action(receiveScanAction())
            .and()
            .withExternal()
                .source(UniqueBarcodeState.RECEIVING_SCANNED)
                .target(UniqueBarcodeState.RECEIVING_SCANNED)
                .event(UniqueBarcodeEvent.RECEIVE_SCAN)
                .guard(receiveScanGuard())
                .action(receiveScanAction())
            .and()
            .withExternal()
                .source(UniqueBarcodeState.RECEIVING_SCANNED)
                .target(UniqueBarcodeState.CREATED)
                .event(UniqueBarcodeEvent.CANCEL_RECEIVE_SCAN)
                .guard(cancelReceiveScanGuard())
                .action(cancelReceiveScanAction())
            .and()
            .withExternal()
                .source(UniqueBarcodeState.RECEIVING_SCANNED)
                .target(UniqueBarcodeState.IN_TEMPORARY_AREA)
                .event(UniqueBarcodeEvent.TRANSFER_TO_TEMPORARY_AREA)
                .guard(transferToTemporaryAreaGuard())
                .action(transferToTemporaryAreaAction());
    }

    @Bean
    public Guard<UniqueBarcodeState, UniqueBarcodeEvent> receiveScanGuard() {
        return ctx -> {
            String barcode = (String) ctx.getMessageHeader("barcode");
            if (barcode == null || barcode.isBlank()) {
                log.warn("RECEIVE_SCAN reddedildi: barcode header eksik");
                return false;
            }
            boolean exists = repository.findByBarcode(barcode).isPresent();
            if (!exists) {
                log.warn("RECEIVE_SCAN reddedildi: barcode bulunamadı. barcode={}", barcode);
            }
            return exists;
        };
    }

    @Bean
    public Action<UniqueBarcodeState, UniqueBarcodeEvent> receiveScanAction() {
        return ctx -> {
            String barcode = (String) ctx.getMessageHeader("barcode");
            AurOrderDetail aurOrderDetail = (AurOrderDetail) ctx.getMessageHeader("aurOrderDetail");
            repository.findByBarcode(barcode).ifPresent(ub -> {
                ub.setStatus(UniqueBarcodeState.RECEIVING_SCANNED);
                ub.setAurOrderDetail(aurOrderDetail);
                repository.save(ub);
                log.info("UniqueBarcode RECEIVING_SCANNED durumuna geçti. barcode={}", barcode);
            });
        };
    }

    @Bean
    public Guard<UniqueBarcodeState, UniqueBarcodeEvent> cancelReceiveScanGuard() {
        return ctx -> {
            String barcode = (String) ctx.getMessageHeader("barcode");
            if (barcode == null || barcode.isBlank()) {
                log.warn("CANCEL_RECEIVE_SCAN reddedildi: barcode header eksik");
                return false;
            }
            return repository.findByBarcode(barcode).map(ub -> {
                if (ub.getStatus() != UniqueBarcodeState.RECEIVING_SCANNED) {
                    log.warn("CANCEL_RECEIVE_SCAN reddedildi: RECEIVING_SCANNED statüsünde değil. barcode={}, status={}", barcode, ub.getStatus());
                    return false;
                }
                return true;
            }).orElseGet(() -> {
                log.warn("CANCEL_RECEIVE_SCAN reddedildi: barcode bulunamadı. barcode={}", barcode);
                return false;
            });
        };
    }

    @Bean
    public Action<UniqueBarcodeState, UniqueBarcodeEvent> cancelReceiveScanAction() {
        return ctx -> {
            String barcode = (String) ctx.getMessageHeader("barcode");
            repository.findByBarcode(barcode).ifPresent(ub -> {
                ub.setStatus(UniqueBarcodeState.CREATED);
                ub.setAurOrderDetail(null);
                repository.save(ub);
                log.info("UniqueBarcode CREATED durumuna geri döndü. barcode={}", barcode);
            });
        };
    }

    @Bean
    public Guard<UniqueBarcodeState, UniqueBarcodeEvent> transferToTemporaryAreaGuard() {
        return ctx -> {
            String barcode = (String) ctx.getMessageHeader("barcode");
            Long addressId = (Long) ctx.getMessageHeader("addressId");
            if (barcode == null || barcode.isBlank()) {
                log.warn("TRANSFER_TO_TEMPORARY_AREA reddedildi: barcode header eksik");
                return false;
            }
            if (addressId == null) {
                log.warn("TRANSFER_TO_TEMPORARY_AREA reddedildi: addressId header eksik");
                return false;
            }
            return repository.findByBarcode(barcode).map(ub -> {
                if (ub.getStatus() != UniqueBarcodeState.RECEIVING_SCANNED) {
                    log.warn("TRANSFER_TO_TEMPORARY_AREA reddedildi: RECEIVING_SCANNED statüsünde değil. barcode={}, status={}", barcode, ub.getStatus());
                    return false;
                }
                return true;
            }).orElseGet(() -> {
                log.warn("TRANSFER_TO_TEMPORARY_AREA reddedildi: barcode bulunamadı. barcode={}", barcode);
                return false;
            });
        };
    }

    @Bean
    public Action<UniqueBarcodeState, UniqueBarcodeEvent> transferToTemporaryAreaAction() {
        return ctx -> {
            String barcode = (String) ctx.getMessageHeader("barcode");
            Long addressId = (Long) ctx.getMessageHeader("addressId");
            repository.findByBarcode(barcode).ifPresent(ub -> {
                ub.setAddress(new AurDepoUrunAdres(addressId));
                ub.setStatus(UniqueBarcodeState.IN_TEMPORARY_AREA);
                repository.save(ub);
                log.info("UniqueBarcode IN_TEMPORARY_AREA durumuna geçti. barcode={}, addressId={}", barcode, addressId);
            });
        };
    }

}
