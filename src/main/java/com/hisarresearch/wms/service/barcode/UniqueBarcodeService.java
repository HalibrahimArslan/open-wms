package com.hisarresearch.wms.service.barcode;


import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.barcode.UniqueBarcode;
import com.hisarresearch.wms.domain.enumeration.UnitOfMeasure;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.repository.barcode.UniqueBarcodeRepository;
import com.hisarresearch.wms.service.ProductService;
import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeEvent;
import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeState;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeAddressDTO;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeCreateDTO;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeResponseDTO;
import com.hisarresearch.wms.service.dto.event.UniqueBarcodeAddressUpdatedEvent;
import com.hisarresearch.wms.service.mapper.UniqueBarcodeMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UniqueBarcodeService {

    private final UniqueBarcodeRepository repository;
    private final UniqueBarcodeMapper mapper;
    private final ProductService productService;
    private final ApplicationEventPublisher publisher;
    private final StateMachineFactory<UniqueBarcodeState, UniqueBarcodeEvent> stateMachineFactory;

    public UniqueBarcodeService(UniqueBarcodeRepository repository, UniqueBarcodeMapper mapper,
                                ProductService productService, ApplicationEventPublisher publisher,
                                StateMachineFactory<UniqueBarcodeState, UniqueBarcodeEvent> stateMachineFactory) {
        this.repository = repository;
        this.mapper = mapper;
        this.productService = productService;
        this.publisher = publisher;
        this.stateMachineFactory = stateMachineFactory;
    }

    private static final DateTimeFormatter YYMMDD =
        DateTimeFormatter.ofPattern("yyMMdd")
            .withZone(ZoneId.systemDefault());

    private static final String LOT_BASED_RECEIVING_DATE = "000000";

    @Transactional
    public List<UniqueBarcodeResponseDTO> createUniqueBarcodes(UniqueBarcodeCreateDTO dto) {
        return createUniqueBarcodesBulk(Collections.singletonList(dto));
    }

    private List<UniqueBarcodeResponseDTO> createUniqueBarcodes(
        UniqueBarcodeCreateDTO dto, Map<String, Deque<UniqueBarcode>> reusePools) {
        UniqueBarcode uniqueBarcode = mapper.toEntity(dto);
        Product product = productService.saveProduct(uniqueBarcode.getProduct());

        UnitOfMeasure unit = UnitOfMeasure.fromValue(product.getStokBirimi());
        validateUnitOfMeasureRules(dto, product, unit);

        if (Boolean.TRUE.equals(product.getLotBasedTracking())) {
            return buildLotBasedBarcodes(dto, product);
        }

        String receivingDate = dto.getReceivingDate();
        String partiCode = product.getId().getBarkod().concat(receivingDate);

        Deque<UniqueBarcode> reusePool = reusePools.getOrDefault(partiCode, new ArrayDeque<>());

        List<UniqueBarcode> result = new ArrayList<>();

        int reuseCount = Math.min(reusePool.size(), dto.getAdet());
        for (int i = 0; i < reuseCount; i++) {
            UniqueBarcode reused = reusePool.poll();
            reused.setDescription(dto.getDescription());
            reused.setQuantity(dto.getQuantity());
            result.add(repository.save(reused));
        }

        int toCreate = dto.getAdet() - reuseCount;

        if (toCreate > 0) {
            Long currentLot = repository.findMaxLotNumberByPartiCode(partiCode);

            Instant receivingInstant =
                LocalDate.parse(receivingDate, YYMMDD)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant();

            for (int i = 1; i <= toCreate; i++) {
                Long lot = currentLot + i;

                String barcodeStr = generateUniqueBarcode(
                    product.getId().getBarkod(),
                    receivingDate,
                    dto.getCustomerCode(),
                    dto.getErpOrderNo(),
                    lot
                );

                UniqueBarcode tb = mapper.toEntity(dto);
                tb.setProduct(product);
                tb.setBarcode(barcodeStr);
                tb.setPartiCode(partiCode);
                tb.setLotNumber(lot);
                tb.setReceivingDate(receivingInstant);
                result.add(repository.save(tb));
            }
        }

        return result.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    private List<UniqueBarcodeResponseDTO> buildLotBasedBarcodes(UniqueBarcodeCreateDTO dto, Product product) {
        String barcodeStr = generateUniqueBarcode(
            product.getId().getBarkod(),
            LOT_BASED_RECEIVING_DATE,
            dto.getCustomerCode(),
            dto.getErpOrderNo(),
            0L
        );

        UniqueBarcode tb = repository.findByBarcode(barcodeStr)
            .orElseGet(() -> {
                UniqueBarcode entity = mapper.toEntity(dto);
                entity.setProduct(product);
                entity.setBarcode(barcodeStr);
                entity.setPartiCode("0000");
                entity.setLotNumber(0L);
                entity.setReceivingDate(null);
                return repository.save(entity);
            });

        UniqueBarcodeResponseDTO responseDTO = mapper.toDTO(tb);
        List<UniqueBarcodeResponseDTO> result = new ArrayList<>();
        for (int i = 0; i < dto.getAdet(); i++) {
            result.add(responseDTO);
        }
        return result;
    }

    @Transactional
    public List<UniqueBarcodeResponseDTO> createUniqueBarcodesBulk(List<UniqueBarcodeCreateDTO> dtoList) {
        Map<String, Deque<UniqueBarcode>> reusePools = prefetchReusePools(dtoList);

        List<UniqueBarcodeResponseDTO> result = new ArrayList<>();
        for (UniqueBarcodeCreateDTO dto : dtoList) {
            result.addAll(createUniqueBarcodes(dto, reusePools));
        }
        return result;
    }

    private Map<String, Deque<UniqueBarcode>> prefetchReusePools(List<UniqueBarcodeCreateDTO> dtoList) {
        Map<String, Deque<UniqueBarcode>> reusePools = new HashMap<>();
        for (UniqueBarcodeCreateDTO dto : dtoList) {
            String partiCode = dto.getProduct().getBarcode().concat(dto.getReceivingDate());
            reusePools.computeIfAbsent(partiCode, pc -> {
                repository.lockByPartiCode(pc);
                return new ArrayDeque<>(
                    repository.findAllByPartiCodeAndStatusOrderByLotNumberAsc(pc, UniqueBarcodeState.CREATED));
            });
        }
        return reusePools;
    }

    @Transactional
    public void updateToReceivingScanned(String barcode, BigDecimal miktar, AurOrderDetail aurOrderDetail, Double transactionAmount) {
        UniqueBarcode ub = repository.findByBarcode(barcode)
            .orElseThrow(() -> new BadRequestAlertException("Tekil barkod bulunamadı", "UniqueBarcode", "notFound"));

        boolean lotBasedTracking = ub.getProduct() != null && Boolean.TRUE.equals(ub.getProduct().getLotBasedTracking());

        if (!lotBasedTracking) {
            if (ub.getStatus() == UniqueBarcodeState.RECEIVING_SCANNED) {
                throw new BadRequestAlertException("Bu barkod ürün kabulde okutuldu", "UniqueBarcode", "alreadyScanned");
            }

            if (ub.getQuantity().compareTo(miktar) != 0) {
                throw new BadRequestAlertException(
                    "Gönderilen miktar tekil barkod miktarıyla eşleşmiyor. Beklenen: " + ub.getQuantity() + ", Gönderilen: " + miktar,
                    "UniqueBarcode", "quantityMismatch"
                );
            }

            if (ub.getQuantity().compareTo(BigDecimal.valueOf(transactionAmount)) != 0) {
                throw new BadRequestAlertException(
                    "İşlem miktarı tekil barkod miktarıyla eşleşmiyor. Beklenen: " + ub.getQuantity() + ", Gönderilen: " + transactionAmount,
                    "UniqueBarcode", "transactionAmountMismatch"
                );
            }
        }

        StateMachine<UniqueBarcodeState, UniqueBarcodeEvent> sm = stateMachineFactory.getStateMachine();
        Map<String, Object> headers = new HashMap<>();
        headers.put("aurOrderDetail", aurOrderDetail);
        fireEvent(sm, ub.getBarcode(), ub.getStatus(), UniqueBarcodeEvent.RECEIVE_SCAN, headers);
    }

    @Transactional
    public void cancelReceiveScanByOrderDetail(Long aurOrderDetailId) {
        List<UniqueBarcode> receivingScanned = repository
            .findAllByAurOrderDetail_IdAndStatus(aurOrderDetailId, UniqueBarcodeState.RECEIVING_SCANNED);
        if (receivingScanned.isEmpty()) {
            return;
        }
        StateMachine<UniqueBarcodeState, UniqueBarcodeEvent> sm = stateMachineFactory.getStateMachine();
        for (UniqueBarcode ub : receivingScanned) {
            fireEvent(sm, ub.getBarcode(), UniqueBarcodeState.RECEIVING_SCANNED, UniqueBarcodeEvent.CANCEL_RECEIVE_SCAN);
        }
    }

    private void fireEvent(StateMachine<UniqueBarcodeState, UniqueBarcodeEvent> sm, String barcode,
                           UniqueBarcodeState sourceState, UniqueBarcodeEvent event) {
        fireEvent(sm, barcode, sourceState, event, Collections.emptyMap());
    }

    private void fireEvent(StateMachine<UniqueBarcodeState, UniqueBarcodeEvent> sm, String barcode,
                           UniqueBarcodeState sourceState, UniqueBarcodeEvent event, Map<String, Object> extraHeaders) {
        sm.getStateMachineAccessor().doWithAllRegions(access ->
            access.resetStateMachineReactively(
                new DefaultStateMachineContext<>(sourceState, null, null, null)).block());
        sm.startReactively().block();

        MessageBuilder<UniqueBarcodeEvent> messageBuilder =
            MessageBuilder.withPayload(event).setHeader("barcode", barcode);
        extraHeaders.forEach(messageBuilder::setHeader);

        List<StateMachineEventResult<UniqueBarcodeState, UniqueBarcodeEvent>> results = sm.sendEvent(Mono.just(
            messageBuilder.build()
        )).collectList().block();

        boolean accepted = results != null && results.stream()
            .anyMatch(r -> r.getResultType() == StateMachineEventResult.ResultType.ACCEPTED);
        boolean hasError = sm.hasStateMachineError();
        sm.stopReactively().block();

        if (!accepted || hasError) {
            throw new BadRequestAlertException(
                "Tekil barkod durum geçişi başarısız: " + barcode + " (" + event + ")",
                "UniqueBarcode", "stateTransitionFailed");
        }
    }

    @Transactional
    public void transferDetailBarcodesToTemporaryArea(Long aurOrderDetailId, UniqueBarcodeAddressDTO dto) {
        List<UniqueBarcode> receivingScanned = repository
            .findAllByAurOrderDetail_IdAndStatus(aurOrderDetailId, UniqueBarcodeState.RECEIVING_SCANNED);
        if (receivingScanned.isEmpty()) {
            return;
        }
        AurDepoUrunAdres address = new AurDepoUrunAdres(dto.getAddressId());
        for (UniqueBarcode ub : receivingScanned) {
            ub.setAddress(address);
            ub.setStatus(UniqueBarcodeState.IN_TEMPORARY_AREA);
            repository.save(ub);
        }
        publisher.publishEvent(new UniqueBarcodeAddressUpdatedEvent(dto));
    }

    private void validateUnitOfMeasureRules(UniqueBarcodeCreateDTO dto, Product product, UnitOfMeasure unit) {
        if (unit == null) return;

        if (unit.requiresAdetOne() && dto.getQuantity().compareTo(BigDecimal.ONE) != 0) {
            throw new BadRequestAlertException(
                "ADET birimli ürünlerde quantity 1 olmak zorundadır",
                "UniqueBarcode", "invalidQuantityForAdet"
            );
        }
        if (unit.requiresQuantityOne() && dto.getAdet() != 1) {
            throw new BadRequestAlertException(
                unit.name() + " birimli ürünlerde adet 1 olmak zorundadır",
                "UniqueBarcode", "invalidAdetForUnit"
            );
        }
    }

    private String generateUniqueBarcode(
        String erpBarcode,
        String receivingDate,
        String customerCode,
        String erpOrderNo,
        Long lot
    ) {
        String customerNumeric = customerCode.replaceAll("\\D", "");
        String customerPart =
            customerNumeric.substring(customerNumeric.length() - 5);

        String orderNumeric = erpOrderNo.replaceAll("\\D", "");
        String orderPart =
            String.format("%07d", Integer.parseInt(orderNumeric));

        String lotPart = String.format("%04d", lot);

        return erpBarcode
            + receivingDate
            + customerPart
            + orderPart
            + lotPart;
    }
}
