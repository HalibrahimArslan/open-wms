package com.hisarresearch.wms.service;


import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.*;
import com.hisarresearch.wms.domain.enumeration.OrderStatus;
import com.hisarresearch.wms.repository.*;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.dto.event.AurOrderMasterEvent;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.uyumsoft.CloseOrderDto;
import com.hisarresearch.wms.service.dto.uyumsoft.IrsaliyeDetailRequestDTO;
import com.hisarresearch.wms.service.impl.OrderPickingTransactionServiceImpl;
import com.hisarresearch.wms.service.barcode.UniqueBarcodeService;
import com.hisarresearch.wms.service.dto.barcode.UniqueBarcodeResponseDTO;
import com.hisarresearch.wms.service.mapper.AurOrderDetailMapper;
import com.hisarresearch.wms.service.mapper.AurOrderDetailSktMapper;
import com.hisarresearch.wms.service.mapper.AurOrderMasterMapper;
import com.hisarresearch.wms.service.erp.MikroServices;
import com.hisarresearch.wms.utility.AurHelper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import com.hisarresearch.wms.exception.validation.InvalidOrderException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AurOrderMasterService {

    private final Logger log = LoggerFactory.getLogger(AurOrderMasterService.class);

    private static final String ENTITY_NAME = "AurOrderMaster";


    @Autowired
    private AurOrderMasterRepository aurOrderMasterRepository;

    @Autowired
    private AurUserRepository aurUserRepository;

    @Autowired
    private OrderPickingTransactionRepository orderPickingTransactionRepository;

    @Autowired
    private OrderPickingTransactionServiceImpl orderPickingTransactionService;

    @Autowired
    private AurPartialDetailsService aurPartialDetailsService;

    @Autowired
    private AurPartialItemService aurPartialItemService;

    @Autowired
    private AurPartialDetailsRepository aurPartialDetailsRepository;

    @Autowired
    private AurPartialItemRepository aurPartialItemRepository;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private MikroServices mikroServices;

    @Autowired
    private AurDispatchAreaControlService aurDispatchAreaControlService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRowService orderRowService;

    @Autowired
    private PalletBarcodeOrderRelService palletBarcodeOrderRelService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private AurOrderDetailSktRepository aurTmpDetailSktRepository;

    @Autowired
    private AurOrderMasterMapper aurOrderMasterMapper;

    @Autowired
    @Lazy
    private AurReserveService reserveService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private AurLookupService aurLookupService;

    @Autowired
    private AurOrderDetailService aurOrderDetailService;

    @Lazy
    @Autowired
    MailService mailService;

    @Autowired
    AurOrderDetailMapper aurOrderDetailMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    AurOrderDetailSktMapper aurOrderDetailSktMapper;

    @Autowired
    UniqueBarcodeService uniqueBarcodeService;

    @Transactional
    public  Optional<AurOrderMaster> findByOrderInfoAndUserIdAndOperationTypeAndStatuses(String orderInfo,Long userId,String operationType,List<String> statuses){
        return aurOrderMasterRepository.findByOrderInfoAndAurUser_IdAndOpTypeAndStatusIn(orderInfo,userId,operationType,statuses);
    }

    @Transactional
    public Optional<AurOrderMaster> findById(Long id) {
        return aurOrderMasterRepository.findById(id);
    }

    @Transactional
    public Optional<AurOrderMaster> findWithDetailsById(Long id) {
        return aurOrderMasterRepository.findWithDetailsById(id);
    }

    @Transactional
    public  Optional<AurOrderMaster> findByOrderInfo(String orderInfo){
        return aurOrderMasterRepository.findByOrderInfo(orderInfo);
    }

    @Transactional
    public  Optional<AurOrderMaster> findByDocNo(String documentNo){
        return aurOrderMasterRepository.findByBelgeNo(documentNo);
    }

    @Transactional
    public  AurOrderMaster save(AurOrderMaster orderMaster){
        return aurOrderMasterRepository.save(orderMaster);
    }


    public AurOrderMaster isExistOrderByOrderInfo(String orderInfo){
        Optional<AurOrderMaster> orderMaster = aurOrderMasterRepository.findByOrderInfo(orderInfo);
        if(orderMaster.isPresent()){
            return orderMaster.get();
        }
        throw new InvalidOrderException();
    }

    public AurOrderMaster isExistOrderById(Long id){
        Optional<AurOrderMaster> orderMaster = aurOrderMasterRepository.findById(id);
        if(orderMaster.isPresent()){
            return orderMaster.get();
        }
        throw new InvalidOrderException();
    }

    public void createOrderWithLeftItems(String orderInfo, List<AurOrderDetail> orderDetails) throws CloneNotSupportedException {
        AurOrderMaster aurOrderMaster = isExistOrderByOrderInfo(orderInfo);
        List<AurOrderDetail> existingOrderDetails = aurOrderDetailService.findByOrderIdAndStatus(aurOrderMaster.getId(),OrderStatus.OUT_PROGRESS.name());
        List<String> orderSipUidList = orderDetails.stream().map(AurOrderDetail::getSipUid).collect(Collectors.toList());
        List<AurOrderDetail> transferList = new ArrayList<>();

        existingOrderDetails.forEach(item -> {
            if (orderSipUidList.stream().noneMatch(q -> q.equals(item.getSipUid()))) {
                transferList.add(item);
            }
        });

        List<AurOrderDetail> transferDetails = orderDetails.stream().filter(d -> d.getObserverAmount() < d.getTeslimMiktar()).collect(Collectors.toList());
        List<AurOrderDetail> remainingDetails = transferDetails.stream().map(d -> {
                AurOrderDetail clonedOrderDetail = new AurOrderDetail(d);
                double newOrderAmount = BigDecimal.valueOf(d.getSiparisMiktar())
                    .subtract(BigDecimal.valueOf(d.getObserverAmount()))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
                double difference = BigDecimal.valueOf(d.getTeslimMiktar())
                    .subtract(BigDecimal.valueOf(d.getObserverAmount()))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
                clonedOrderDetail.setSiparisMiktar(newOrderAmount);
                clonedOrderDetail.setTeslimMiktar(difference);
                clonedOrderDetail.setObserverAmount(difference);
                clonedOrderDetail.setOrder(null);
                clonedOrderDetail.setStatus(OrderStatus.OUT_PROGRESS.name());
                return clonedOrderDetail;
            })
            .collect(Collectors.toList());

        if(!remainingDetails.isEmpty() || !transferList.isEmpty()) {
            AurOrderMaster clone = aurOrderMaster.clone();
            clone.setStatus("OUT_PROGRESS");
            AurOrderMaster savedOne = aurOrderMasterRepository.save(clone);
            savedOne.setOrderInfo("AUR-".concat(savedOne.getId().toString()).concat("-1"));
            savedOne.setBelgeNo("");
            remainingDetails.forEach(listItem -> listItem.setOrder(savedOne));
            transferList.forEach(transferItem -> transferItem.setOrder(savedOne));
            List<AurOrderDetail> createdItems = aurOrderDetailService.saveAll(remainingDetails);
            createdItems.forEach(createdItem -> {
                AurOrderDetail aurOrderDetail = orderDetails.stream().filter(detail -> detail.getSipUid().equals(createdItem.getSipUid()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(translationService.getErrorMessage("orderDetail.noneMatch"), ENTITY_NAME));
                OrderPickingTransaction opt = orderPickingTransactionService.findByAurTmpDetailIdAndTransactionType(aurOrderDetail.getId(), TransactionType.PICKING)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(translationService.getErrorMessage("orderDetail.noneMatch"), ENTITY_NAME));
                orderPickingTransactionService.saveByAurTmpDetailDto(createdItem, opt.getAddress().getUrunAdresId(), createdItem.getTeslimMiktar());
            });
        }
    }

    public void saveAurOrderList(AurOrderMasterDTO aurOrderMasterDTO) throws Exception {
        Optional<AurOrderMaster> aurOrderMaster = aurOrderMasterRepository.findByOrderInfo(aurOrderMasterDTO.getOrderInfo());
        String initialStatus = "OPEN";

        if (aurOrderMaster.isPresent()) {
            updateOrder(aurOrderMaster.get(), aurOrderMasterDTO);
        } else {
            createOrder(aurOrderMasterDTO, initialStatus);
            List<AurLookupTable> appointedMailIsActive = aurLookupService.getByLookupName("APPOINTED_MAIL_IS_ACTIVE");
            if (appointedMailIsActive != null && !appointedMailIsActive.isEmpty() && appointedMailIsActive.get(0).getLookupCode().equals("1")) {
                mailService.sendAppointedOrderMail(aurOrderMasterDTO);
            }
            List<String> siparisNoList = extractSiparisNoList(aurOrderMasterDTO);
            if (!siparisNoList.isEmpty()) {
                mikroServices.sendSiparisNoList(siparisNoList);
            }

        }
    }

    private List<String> extractSiparisNoList(AurOrderMasterDTO aurOrderMasterDTO) {
        if (aurOrderMasterDTO.getAurTmpDetailList() == null) {
            return Collections.emptyList();
        }

        return aurOrderMasterDTO.getAurTmpDetailList().stream()
            .map(AurOrderDetailDTO::getSiparisNo)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(no -> no.startsWith("MG"))
            .distinct()
            .collect(Collectors.toList());
    }


    public void createOrder(AurOrderMasterDTO dto, String initialStatus)  {
        List<String> sipUids = dto.getDistinctSipUidList();
        String operationType = dto.getOpType();
        boolean hasPreviousOrderRecord = hasPreviousOrderRecordBySipUid(sipUids, operationType);

        if (hasPreviousOrderRecord) {
            throw new BadRequestAlertException("Ilgili siparis nolara ait devam eden kayıt var", "aurOrderMaster", "invalid.order");
        }


        AurOrderMaster newOrder = aurOrderMasterMapper.dtoToMaster(dto);
        CustomerAddress customerAddress = new CustomerAddress();
        newOrder.setStatus("OPEN");
        newOrder.setOrderDepoCode(dto.getOrderDepoCode());
        newOrder.setBolgeKodu(dto.getBolgeKodu());
        if (newOrder.getOpType().equals("MSK")) {
            customerAddress = customerAddressService.saveCustomerAddress(dto);
            newOrder.setCustomerAddress(customerAddress);
        }
        newOrder = aurOrderMasterRepository.save(newOrder);
        newOrder.setOrderInfo("AUR-".concat(newOrder.getId().toString()));
        saveOrderDetail(dto.getAurTmpDetailList(), newOrder, dto.getOpType(), dto.getAddressId(), initialStatus);

        dto.setId(newOrder.getId());
    }

    public void updateOrder(AurOrderMaster order, AurOrderMasterDTO updateDto) throws Exception {
        String initialStatus = order.getStatus();
        order.setStatus(updateDto.getStatus());
        order.setSoforAdi(updateDto.getSoforAdi());
        order.setSoforPlaka(updateDto.getSoforPlaka());
        order.setSoforTcNo(updateDto.getSoforTcNo());
        order.setSoforTel(updateDto.getSoforTel());
        order.setTransportationType(updateDto.getTransportationType());
        order.setCompanyLogistics(updateDto.getCompanyLogistics());
        order.setCarryType(updateDto.getCarryType());
        if (updateDto.getControlAddress() != null) {
            order.setControlAddress(updateDto.getControlAddress());
        }

        AurOrderMaster savedOrder = aurOrderMasterRepository.save(order);
        if(updateDto.getStatus().equals(OrderStatus.OUT_PROGRESS.name()) && initialStatus.equals(OrderStatus.IN_PROGRESS.name())) {
            publisher.publishEvent(new AurOrderMasterEvent(savedOrder));
            return;
        }
        List<AurOrderDetail> savedDetails = saveOrderDetail(updateDto.getAurTmpDetailList(), order, order.getOpType(), updateDto.getAddressId(), initialStatus);
        if (updateDto.getStatus().equals(OrderStatus.DONE.name())) {
            createOrderWithLeftItems(updateDto.getOrderInfo(), savedDetails);
        }
    }

    public List<AurOrderDetail> saveOrderDetail(List<AurOrderDetailDTO> detailList, AurOrderMaster orderMaster, String opType, String addressId, String masterStatus)  {
        Boolean isPartial = false;
        Long aurPartialItemId = 0L;
        List<AurOrderDetail> details = new ArrayList<>();

        for (AurOrderDetailDTO aod : detailList) {
            Optional<AurOrderDetail> aurTmpDetail = checkTmpOrderDetail(aod, orderMaster.getId(), opType);
            if (aurTmpDetail.isPresent()) {
                Double transactionAmount = aod.getTeslimMiktar() - aurTmpDetail.get().getTeslimMiktar();
                AurOrderDetail updatedOne = updateTmpDetail(aurTmpDetail.get(), aod, orderMaster, transactionAmount, opType, addressId, detailList.get(0).getStatus(), masterStatus);
                details.add(updatedOne);
            } else {
                createTmpDetail(aod, isPartial, aurPartialItemId, orderMaster.getId(), Long.valueOf(addressId)); //TODO
            }
        }
        return details;
    }

    public Optional<AurOrderDetail> checkTmpOrderDetail(AurOrderDetailDTO atd, Long orderId, String operationType) {
        Optional<AurOrderDetail> searchTmpOrder = Optional.of(new AurOrderDetail());
        if (operationType.equals("FMK")) {
            List<AurOrderDetail> tmpDetails = aurOrderDetailService.findByStokKoduAndOrderId(atd.getStokKodu(), orderId);
            if (!tmpDetails.isEmpty()) {
                searchTmpOrder = Optional.ofNullable(tmpDetails.get(0));
            } else {
                searchTmpOrder = Optional.empty();
            }

        }
        if (operationType.equals("MSK")) {
            searchTmpOrder = aurOrderDetailService.findBySipUidAndOrderIdAndAurPartialItemIdAndBarkod(atd.getSipUid(), orderId, atd.getAurPartialItemId(), atd.getBarkod());

        }
        return searchTmpOrder;
    }

    public void createTmpDetail(AurOrderDetailDTO saveTmpDetail, Boolean isPartial, Long aurPartialItemId, Long orderId, Long addressId) {
        saveTmpDetail.setOrderId(orderId);
        saveTmpDetail.setStatus("IN_PROGRESS");
        saveTmpDetail.setPiece(isPartial);
        saveTmpDetail.setAurPartialItemId(aurPartialItemId);
        Optional<AurPartialItemDTO> partialItemDTO = aurPartialItemService.findOneByBarcode(saveTmpDetail.getBarkod());
        if (partialItemDTO.isPresent()) {
            createTmpDetailWhenPartial(partialItemDTO.get().getId(), saveTmpDetail, addressId);
        } else {
            AurOrderDetail savedOne = aurOrderDetailService.save(saveTmpDetail);
            orderPickingTransactionService.saveByAurTmpDetailDto(savedOne, addressId, savedOne.getTeslimMiktar());

        }

    }

    public void createTmpDetailWhenPartial(Long partialItemId, AurOrderDetailDTO baseTmpDetail, Long addressId) {
        List<AurPartialDetails> partialDetails = aurPartialDetailsService.findByAurPartialItemId(partialItemId);
        for (AurPartialDetails partialDetail : partialDetails) {
            AurOrderDetail partialTmpDetail = new AurOrderDetail();
            AurOrderMaster aurOrderMaster = new AurOrderMaster(baseTmpDetail.getOrderId());

            partialTmpDetail.setOrder(aurOrderMaster);
            partialTmpDetail.setStatus(baseTmpDetail.getStatus());
            partialTmpDetail.setStokKodu(partialDetail.getStockCode());
            partialTmpDetail.setBarkod(partialDetail.getBarcode());
            partialTmpDetail.setStokBirimi(baseTmpDetail.getStokBirimi());
            partialTmpDetail.setSiparisMiktar(baseTmpDetail.getSiparisMiktar() * partialDetail.getQuantity());
            partialTmpDetail.setTeslimMiktar(0.0);
            partialTmpDetail.setStokAdi(partialDetail.getStockName());
            partialTmpDetail.setSipUid(baseTmpDetail.getSipUid());
            partialTmpDetail.setSiparisNo(baseTmpDetail.getSiparisNo());
            partialTmpDetail.setObserverAmount(0.0);
            partialTmpDetail.setPiece(true);
            partialTmpDetail.setAurPartialItemId(partialItemId);

            AurOrderDetail savedOne = aurOrderDetailService.save(partialTmpDetail);
            orderPickingTransactionService.saveByAurTmpDetailDto(savedOne, addressId, savedOne.getTeslimMiktar());

        }
    }

    public AurOrderDetail updateTmpDetail(AurOrderDetail existingTmpDetail, AurOrderDetailDTO updateInfo, AurOrderMaster orderMaster, Double transactionAmount, String operationType, String addressId, String firstStatus, String masterStatus) {
        OrderPickingTransaction opt = new OrderPickingTransaction();
        String warehouseCode = String.valueOf(orderMaster.getDepoNo());

        existingTmpDetail.setStatus(updateInfo.getStatus());
        existingTmpDetail.setTeslimMiktar(updateInfo.getTeslimMiktar());
        existingTmpDetail.setObserverAmount(updateInfo.getObserverAmount());
        if (updateInfo.getObserverAmount() > 0) {
            Optional<AurOrderMaster> aom = findById(orderMaster.getId());
            aom.ifPresent(aurOrderMaster -> aurOrderMaster.setObservedUserId(userService.getUserId()));
        }

        if (existingTmpDetail.getStatus().equals("IN_PROGRESS") && operationType.equals("MSK")) {
            aurDepoUrunAdresStokService.decreaseProductAmount(existingTmpDetail.getBarkod(),warehouseCode,transactionAmount, Long.valueOf(addressId));
            opt.setReferenceId(existingTmpDetail.getId());
            opt.setAddress(new AurDepoUrunAdres(Long.valueOf(addressId)));
            opt.setStockCode(existingTmpDetail.getStokKodu());
            opt.status(true);
            opt.setTransactionAmount(transactionAmount);
            opt.setTransactionType(TransactionType.PICKING);
            orderPickingTransactionRepository.save(opt);
        }
        if (firstStatus.equals("OUT_PROGRESS")) {
            List<AurOrderDetail> tmpDetails = aurOrderDetailService.findByOrderId(orderMaster.getId());
            tmpDetails.forEach(x -> {
                if (x.getStatus().equals("OPEN") || x.getTeslimMiktar() == 0) {
                    x.setStatus("SUSPENDED");
                    aurOrderDetailService.save(x);
                }
            });

        }
        if (updateInfo.getStatus().equals("OUT_PROGRESS") && masterStatus.equals("OUT_PROGRESS")) {
            aurDispatchAreaControlService.saveDispatchMovement(existingTmpDetail);
        }

        return existingTmpDetail;
    }


    public List<AurOrderMasterDTO> getDetailList(List<AurOrderMaster> aurOrderMasters) {
        List<AurOrderMasterDTO> aurOrderMasterDTO = new ArrayList<>();

        for (AurOrderMaster aom : aurOrderMasters) {
            AurOrderMasterDTO dto = new AurOrderMasterDTO();
            dto.setAurUserId(aom.getAurUser().getId());
            dto.setOrderInfo(aom.getOrderInfo());
            dto.setDepoNo(aom.getDepoNo());
            dto.setId(aom.getId());
            dto.setFirmCode(aom.getFirmCode());
            dto.setOpType(aom.getOpType());
            dto.setStatus(aom.getStatus());
            dto.setBelgeNo(aom.getBelgeNo());
            dto.setSoforAdi(aom.getSoforAdi());
            dto.setSoforTel(aom.getSoforTel());
            dto.setSoforPlaka(aom.getSoforPlaka());
            dto.setSoforTcNo(aom.getSoforTcNo());
            dto.setFirmName(aom.getFirmName());
            dto.setCreatedDate(aom.getCreatedDate());
            List<AurOrderDetail> details = aurOrderDetailService.findByOrderId(aom.getId());
            dto.setAurTmpDetailList(aurOrderDetailMapper.toDto(details));

            aurOrderMasterDTO.add(dto);
        }
        return aurOrderMasterDTO.stream().sorted(Comparator.comparing(AurOrderMasterDTO::getCreatedDate).reversed()).collect(Collectors.toList());

    }


    public List<AurOrderMasterDTO> findByOpTypeAndDepoNoAndAurUserIdAndStatusIn(Collection<String> statusList, String opType, Integer depoCode, Long userId) {
        List<AurOrderMaster> aurOrderMasterList = aurOrderMasterRepository.findByOpTypeAndDepoNoAndAurUserIdAndStatusIn(opType, depoCode, userId, statusList);
        return getDetailList(aurOrderMasterList);
    }


    public List<AurOrderMasterDTO> getAurTmpOrderListWithStatusOutProgress(String status, String opType, Integer depoCode, Long controlAddressId) {
        AurDepoUrunAdres controlAddress = new AurDepoUrunAdres(controlAddressId);
        List<AurOrderMaster> aurOrderMasterList = aurOrderMasterRepository.findByStatusAndOpTypeAndControlAddressAndOrderDepoCode(status, opType, controlAddress, depoCode);
        return getDetailList(aurOrderMasterList);
    }

    public List<AurOrderMasterDTO> getAurTmpOrderList() {
        List<AurOrderMasterDTO> aurOrderMasterDTO = new ArrayList<>();

        Instant currentInstant = Instant.now();

        ZonedDateTime currentZonedDateTime = currentInstant.atZone(ZoneId.systemDefault());

        ZonedDateTime oneMonthAgoZonedDateTime = currentZonedDateTime.minus(1, ChronoUnit.MONTHS);
        Instant oneMonthAgoInstant = oneMonthAgoZonedDateTime.toInstant();

        List<AurOrderMaster> aurOrderMaster = aurOrderMasterRepository.findByCreatedDateBetween(oneMonthAgoInstant, currentInstant);//TODO

        for (AurOrderMaster aom : aurOrderMaster) {
            AurOrderMasterDTO dto = new AurOrderMasterDTO();
            dto.setAurUserId(aom.getAurUser().getId());
            dto.setOrderInfo(aom.getOrderInfo());
            dto.setDepoNo(aom.getDepoNo());
            dto.setId(aom.getId());
            dto.setFirmCode(aom.getFirmCode());
            dto.setOpType(aom.getOpType());
            dto.setStatus(aom.getStatus());
            dto.setBelgeNo(aom.getBelgeNo());
            dto.setSoforAdi(aom.getSoforAdi());
            dto.setSoforTel(aom.getSoforTel());
            dto.setSoforPlaka(aom.getSoforPlaka());
            dto.setSoforTcNo(aom.getSoforTcNo());
            dto.setFirmName(aom.getFirmName());
            dto.setCreatedDate(aom.getCreatedDate());
            dto.setCariBaglantiTipi(aom.getBaglantiTipi());
            dto.setCariCode(aom.getCariCode());
            List<AurOrderDetail> details = aurOrderDetailService.findByOrderId(aom.getId());
            dto.setAurTmpDetailList(aurOrderDetailMapper.toDto(details));

            aurOrderMasterDTO.add(dto);
        }
        return aurOrderMasterDTO.stream().sorted(Comparator.comparing(AurOrderMasterDTO::getCreatedDate).reversed()).collect(Collectors.toList());
    }

    public List<AurOrderDetailWithPartialDTO> getAurTmpOrderListWithPartialList(String info, AurUser user, String operationType) {
        List<AurOrderDetailWithPartialDTO> aurTmpDetailDto = new ArrayList<>();
        List<String> desiredStatuses = new ArrayList<>();
        desiredStatuses.add("OPEN");
        desiredStatuses.add("IN_PROGRESS");

        AurOrderMaster aom = findByOrderInfoAndUserIdAndOperationTypeAndStatuses(info, user.getId(), operationType, desiredStatuses)
            .orElseThrow(() -> new BusinessException(translationService.getErrorMessage("tmpDetail.orderNotBelongToUser", info, user.getLogin()), ENTITY_NAME));

        AurOrderDetailWithPartialDTO dto = new AurOrderDetailWithPartialDTO();
        dto.setAurUserId(aom.getAurUser().getId());
        dto.setOrderInfo(aom.getOrderInfo());
        dto.setDepoNo(aom.getDepoNo());
        dto.setId(aom.getId());
        dto.setFirmCode(aom.getFirmCode());
        dto.setOpType(aom.getOpType());
        dto.setStatus(aom.getStatus());
        dto.setBelgeNo(aom.getBelgeNo());
        dto.setSoforAdi(aom.getSoforAdi());
        dto.setSoforTel(aom.getSoforTel());
        dto.setSoforPlaka(aom.getSoforPlaka());
        dto.setSoforTcNo(aom.getSoforTcNo());
        dto.setFirmName(aom.getFirmName());
        dto.setCreatedDate(aom.getCreatedDate());
        dto.setCariBaglantiTipi(aom.getBaglantiTipi());
        dto.setCariCode(aom.getCariCode());
        List<AurOrderDetail> details = aurOrderDetailService.findByOrderId(aom.getId());
        dto.setAurTmpDetailList(convertToPartial(details.stream().filter(q -> !q.getStatus().equals("SUSPENDED")).collect(Collectors.toList())));
        reserveService.checkReserveStatus(dto.getAurTmpDetailList());

        aurTmpDetailDto.add(dto);

        return aurTmpDetailDto.stream().sorted(Comparator.comparing(AurOrderDetailWithPartialDTO::getCreatedDate).reversed()).collect(Collectors.toList());
    }

    public List<PartialDetailDto> convertToPartial(List<AurOrderDetail> aurOrderDetailList) {
        List<PartialDetailDto> response = new ArrayList<>();
        int counter = 0;

        for (AurOrderDetail item : aurOrderDetailList) {
            PartialDetailDto dto = new PartialDetailDto();
            PieceMasterDTO pieceMasterDto = new PieceMasterDTO();
            log.debug("Convert to partial {}", item);
            Optional<AurPartialDetails> detailList = aurPartialDetailsService.findByBarcodeAndAurPartialItemId(item.getBarkod(), item.getAurPartialItemId());
            if (item.getPiece()) {
                Optional<AurPartialItemDTO> masterItem = aurPartialItemService.findOne(item.getAurPartialItemId());
                if (masterItem.isPresent()) {
                    pieceMasterDto.setStokKodu(masterItem.get().getPackageCode());
                    pieceMasterDto.setStokAdi(masterItem.get().getPackageName());
                    pieceMasterDto.setBarcode(masterItem.get().getPackageBarcode());
                }

            }
            dto.setId(counter);
            dto.setAurPartialItemId(item.getAurPartialItemId());
            dto.setPiece(item.getPiece());
            dto.setOrderNo(item.getSiparisNo());
            dto.setStokKodu(item.getStokKodu());
            dto.setStokAdi(item.getStokAdi());
            dto.setBarkod(item.getBarkod());

            if (detailList.isPresent()) {
                dto.setPieceAmount(detailList.get().getQuantity());
            } else {
                dto.setPieceAmount(1.0);
            }
            DecimalFormat df = new DecimalFormat("#.##");
            double rounded = Double.parseDouble(df.format(item.getSiparisMiktar()));
            dto.setSiparisMiktar(rounded);
            dto.setPieceMaster(pieceMasterDto);
            dto.setTeslimMiktar(item.getTeslimMiktar());
            dto.setSipUid(item.getSipUid());

            response.add(dto);
            counter++;
        }
        return response;
    }

    public List<AurOrderDetailWithPartialDTO> getPickingTmpOrderByUserName(String opType, Integer depoNo, String info)  {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AurUser aurUser = aurUserRepository.findByLogin(userName);

        if (aurUser == null) {
            String errorMessage = translationService.getErrorMessage("user.notFound");
            throw new BusinessException(errorMessage, ENTITY_NAME);
        }

        return getAurTmpOrderListWithPartialList(info, aurUser, opType);
    }

    public List<AurOrderMasterDTO> getAurTmpOrderListByUserName(String opType, Integer depoNo) {
        long userId = userService.getUserId();

        Collection<String> statusList = new ArrayList<>();
        statusList.add("OPEN");
        statusList.add("IN_PROGRESS");

        return findByOpTypeAndDepoNoAndAurUserIdAndStatusIn(statusList, opType, depoNo, userId);
    }

    public List<AurOrderDetail> getAurOrderListByUserDefined(String firmCode, String status, String opType, Integer depoNo) {
        List<AurOrderDetail> aurOrderDetails = new ArrayList<>();

        List<AurOrderMaster> aurOrderMasters = aurOrderMasterRepository.findByFirmCodeAndStatusNotAndOpTypeAndDepoNo(firmCode, status, opType, depoNo);

        if (!aurOrderMasters.isEmpty()) {
            List<AurOrderMaster> aom = aurOrderMasters.stream().filter(x -> x.getStatus().equals("OPEN") || x.getStatus().equals("IN_PROGRESS")).collect(Collectors.toList());

            for (AurOrderMaster aurOrderMaster : aom) {
                List<AurOrderDetail> aurOrderDetailList = aurOrderDetailService.findByOrderIdAndStatusNot(aurOrderMaster.getId(), status);
                aurOrderDetails.addAll(aurOrderDetailList);
            }
        }

        return aurOrderDetails;
    }

    public List<AurOrderMasterDTO> getDoneAurOrderList(String opType, Integer depoCode, Long controlAddressId) {
        List<AurOrderMasterDTO> filteredMainList = getAurTmpOrderListWithStatusOutProgress("OUT_PROGRESS", opType, depoCode, controlAddressId);

        for (AurOrderMasterDTO dto : filteredMainList) {
            List<AurOrderDetailDTO> aurOrderDetails = dto.getAurTmpDetailList();
            dto.setAurTmpDetailList(aurOrderDetails.stream().filter(tmp -> tmp.getStatus().equals("OUT_PROGRESS")).collect(Collectors.toList()));
        }

        return filteredMainList;
    }

    public String suspendOrder(Long id) {
        AurOrderMaster orderMaster = findById(id).orElseThrow(InvalidOrderException::new);
        List<String> desiredOperationTypes = new ArrayList<>();
        desiredOperationTypes.add(WmOperationType.MUSTERI_SEVKIYAT.getOperationType());
        desiredOperationTypes.add(WmOperationType.DEPOLAR_ARASI_SEVKIYAT.getOperationType());

        orderMaster.setStatus(OrderStatus.SUSPENDED.toString());
        aurOrderMasterRepository.save(orderMaster);
        List<AurOrderDetail> aurOrderDetails = aurOrderDetailService.findByOrderId(id);
        if (desiredOperationTypes.contains(orderMaster.getOpType())) {
            aurOrderDetails.forEach(x -> {
                if (x.getStatus().equals(OrderStatus.OUT_PROGRESS.toString())) {
                    aurDepoUrunAdresStokService.deleteFromDispatchArea(orderMaster.getDepoNo(), x.getBarkod(), x.getTeslimMiktar(), orderMaster.getControlAddress().getUrunAdresId());
                } else {
                    revertAmountToAddresses(x.getId(), orderMaster.getAssignedOrder());
                }
            });
        }
        aurOrderDetails.forEach(detail -> {
            uniqueBarcodeService.cancelReceiveScanByOrderDetail(detail.getId());
            detail.setStatus(OrderStatus.SUSPENDED.toString());
        });
        aurOrderDetailService.saveAll(aurOrderDetails);
        palletBarcodeOrderRelService.completePalletBarcodes(id);
        return "success";
    }

    public List<AurOrderMasterDTO> getById(Long aurOrderMasterId) {
        List<AurOrderMasterDTO> aurOrderMasterDTO = getAurTmpOrderList();

        List<AurOrderMasterDTO> response = aurOrderMasterDTO.stream().filter(x -> x.getId() == aurOrderMasterId.longValue()).collect(Collectors.toList());

        for (AurOrderMasterDTO dto : response) {
            List<AurOrderDetailDTO> aurOrderDetails = dto.getAurTmpDetailList();
            List<AurOrderDetailDTO> filteredList = aurOrderDetails.stream().filter(tmp -> !tmp.getStatus().equals("DONE")).collect(Collectors.toList());
            dto.setAurTmpDetailList(filteredList);

        }

        return response;

    }

    public String reformAurOrderTmpDetail(AurOrderDetailReformDTO aurTmpDetailReformDto) throws InvalidOrderException {
        AurOrderMaster orderMaster = findById(aurTmpDetailReformDto.getAurOrderId()).orElseThrow(InvalidOrderException::new);

        if (aurTmpDetailReformDto.getStatus().equals("SUSPENDED")) {
            suspendOrder(orderMaster.getId());
        } else {
            List<AurOrderDetail> deletedItems = aurTmpDetailReformDto.getDeleteItems();
            for (AurOrderDetail deletedOne : deletedItems) {
                List<AurOrderDetail> aurOrderDetail = aurOrderDetailService.findByStokKoduAndOrderId(deletedOne.getStokKodu(), aurTmpDetailReformDto.getAurOrderId());

                aurOrderDetail.forEach(tmpItem -> {
                    tmpItem.setStatus("SUSPENDED");
                    aurOrderDetailService.save(tmpItem);

                });
            }
            List<AurOrderDetail> addedItems = aurTmpDetailReformDto.getAddedItems();
            for (AurOrderDetail addedOne : addedItems) {
                List<AurOrderDetail> aurOrderDetail = aurOrderDetailService.findByStokKoduAndOrderId(addedOne.getStokKodu(), aurTmpDetailReformDto.getAurOrderId());

                if (!aurOrderDetail.isEmpty()) {
                    aurOrderDetail.get(0).setSiparisMiktar(addedOne.getSiparisMiktar());
                    aurOrderDetailService.save(aurOrderDetail.get(0));
                } else {
                    addedOne.setStatus("OPEN");
                    AurOrderMaster aurOrderMaster = new AurOrderMaster(aurTmpDetailReformDto.getAurOrderId());
                    addedOne.setOrder(aurOrderMaster);
                    aurOrderDetailService.save(addedOne);
                }
            }
        }

        return "success";
    }

    public String reformAurOrderTmpDetailSevkiyat(AurOrderDetailReformSevkiyatDTO aurTmpDetailReformSevkiyatDto) {

        AurOrderMaster aurOrderMaster = aurOrderMasterRepository.findById(aurTmpDetailReformSevkiyatDto.getAurOrderId()).orElseThrow(InvalidOrderException::new);

        if (aurTmpDetailReformSevkiyatDto.getStatus().equals("SUSPENDED")) {
            suspendOrder(aurTmpDetailReformSevkiyatDto.getAurOrderId());
        } else {
            aurOrderMaster.setStatus(aurTmpDetailReformSevkiyatDto.getStatus());
            List<AurOrderDetail> deletedItems = aurTmpDetailReformSevkiyatDto.getDeleteItems();
            for (AurOrderDetail deletedOne : deletedItems) {
                Optional<AurOrderDetail> aurTmpDetail = aurOrderDetailService.findBySipUidAndOrderId(deletedOne.getSipUid(), aurTmpDetailReformSevkiyatDto.getAurOrderId());
                aurTmpDetail.ifPresentOrElse(tmpItem -> {
                    tmpItem.setStatus("SUSPENDED");
                    aurOrderDetailService.save(tmpItem);

                }, () -> {
                });
            }
            List<AurCariOrderDetailListDto> addedItems = aurTmpDetailReformSevkiyatDto.getAddedItems();
            for (AurCariOrderDetailListDto addedOne : addedItems) {
                Optional<AurOrderDetail> aurTmpDetail = Optional.of(new AurOrderDetail());
                Long aurPartialItemId = 0L;
                boolean isPiece = false;

                Optional<AurPartialItemDTO> isPartialItem = aurPartialItemService.findOneByBarcode(addedOne.getBarkod());

                if (isPartialItem.isPresent()) {
                    aurTmpDetail = aurOrderDetailService.findBySipUidAndOrderIdAndBarkod(addedOne.getSipUid(), aurTmpDetailReformSevkiyatDto.getAurOrderId(), addedOne.getBarkod());
                    aurPartialItemId = isPartialItem.get().getId();
                    isPiece = true;

                } else {
                    aurTmpDetail = aurOrderDetailService.findBySipUidAndOrderId(addedOne.getSipUid(), aurTmpDetailReformSevkiyatDto.getAurOrderId());
                }
                if (aurTmpDetail.isPresent()) {
                    aurTmpDetail.get().setSiparisMiktar(addedOne.getSiparisMiktar());
                    aurTmpDetail.get().setStatus(aurTmpDetailReformSevkiyatDto.getStatus());
                    aurOrderDetailService.save(aurTmpDetail.get());
                } else {
                    AurOrderDetail dto = new AurOrderDetail();
                    dto.setOrder(aurOrderMaster);
                    dto.setStatus("OPEN");
                    dto.setStokKodu(addedOne.getStokKodu());
                    dto.setBarkod(addedOne.getBarkod());
                    dto.setStokBirimi(addedOne.getStokBirimi());
                    dto.setSiparisMiktar(addedOne.getSiparisMiktar());
                    dto.setTeslimMiktar(addedOne.getTeslimMiktar());
                    dto.setStokAdi(addedOne.getStokAdi());
                    dto.setSipUid(addedOne.getSipUid());
                    dto.setSiparisNo(addedOne.getOrderNo());
                    dto.setObserverAmount(0.0);
                    dto.setAurPartialItemId(aurPartialItemId);
                    dto.setPiece(isPiece);
                    aurOrderDetailService.save(dto);
                }
            }

        }

        return "success";

    }

    public AurOrderMaster saveAurOrderListWithoutAssign(AurOrderMasterDTO aurOrderMasterDTO) throws Exception {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AurOrderMaster> orderMaster = aurOrderMasterRepository.findByOrderInfo(aurOrderMasterDTO.getOrderInfo());

        List<String> orderNoList = aurOrderMasterDTO.getDistinctOrderNoList();
        boolean hasPreviousOrderRecord = hasPreviousOrderRecordByOrderNo(orderNoList);

        if(hasPreviousOrderRecord){
            throw new BadRequestAlertException("Ilgili siparis nolara ait devam eden kayıt var","aurOrderMaster","invalid.order");
        }

        if (orderMaster.isPresent()) {
            AurOrderMaster updatedOne = updateOrderWithoutAssign(orderMaster.get(), aurOrderMasterDTO.getOpType(), aurOrderMasterDTO, userName);
            saveOrderDetailWithoutAssign(aurOrderMasterDTO.getAurTmpDetailList(), updatedOne.getId(), updatedOne.getOpType(), aurOrderMasterDTO.getAddressId());
            if (!updatedOne.getStatus().equals("SUSPENDED")) {
                updatedOne.setStatus(aurOrderMasterDTO.getStatus());
            }
            return updatedOne;
        } else {
            return saveOrderWithoutAssign(aurOrderMasterDTO);
        }
    }

    @Transactional
    public AurOrderMaster saveOrderWithoutAssign(AurOrderMasterDTO saveDto) throws Exception {
        long aurUserId = userService.getUserId();
        List<String> sipUidList = saveDto.getDistinctSipUidList();
        String operationType = saveDto.getOpType();

        if (hasPreviousOrderRecordBySipUid(sipUidList, operationType)) {
            throw new BadRequestAlertException("Ilgili siparis nolara ait devam eden kayıt var", "aurOrderMaster", "invalid.order");
        }

        saveDto.setAurUserId(aurUserId);
        AurOrderMaster newOrder = aurOrderMasterMapper.dtoToMaster(saveDto);

        CustomerAddress customerAddress = new CustomerAddress();

        if (newOrder.getOpType().equals("MSK")) {
            customerAddress = customerAddressService.saveCustomerAddress(saveDto);
            newOrder.setCustomerAddress(customerAddress);
        }

        newOrder.setStatus("IN_PROGRESS");
        newOrder.setAssignedOrder(false);
        newOrder = aurOrderMasterRepository.save(newOrder);
        newOrder.setOrderInfo("AUR-".concat(newOrder.getId().toString()));
        newOrder.setOrderDepoCode(saveDto.getOrderDepoCode());
        newOrder.setBolgeKodu(saveDto.getBolgeKodu());

        saveOrderDetailWithoutAssign(saveDto.getAurTmpDetailList(), newOrder.getId(), saveDto.getOpType(), saveDto.getAddressId());

        return newOrder;

    }

    public AurOrderMaster updateOrderWithoutAssign(AurOrderMaster orderMaster, String operationType, AurOrderMasterDTO dto, String userName) {
        if (operationType.equals("FMK")) {
            orderMaster.setStatus(dto.getStatus());
            orderMaster.setBelgeNo(dto.getBelgeNo());
            orderMaster.setSoforAdi(dto.getSoforAdi());
            orderMaster.setSoforPlaka(dto.getSoforPlaka());
            orderMaster.setSoforTcNo(dto.getSoforTcNo());
            orderMaster.setSoforTel(dto.getSoforTel());

            aurOrderMasterRepository.save(orderMaster);

        }
        if (operationType.equals("MSK")) {
            if (dto.getControlAddress() != null) {
                orderMaster.setControlAddress(dto.getControlAddress());
            }
            aurOrderMasterRepository.save(orderMaster);
        }

        return orderMaster;

    }

    public void saveOrderDetailWithoutAssign(List<AurOrderDetailDTO> detailList, Long orderId, String opType, String addressId) throws Exception {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        for (AurOrderDetailDTO aod : detailList) {
            aod.setTeslimMiktar(AurHelper.roundAmount(aod.getTeslimMiktar()));
            aod.setObserverAmount(AurHelper.roundAmount(aod.getObserverAmount()));
            Optional<AurOrderDetail> aurTmpDetail = checkOrderDetailWhenWithoutAssign(aod, orderId,opType);
            Double transactionAmount = aod.getTeslimMiktar();
            AurOrderDetail savedDetail;
            if (aurTmpDetail.isPresent()) {
                Double previousAmount = aurTmpDetail.get().getStatus().equals(OrderStatus.SUSPENDED.name()) ? 0.0 : aurTmpDetail.get().getTeslimMiktar();
                transactionAmount = AurHelper.roundAmount(aod.getTeslimMiktar() - previousAmount);
                List<AurOrderDetailSktDTO> sktList = aod.getAurTmpDetailSktList();
                savedDetail = updateOrderDetailWithoutAssign(aurTmpDetail.get(), aod, transactionAmount, orderId, userName, opType, addressId);
                updateAurTmpSktList(sktList, aurTmpDetail.get());
            } else {
                List<AurOrderDetailSkt> sktList = aurOrderDetailSktMapper.toEntity(aod.getAurTmpDetailSktList());
                savedDetail = saveOrderDetailWithoutAssign(aod, orderId, Long.valueOf(addressId));
                sktList.forEach(skt -> {
                    skt.setAurTmpDetail(savedDetail);
                    aurTmpDetailSktRepository.save(skt);
                });
            }

            if (Objects.nonNull(aod.getUniqueBarcodeAssign())) {
                uniqueBarcodeService.updateToReceivingScanned(
                    aod.getUniqueBarcodeAssign().getBarcode(),
                    aod.getUniqueBarcodeAssign().getQuantity(),
                    savedDetail,
                    transactionAmount
                );
            }
        }

        if (!detailList.isEmpty() && detailList.get(0).getStatus().equals("OUT_PROGRESS")) {
            saveOrderDetailWithoutAssignWhenOutProgress(orderId, detailList, opType);
        }
    }

    public void saveOrderDetailWithoutAssignWhenOutProgress(Long orderId, List<AurOrderDetailDTO> detailList, String opType) throws Exception {
        AurHelper aurHelper = new AurHelper();
        List<AurOrderDetail> savedList = new ArrayList<>();

        List<AurOrderDetail> tmpDetails = aurOrderDetailService.findByOrderId(orderId);
        List<String> distinctOrderNo = tmpDetails.stream().filter(aurHelper.distinctByKey(AurOrderDetail::getSiparisNo)).map(AurOrderDetail::getSiparisNo).collect(Collectors.toList());
        detailList.forEach(item -> {
            Optional<AurOrderDetail> atm = checkOrderDetailWhenWithoutAssign(item, orderId, opType);
            atm.ifPresent(savedList::add);
        });
        List<String> inputParams = savedList.stream().filter(aurHelper.distinctByKey(AurOrderDetail::getSiparisNo)).map(AurOrderDetail::getSiparisNo).collect(Collectors.toList());
        if (inputParams.containsAll(distinctOrderNo)) {
            combineOrders(savedList, "DONE");
        } else {
            combineOrders(savedList, "PARTIAL");
        }
    }

    private void validateReceivingDeliveryLimit(AurOrderDetailDTO aod, String opType, Optional<AurPartialDetails> partialDetail) {
        if (!WmOperationType.FIRMADAN_MAL_KABUL.getOperationType().equals(opType)) {
            return;
        }
        double orderAmount = aod.getSiparisMiktar();
        if (partialDetail.isPresent()) {
            orderAmount = aod.getSiparisMiktar() * partialDetail.get().getQuantity();
        }
        double allowedLimit = orderAmount * 1.10;
        if (aod.getTeslimMiktar() > allowedLimit) {
            throw new BusinessException(
                translationService.getErrorMessage("orderDetail.receivingDeliveryLimitExceeded",
                    orderAmount, aod.getTeslimMiktar(), allowedLimit),
                ENTITY_NAME, "receivingDeliveryLimitExceeded"
            );
        }
    }

    public AurOrderDetail updateOrderDetailWithoutAssign(AurOrderDetail existingTmpDetail, AurOrderDetailDTO updateParams, Double transactionAmount, Long orderId, String userName, String operationType, String addressId) {
        OrderPickingTransaction opt = new OrderPickingTransaction();

        existingTmpDetail.setStatus(updateParams.getStatus());
        existingTmpDetail.setTeslimMiktar(AurHelper.roundAmount(updateParams.getTeslimMiktar()));
        existingTmpDetail.setObserverAmount(AurHelper.roundAmount(updateParams.getObserverAmount()));
        if (updateParams.getObserverAmount() > 0) {
            Optional<AurOrderMaster> aom = aurOrderMasterRepository.findById(orderId);
            aom.ifPresent(aurOrderMaster -> aurOrderMaster.setObservedUserId(userService.getUserId()));
        }

        AurOrderDetail savedOne = aurOrderDetailService.save(existingTmpDetail);

        if (existingTmpDetail.getStatus().equals("IN_PROGRESS") && operationType.equals("MSK")) {
            opt.setReferenceId(savedOne.getId());
            opt.setAddress(new AurDepoUrunAdres(Long.valueOf(addressId)));
            opt.setStockCode(savedOne.getStokKodu());
            opt.status(true);
            opt.setTransactionAmount(transactionAmount);
            opt.setTransactionType(TransactionType.PICKING);
            orderPickingTransactionRepository.save(opt);
        }

        return savedOne;

    }

    public AurOrderDetail saveOrderDetailWithoutAssign(AurOrderDetailDTO tmpDetail, Long orderId, Long addressId) {
        Optional<AurPartialDetails> partialItem = aurPartialDetailsService.findByBarcodeAndAurPartialItemId(tmpDetail.getBarkod(), tmpDetail.getAurPartialItemId());
        AurOrderMaster aurOrderMaster = new AurOrderMaster(orderId);
        partialItem.ifPresentOrElse((item) -> {
            tmpDetail.setPiece(true);
            tmpDetail.setAurPartialItemId(item.getAurPartialItem().getId());
        }, () -> {
            tmpDetail.setPiece(false);
            tmpDetail.setAurPartialItemId(0L);
        });
        tmpDetail.setOrderId(orderId);
        tmpDetail.setStatus("IN_PROGRESS");
        tmpDetail.setTeslimMiktar(AurHelper.roundAmount(tmpDetail.getTeslimMiktar()));
        tmpDetail.setObserverAmount(AurHelper.roundAmount(tmpDetail.getObserverAmount()));
        AurOrderDetail savedOne = aurOrderDetailService.save(tmpDetail);
        orderPickingTransactionService.saveByAurTmpDetailDto(savedOne, addressId, savedOne.getTeslimMiktar());
        return savedOne;

    }

    public Optional<AurOrderDetail> checkOrderDetailWhenWithoutAssign(AurOrderDetailDTO orderDetail, Long orderId,String opType) {
        if(!StringUtils.hasText(orderDetail.getBarkod())){
            throw new IllegalArgumentException(translationService.getErrorMessage("orderDetail.barcodeRequired"));
        }
        Optional<AurPartialDetails> isPartialItem = aurPartialDetailsService.findByBarcodeAndAurPartialItemId(orderDetail.getBarkod(), orderDetail.getAurPartialItemId());
        validateReceivingDeliveryLimit(orderDetail, opType, isPartialItem);
        if (isPartialItem.isPresent()) {
            return aurOrderDetailService.findBySipUidAndOrderIdAndBarkod(orderDetail.getSipUid(), orderId, orderDetail.getBarkod());
        }
        return aurOrderDetailService.findBySipUidAndOrderId(orderDetail.getSipUid(), orderId);
    }

    public List<AurOrderDetailResponseDTO> findTmpOrderByFirmCodeAndOrderNo(int depoCode, String firmCode, String opType, String orderNo) {
        Long aurUserId = userService.getUserId();
        List<AurOrderDetail> response = new ArrayList<>();
        List<AurOrderMaster> aurOrderMasterList = aurOrderMasterRepository.findByFirmCodeAndStatusAndOpTypeAndDepoNoAndAurUserId(firmCode, "IN_PROGRESS", opType, depoCode, aurUserId);

        for (AurOrderMaster aom : aurOrderMasterList) {
            List<AurOrderDetail> aurOrderDetailList = aurOrderDetailService.findByOrderIdAndSiparisNoAndStatus(aom.getId(), orderNo, "IN_PROGRESS");
            response.addAll(aurOrderDetailList);
        }

        List<AurOrderDetailResponseDTO> result = aurOrderDetailMapper.toResponseDtoList(response);
        result.forEach(dto -> {
            if (dto.getUniqueBarcodeList() != null) {
                dto.getUniqueBarcodeList().sort(Comparator.comparing(
                    UniqueBarcodeResponseDTO::getLastModifiedDate,
                    Comparator.nullsLast(Comparator.reverseOrder())
                ));
            }
        });
        return result;
    }

    public List<AurOrderDetail> findTmpOrderByFirmCodeAndOrderNoAtDispatchArea(int depoCode, String firmCode, String opType, String orderNo) {
        List<AurOrderDetail> response = new ArrayList<>();
        List<AurOrderMaster> aurOrderMasterList = aurOrderMasterRepository.findByFirmCodeAndStatusAndOpTypeAndDepoNo(firmCode, "OUT_PROGRESS", opType, depoCode);

        for (AurOrderMaster aom : aurOrderMasterList) {
            List<AurOrderDetail> aurOrderDetailList = aurOrderDetailService.findByOrderIdAndSiparisNoAndStatus(aom.getId(), orderNo, "OUT_PROGRESS");
            response.addAll(aurOrderDetailList);
        }

        return response;
    }

    public void combineOrders(List<AurOrderDetail> aurOrderDetails, String type) throws Exception {
        String userName = userService.getUserName();

        Optional<AurOrderDetail> atd = aurOrderDetailService.findById(aurOrderDetails.get(0).getId());
        String firmCode = "";
        if (atd.isPresent()) {
            Optional<AurOrderMaster> aom = aurOrderMasterRepository.findById(atd.get().getOrder().getId());
            if (aom.isPresent()) {
                firmCode = aom.get().getFirmCode();
            }
            List<AurOrderMaster> aurOrderMasterList = aurOrderMasterRepository.findByFirmCodeAndStatusAndOpTypeAndDepoNo(firmCode, "OUT_PROGRESS", "MSK", aom.get().getDepoNo());

            if (!aurOrderMasterList.isEmpty()) {
                if (aurOrderMasterList.size() > 1 && !firmCode.equals("120.50.342") && !firmCode.equals("120.50.341")) {
                    throw new BadRequestAlertException("1 den fazla sipariş birleştirilemez ", "combineOrders", "overLimit");
                }
                if (aurOrderMasterList.size() == 1 && !firmCode.equals("120.50.342") && !firmCode.equals("120.50.341")) {
                    List<AurOrderDetail> aurOrderDetailList = aurOrderDetailService.findByOrderId(aurOrderMasterList.get(0).getId());
                    for (AurOrderDetail params : aurOrderDetails) {
                        Optional<AurOrderDetail> tmpDetail = aurOrderDetailList.stream().filter(x -> x.getSipUid().equals(params.getSipUid())).findAny();
                        if (tmpDetail.isPresent()) {
                            tmpDetail.get().setTeslimMiktar(params.getTeslimMiktar() + tmpDetail.get().getTeslimMiktar());
                            tmpDetail.get().setObserverAmount(params.getObserverAmount() + tmpDetail.get().getObserverAmount());
                            tmpDetail.get().setStatus("OUT_PROGRESS");
                            params.setStatus("SUSPENDED");
                            aurOrderDetailService.save(params);
                            aurOrderDetailService.save(tmpDetail.get());

                        } else {
                            AurOrderMaster aurOrderMaster = new AurOrderMaster(aurOrderMasterList.get(0).getId());
                            params.setOrder(aurOrderMaster);
                            aurOrderDetailService.save(params);
                        }
                    }

                    if (type.equals("DONE")) {
                        aom.get().setStatus("SUSPENDED");
                        aurOrderMasterRepository.save(aom.get());
                    }


                }
                if (aurOrderMasterList.isEmpty() || firmCode.equals("120.50.342") || firmCode.equals("120.50.341")) {
                    if (type.equals("DONE")) {
                        aurOrderDetails.forEach(x -> {
                            if (x.getStatus().equals("OPEN") || x.getTeslimMiktar() == 0) {
                                x.setStatus("SUSPENDED");
                            } else {
                                x.setStatus("OUT_PROGRESS");
                            }
                            aurOrderDetailService.save(x);
                            aom.get().setStatus("OUT_PROGRESS");
                            aurOrderMasterRepository.save(aom.get());

                        });
                    } else {
                        Optional<AurOrderMaster> aurOrderMaster = aurOrderMasterRepository.findById(aom.get().getId());

                        AurOrderMasterDTO dto = new AurOrderMasterDTO();
                        dto.setAurUserId(userService.getUserId());
                        dto.setOrderInfo("");
                        dto.setStatus("OUT_PROGRESS");
                        dto.setDepoNo(aurOrderMaster.get().getDepoNo());
                        dto.setFirmCode(aurOrderMaster.get().getFirmCode());
                        dto.setOpType(aurOrderMaster.get().getOpType());
                        dto.setBelgeNo(aurOrderMaster.get().getBelgeNo());
                        dto.setSoforAdi(aurOrderMaster.get().getBelgeNo());
                        dto.setSoforTel(aurOrderMaster.get().getSoforTel());
                        dto.setSoforPlaka(aurOrderMaster.get().getSoforPlaka());
                        dto.setSoforTcNo(aurOrderMaster.get().getSoforTcNo());
                        dto.setFirmName(aurOrderMaster.get().getFirmName());
                        dto.setCreatedDate(Instant.now());
                        dto.setAddressId("");
                        dto.setAurTmpDetailList(aurOrderDetailMapper.toDto(aurOrderDetails));
                        AurOrderMaster savedAurOrderMaster = saveAurOrderListWithoutAssign(dto);
                        Optional<AurOrderMaster> finalList = aurOrderMasterRepository.findByOrderInfo(savedAurOrderMaster.getOrderInfo());
                        if (finalList.isPresent()) {
                            finalList.get().setStatus("OUT_PROGRESS");
                            List<AurOrderDetail> tmpDetailList = aurOrderDetailService.findByOrderId(finalList.get().getId());
                            tmpDetailList.forEach(item -> item.setStatus("OUT_PROGRESS"));
                            aurOrderMasterRepository.save(finalList.get());
                            aurOrderDetailService.saveAll(tmpDetailList);

                        }

                        aurOrderDetailService.findByOrderId(aom.get().getId()).forEach(item -> {
                            for (AurOrderDetail tmp : aurOrderDetails) {
                                if (item.getSipUid().equals(tmp.getSipUid())) {
                                    item.setStatus("SUSPENDED");

                                }
                            }
                            aurOrderDetailService.save(item);

                        });

                    }
                }
            }
        }

    }

    public List<AurOrderMasterDTO> getAurTmpListWithStatusDone(String aurOrderInfo) throws CloneNotSupportedException {
        List<AurOrderMasterDTO> response = new ArrayList<>();
        AurHelper aurHelper = new AurHelper();
        Optional<AurOrderMaster> aom = aurOrderMasterRepository.findByOrderInfo(aurOrderInfo);
        if (aom.isPresent()) {
            AurOrderMasterDTO dto = aurOrderMasterMapper.masterToDTO(aom.get());
            List<AurOrderDetail> tmpList = aurOrderDetailService.findByOrderId(aom.get().getId());
            List<AurOrderDetail> filteredList = tmpList.stream().filter(item -> item.getStatus().equals("OUT_PROGRESS") && !item.getPiece()).collect(Collectors.toList());
            List<AurOrderDetail> partialItems = tmpList.stream().filter(item -> item.getStatus().equals("OUT_PROGRESS") && item.getPiece()).collect(Collectors.toList());
            List<String> distinctSipUid = partialItems.stream().filter(aurHelper.distinctByKey(AurOrderDetail::getSipUid)).map(AurOrderDetail::getSipUid).collect(Collectors.toList());

            for (String item : distinctSipUid) {
                List<AurOrderDetail> list = partialItems.stream().filter(partialItem -> partialItem.getSipUid().equals(item)).collect(Collectors.toList());
                List<AurPartialDetails> wholeList = aurPartialDetailsRepository.findByAurPartialItem_Id(list.get(0).getAurPartialItemId());
                AurPartialItem aurPartialItem = aurPartialItemRepository.findById(list.get(0).getAurPartialItemId()).orElseThrow(() -> new BusinessException("İlgili sipariş detayına ait parçalı ürün kaydı bulunamadı",ENTITY_NAME));

                List<Double> orderAmounts = new ArrayList<>();
                if (list.size() == wholeList.size()) {
                    list.forEach(listItem -> {
                        List<AurPartialDetails> exactItem = wholeList.stream().filter(x -> x.getStockCode().equals(listItem.getStokKodu())).collect(Collectors.toList());
                        Double amount = listItem.getTeslimMiktar() / exactItem.get(0).getQuantity();
                        orderAmounts.add(amount);
                    });
                    Optional<Double> number = orderAmounts.stream().min(Comparator.naturalOrder());
                    Double amount = 0.0;
                    if (number.isPresent()) {
                        amount = number.get();
                    }
                    AurOrderDetail clonedOrderDetail = list.get(0).clone();
                    clonedOrderDetail.setPiece(false);
                    clonedOrderDetail.setSiparisMiktar(amount);
                    clonedOrderDetail.setTeslimMiktar(amount);
                    clonedOrderDetail.setObserverAmount(amount);
                    clonedOrderDetail.setAurPartialItemId(0L);
                    clonedOrderDetail.setBarkod(aurPartialItem.getPackageBarcode());
                    clonedOrderDetail.setStokKodu(aurPartialItem.getPackageCode());
                    clonedOrderDetail.setStokAdi(aurPartialItem.getPackageName());


                    AurOrderDetail savedOne = aurOrderDetailService.save(clonedOrderDetail);
                    filteredList.add(savedOne);
                }
                list.forEach(partialItem -> {
                    if (partialItem.getPiece()) {
                        partialItem.setStatus("DONE");
                    }
                });

            }
            dto.setAurTmpDetailList(aurOrderDetailMapper.toDto(filteredList));
            response.add(dto);
        } else {
            throw new InvalidOrderException();
        }
        return response;

    }

    public List<PalletOrderRelIdDto> getIdFromStockCodeList(List<String> stockCodes, Long aurOrderId) {
        List<PalletOrderRelIdDto> palletOrderRelIdDtos = new ArrayList<>();

        stockCodes.forEach(stockCode -> {
            PalletOrderRelIdDto dto = new PalletOrderRelIdDto();
            List<Long> aurTmpDetailIdList = new ArrayList<>();
            aurOrderDetailService.findByStokKoduAndOrderId(stockCode, aurOrderId).forEach(tmpItem -> aurTmpDetailIdList.add(tmpItem.getId()));
            dto.setStockCode(stockCode);
            dto.setAurTmpDetailIds(aurTmpDetailIdList);

            palletOrderRelIdDtos.add(dto);

        });
        return palletOrderRelIdDtos;
    }

    public List<AurOrderDetail> getOrderDetailByMasterDocNo(String documentNo) {
        Optional<AurOrderMaster> orderMaster = findByDocNo(documentNo);
        if (orderMaster.isPresent()) {
            return aurOrderDetailService.findByOrderId(orderMaster.get().getId());

        } else {
            throw new InvalidOrderException();
        }
    }

    public List<AurOrderDetail> getOrderDetailByOrderInfo(String orderInfo) {
        Optional<AurOrderMaster> orderMaster = findByOrderInfo(orderInfo);
        if (orderMaster.isPresent()) {
            return aurOrderDetailService.findByOrderId(orderMaster.get().getId());

        } else {
            throw new InvalidOrderException();
        }
    }

    public void revertAmountToAddresses(Long aurTmpDetailId, Boolean assignedOrder) {
        List<OrderPickingTransaction> opt = orderPickingTransactionService.findByAurTmpDetailIdAndTransactionType(aurTmpDetailId, TransactionType.PICKING);
        if (opt.isEmpty()) {
            return;
        }

        AurOrderDetail aurOrderDetail = aurOrderDetailService.findById(aurTmpDetailId).orElse(null);

        for (OrderPickingTransaction transactionItem : opt) {
            ProductAddressSaveDTO productAddressSaveDTO = new ProductAddressSaveDTO();
            assert aurOrderDetail != null;
            productAddressSaveDTO.setStokKod(aurOrderDetail.getStokKodu());
            productAddressSaveDTO.setBarcode(aurOrderDetail.getBarkod());
            productAddressSaveDTO.setMiktar(transactionItem.getTransactionAmount());
            productAddressSaveDTO.setBarkodTipi("RAF");
            productAddressSaveDTO.setStatus(true);
            productAddressSaveDTO.setOrderNo("");

            AurDepoUrunAdres address = transactionItem.getAddress();
            productAddressSaveDTO.setDepoCode(String.valueOf(address.getDepoNo()));
            productAddressSaveDTO.setCompanyCode(address.getCompanyCode());
            productAddressSaveDTO.setUrunAdres(address.getAdres());

            aurDepoUrunAdresStokService.saveProductAddress(productAddressSaveDTO);

        }

    }

    public List<TransactionResponseByDocNoDto> getTransactionsByDocumentNo(String documentNo) {
        List<AurOrderDetail> tmpDetails = getOrderDetailByMasterDocNo(documentNo);
        Optional<AurOrderMaster> orderMaster = findByDocNo(documentNo);
        orderMaster.orElseThrow(InvalidOrderException::new);
        return orderPickingTransactionService.getOrderTransactions(tmpDetails, orderMaster.get().getAssignedOrder());
    }

    public List<TransactionResponseByDocNoDto> getTransactionsOrderInfo(String orderInfo) {
        List<AurOrderDetail> tmpDetails = getOrderDetailByOrderInfo(orderInfo);
        Optional<AurOrderMaster> orderMaster = findByOrderInfo(orderInfo);
        orderMaster.orElseThrow(InvalidOrderException::new);
        return orderPickingTransactionService.getOrderTransactions(tmpDetails, orderMaster.get().getAssignedOrder());
    }

    public TransactionResponseByDocNoDto getTransactionsByTmpDetailId(String sipUid, Long orderId, Long partailItemId) {
        AurOrderDetail aurOrderDetail = aurOrderDetailService.findBySipUidAndOrderIdAndAurPartialItemId(sipUid, orderId, partailItemId).orElseThrow(() -> new RuntimeException(translationService.getErrorMessage("tmpDetailService.invalidKey")));
        AurOrderMaster orderMaster = findById(aurOrderDetail.getOrder().getId()).orElseThrow(InvalidOrderException::new);
        return orderPickingTransactionService.getOrderTransactionItem(aurOrderDetail, orderMaster.getAssignedOrder());
    }

    public void revertTransaction(Long transactionId) throws CloneNotSupportedException {
        OrderPickingTransaction transaction = orderPickingTransactionService.findOne(transactionId).orElseThrow(() -> new RuntimeException(translationService.getErrorMessage("opt.invalidKey")));
        AurOrderDetail aurOrderDetail = aurOrderDetailService.findById(transaction.getReferenceId()).orElseThrow(() -> new RuntimeException(translationService.getErrorMessage("tmpDetailService.invalidKey")));
        AurOrderMaster orderMaster = findById(aurOrderDetail.getOrder().getId()).orElseThrow(InvalidOrderException::new);
        Double updatedValue = aurOrderDetail.getTeslimMiktar() - transaction.getTransactionAmount();
        aurOrderDetail.setTeslimMiktar(updatedValue);
        aurOrderDetail.setObserverAmount(updatedValue);
        transaction.setStatus(false);
        if (orderMaster.getOpType().equals("MSK") || orderMaster.getOpType().equals("DAS")) {
            aurDepoUrunAdresStokService.increaseProductAmount(aurOrderDetail, transaction.getTransactionAmount(), transaction.getAddress().getUrunAdresId());
        }
        orderPickingTransactionService.revertOrderTransaction(aurOrderDetail, transaction.getTransactionAmount(), transaction.getAddress().getUrunAdresId());
    }


    public List<TmpOrderDto> getOrderAtDispatchArea(String orderInfo) {
        List<TmpOrderDto> tmpOrderDtoList = new ArrayList<>();
        Optional<AurOrderMaster> orderMaster = findByOrderInfo(orderInfo);
        if (orderMaster.isPresent()) {
            if (orderMaster.get().getBaglantiTipi().equals(FirmConnectionType.CUSTOMER.getValue())) {
                aurOrderMasterRepository.findByFirmCodeAndStatusAndOpTypeAndDepoNo(orderMaster.get().getFirmCode(), "OUT_PROGRESS", orderMaster.get().getOpType(), orderMaster.get().getDepoNo()).stream().filter(q -> q.getId() != orderMaster.get().getId()).forEach(master -> {
                    TmpOrderDto dto = new TmpOrderDto();
                    dto.setMasterId(master.getId());
                    dto.setAurTmpDetailList(aurOrderDetailService.findByOrderId(master.getId()).stream().filter(q -> q.getStatus().equals("OUT_PROGRESS")).collect(Collectors.toList()));
                    tmpOrderDtoList.add(dto);
                });

            }
            if (orderMaster.get().getBaglantiTipi().equals(FirmConnectionType.VENDOR.getValue())) {
                aurOrderMasterRepository.findByStatusAndFirmCodeAndBaglantiTipiAndCariCodeAndDepoNo("OUT_PROGRESS", orderMaster.get().getFirmCode(), orderMaster.get().getBaglantiTipi(), orderMaster.get().getCariCode(), orderMaster.get().getDepoNo()).stream().filter(q -> q.getId() != orderMaster.get().getId()).forEach(master -> {
                    TmpOrderDto dto = new TmpOrderDto();
                    dto.setMasterId(master.getId());
                    dto.setAurTmpDetailList(aurOrderDetailService.findByOrderId(master.getId()).stream().filter(q -> q.getStatus().equals("OUT_PROGRESS")).collect(Collectors.toList()));
                    tmpOrderDtoList.add(dto);

                });

            }
            return tmpOrderDtoList;
        } else {
            throw new InvalidOrderException();
        }
    }

    public void combineRelatedOrder(CombineOrdersDto combineOrdersDto) {
        Optional<AurOrderMaster> aurOrderMaster = aurOrderMasterRepository.findByOrderInfo(combineOrdersDto.getOrderInfo());
        if (aurOrderMaster.isPresent()) {
            combineOrdersDto.getOrderIdList().forEach(masterId -> {
                aurOrderDetailService.findByOrderId(masterId).stream().filter(q -> q.getStatus().equals("OUT_PROGRESS")).forEach(itemAtDispatchArea -> {
                    isSameOrder(aurOrderMaster.get().getId(), itemAtDispatchArea.getId());
                    itemAtDispatchArea.setOrder(aurOrderMaster.get());
                    orderPickingTransactionService.saveOrderCombineTransactions(aurOrderMaster.get().getId(), masterId);
                });
                aurOrderMasterRepository.findById(masterId).get().setStatus("SUSPENDED");
            });

        } else {
            throw new InvalidOrderException();
        }

    }

    public void isSameOrder(Long aurOrderId, Long id) {
        List<AurOrderDetail> aurOrderDetailList = aurOrderDetailService.findByOrderId(aurOrderId);
        Optional<AurOrderDetail> tmpItem = aurOrderDetailService.findById(id);
        if (tmpItem.isPresent()) {
            if (aurOrderDetailList.stream().anyMatch(q -> q.getSipUid().equals(tmpItem.get().getSipUid()) && q.getStatus().equals("OUT_PROGRESS"))) {
                throw new InvalidOrderException();
            }

        } else {
            throw new InvalidOrderException();
        }

    }

    //Parçalı ürünlerde alt parçaların sadece miktarsal güncellemeleri yapılmalı
    AurOrderDetail updatePartialItemFromMicro(AurOrderDetail tmpItem, MicroSipParameterDetailDTO erpResponse) throws CloneNotSupportedException {
        AurOrderDetail updatedItem = new AurOrderDetail();
        Optional<AurPartialDetails> searchItem = aurPartialDetailsService.findByBarcodeAndAurPartialItemId(tmpItem.getBarkod(), tmpItem.getAurPartialItemId());
        if (searchItem.isPresent()) {
            Double perItemQuantity = searchItem.get().getQuantity();
            Double updatedOrderQuantity = erpResponse.getSipMiktar() - erpResponse.getSipTeslimMiktar();
            tmpItem.setSiparisMiktar(updatedOrderQuantity * perItemQuantity);
            updatedItem = tmpItem.clone();
        }

        return updatedItem;
    }

    AurOrderDetail updateItemFromMicro(AurOrderDetail tmpItem, MicroSipParameterDetailDTO erpResponse) throws CloneNotSupportedException {
        AurOrderDetail updatedItem;
        Double updatedOrderQuantity = erpResponse.getSipMiktar() - erpResponse.getSipTeslimMiktar();
        tmpItem.setSiparisMiktar(updatedOrderQuantity);
        tmpItem.setBarkod(erpResponse.getBarKodu());
        tmpItem.setStokKodu(erpResponse.getSipStokKod());
        tmpItem.setStokAdi(erpResponse.getStoIsim());
        updatedItem = tmpItem.clone();

        return updatedItem;
    }

    public List<AurOrderDetail> updateOrderDetailFromMicro(String orderNo) throws Exception {
        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = mikroServices.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        List<AurOrderDetail> updatedList = new ArrayList<>();

        AurOrderMaster masterInfo = isExistOrderByOrderInfo(orderNo);

        aurOrderDetailService.findByOrderId(masterInfo.getId()).forEach(tmpDetailItem -> {
            try {
                AurOrderDetail updatedItem;
                MicroSipParameterDetailDTO response = mikroServices.getOrderParameterDetail(token, aurCompanyDto.getApiEndPoint(), tmpDetailItem.getSipUid());
                if (tmpDetailItem.getPiece()) {
                    updatedItem = updatePartialItemFromMicro(tmpDetailItem, response);

                } else {
                    updatedItem = updateItemFromMicro(tmpDetailItem, response);
                }

                if (updatedItem.getId() != null) {
                    updatedList.add(updatedItem);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        return updatedList;

    }

    public List<AurOrderDetail> getDetailById(Long orderId) {
        Optional<AurOrderMaster> master = findById(orderId);
        if (master.isPresent()) {
            return aurOrderDetailService.findByOrderId(master.get().getId());
        } else {
            throw new InvalidOrderException();
        }
    }

    public List<AurOrderDetail> getActiveDetailById(Long orderId) {
        return aurOrderDetailService.findActiveByOrderId(orderId);
    }

    public void completeOrder(String orderInfo, String orderNo) {
        Optional<AurOrderMaster> masterOrder = aurOrderMasterRepository.findByOrderInfo(orderInfo);
        if (masterOrder.isPresent()) {
            masterOrder.get().setStatus("DONE");
            aurOrderDetailService.findByOrderId(masterOrder.get().getId()).forEach(tmp -> {
                tmp.setStatus("DONE");
                if (WmOperationType.DEPOLAR_ARASI_SEVKIYAT.getOperationType().equals(masterOrder.get().getOpType())) {
                    updateOrderRowFromTmpTable(orderNo, tmp, Integer.parseInt(masterOrder.get().getFirmCode()), masterOrder.get().getDepoNo(), masterOrder.get().getOpType());

                }
                if (WmOperationType.DEPOLAR_ARASI_KABUL.getOperationType().equals(masterOrder.get().getOpType())) {
                    updateOrderRowFromTmpTable(orderNo, tmp, masterOrder.get().getDepoNo(), Integer.parseInt(masterOrder.get().getFirmCode()), masterOrder.get().getOpType());
                }
            });
        }
    }

    public void updateOrderRowFromTmpTable(String orderNo, AurOrderDetail tmp, int entranceWm, int transferWm, String operationType) {
        if (tmp.getTeslimMiktar() > 0) {
            Optional<Order> order = Optional.empty();
            if (WmOperationType.DEPOLAR_ARASI_SEVKIYAT.getOperationType().equals(operationType)) {
                order = orderService.findByOrderNoAndEntranceWarehouseAndTransferWarehouse(orderNo, entranceWm, transferWm);
            }
            if (WmOperationType.DEPOLAR_ARASI_KABUL.getOperationType().equals(operationType)) {
                order = orderService.findByErpDocumentInfoAndEntranceWarehouseAndTransferWarehouse(orderNo, entranceWm, transferWm);
            }

            order.ifPresent(value -> orderRowService.updateOrderRowByBarcodeAndOrderId(value.getId(), tmp.getBarkod(), tmp.getTeslimMiktar(), operationType));
        }
    }

    public void updateOrderDetailStatus(Long orderId, String status) {
        aurOrderDetailService.findByOrderId(orderId).forEach(detail -> {
            detail.setStatus(status);
        });
    }

    public void updateAurTmpSktList(List<AurOrderDetailSktDTO> orderDetailSktList, AurOrderDetail savedOne) {
        log.debug("Order detail skt dates is saving to aur_tmp_detail_skt table {}", orderDetailSktList);
        List<AurOrderDetailSkt> aurTmpDetailSktList = aurOrderDetailSktMapper.toEntity(orderDetailSktList);
        aurTmpDetailSktList.forEach(sktItem -> sktItem.setAurTmpDetail(savedOne));
        List<AurOrderDetailSkt> sktList = aurTmpDetailSktRepository.findByAurTmpDetail_Id(savedOne.getId());
        for (AurOrderDetailSkt aurTmpDetailSkt : aurTmpDetailSktList) {
            List<AurOrderDetailSkt> matchedList = sktList.stream().filter(sktItem -> sktItem.getSktDate().equals(aurTmpDetailSkt.getSktDate())).collect(Collectors.toList());
            List<AurOrderDetailSkt> notMatchedList = sktList.stream().filter(sktItem -> !sktItem.getSktDate().equals(aurTmpDetailSkt.getSktDate())).collect(Collectors.toList());

            if (matchedList.size() == 1) {
                matchedList.get(0).setQuantity(aurTmpDetailSkt.getQuantity());
                aurTmpDetailSktRepository.deleteAll(notMatchedList);
            } else {
                aurTmpDetailSktRepository.save(aurTmpDetailSkt);
                aurTmpDetailSktRepository.deleteAll(notMatchedList);
            }
        }
    }

    public boolean hasPreviousOrderRecordByOrderNo(List<String> orderNoList) {
        long userId = userService.getUserId();
        List<AurOrderDetail> previousDetails = aurOrderDetailService.findBySiparisNoInAndStatusIn(orderNoList, Arrays.asList("OPEN", "IN_PROGRESS", "OUT_PROGRESS"));
        for (AurOrderDetail detail : previousDetails) {
            Optional<AurOrderMaster> orderMaster = aurOrderMasterRepository.findById(detail.getOrder().getId());
            if (orderMaster.isPresent() && !orderMaster.get().getAurUser().getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPreviousOrderRecordBySipUid(List<String> sipUids, String operataionType) {
        List<AurOrderDetail> previousDetails = aurOrderDetailService.findBySipUidInAndStatusIn(sipUids, Arrays.asList("OPEN", "IN_PROGRESS", "OUT_PROGRESS"));
        for (AurOrderDetail detail : previousDetails) {
            Optional<AurOrderMaster> orderMaster = aurOrderMasterRepository.findById(detail.getOrder().getId());
            if (orderMaster.isPresent() && orderMaster.get().getOpType().equals(operataionType)) {
                return true;
            }
        }
        return false;
    }


   public void sendProductToTemporaryAddress(AurOrderMaster orderMaster, long temporaryAddressId) {
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        List<AurOrderDetail> details = aurOrderDetailService.findByOrderId(orderMaster.getId());
        List<AurOrderDetail> nonPartialDetails = details.stream().filter(item -> !item.getPiece()).collect(Collectors.toList());
        nonPartialDetails.forEach(nonPartialItem -> {
            ProductAddressDefinitionDTO productAddressDefinitionDTO = new ProductAddressDefinitionDTO();
            productAddressDefinitionDTO.setDepoNo(String.valueOf(orderMaster.getDepoNo()));
            productAddressDefinitionDTO.setBarcode(nonPartialItem.getBarkod());
            productAddressDefinitionDTO.setStokKodu(nonPartialItem.getStokKodu());
            productAddressDefinitionDTO.setStokAdi(nonPartialItem.getStokAdi());
            productAddressDefinitionDTO.setMiktar(nonPartialItem.getTeslimMiktar());
            productAddressDefinitionDTO.setUrunAdresId(temporaryAddressId);
            aurDepoUrunAdresStokService.productAddressDefinition(productAddressDefinitionDTO);
        });
    }

    public List<AurCariOrderDetailListDto> getFilteredDepoOrderDetails(Object rawData) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsnResponse;
        jsnResponse = JSONArray.fromObject(rawData);
        List<AurCariOrderDetailListDto> cariList = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < jsnResponse.size(); i++) {
            JSONObject item = jsnResponse.getJSONObject(i);
            String orderNo = item.getString("orderNo");
            String orderDate = item.optString("orderDate", "");
            Integer orderLineItemCount = item.optInt("orderLineItemCount", 0);
            JSONArray jsonArray = item.getJSONArray("orderDetail");
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                String onaylayanKullanici = jsonObject.getString("onaylayanKullanici");
                String onayDurumu = onaylayanKullanici.equals("Onaysız") ? "Onaysız" : "Onaylı";
                AurCariOrderDetailListDto aurCariOrderDetailListDto = mapper.readValue(jsonObject.toString(), new TypeReference<AurCariOrderDetailListDto>() {
                });
                if (aurCariOrderDetailListDto.getDurum().equals("0") && aurCariOrderDetailListDto.getTeslimMiktar() < aurCariOrderDetailListDto.getSiparisMiktar()) {
                    aurCariOrderDetailListDto.setId(k);
                    aurCariOrderDetailListDto.setOrderNo(orderNo);
                    aurCariOrderDetailListDto.setOrderDate(orderDate);
                    aurCariOrderDetailListDto.setOrderLineItemCount(orderLineItemCount);
                    aurCariOrderDetailListDto.setOnayDurum(onayDurumu);
                    cariList.add(k, aurCariOrderDetailListDto);
                    k++;
                }
            }
        }
        return enrichDepoOrderDetails(cariList);
    }

    /**
     * Siparis satirlarini WMS'in kendi {@code aur_order_detail} kayitlariyla zenginlestirir:
     * satir halihazirda bir toplama/sevk emrine baglanmissa {@code aktif} isaretlenir ve
     * isi ustlenen kullanici {@code kullaniciAdi} alanina yazilir.
     *
     * <p>ERP'den bagimsizdir: hem Mikro yanitindan uretilen liste hem de yerel
     * (entegrasyonsuz) modda uretilen liste ayni zenginlestirmeden gecer; boylece
     * istemciye donen yanit her iki modda da ayni sekle sahiptir.
     */
    public List<AurCariOrderDetailListDto> enrichDepoOrderDetails(List<AurCariOrderDetailListDto> cariList) {
        List<String> sipUidList = cariList.stream().map(AurCariOrderDetailListDto::getSipUid).collect(Collectors.toList());
        List<String> statuses = List.of("OPEN", "IN_PROGRESS", "OUT_PROGRESS");
        List<AurOrderDetail> tmpDetails = aurOrderDetailService.findBySipUidInAndStatusIn(sipUidList, statuses);
        Map<String, AurOrderDetail> sipUidToDetailMap = tmpDetails.stream()
            .collect(Collectors.toMap(
                AurOrderDetail::getSipUid,
                Function.identity(),
                (d1, d2) -> d1
            ));

        for (AurCariOrderDetailListDto dto : cariList) {
            AurOrderDetail detail = sipUidToDetailMap.get(dto.getSipUid());
            String user = "";
            if (detail != null && detail.getOrder() != null && detail.getOrder().getAurUser() != null) {
                user = detail.getOrder().getAurUser().getLogin();
            }
            dto.setKullaniciAdi(user);
            boolean status = detail != null;
            dto.setAktif(status);

        }
        return cariList;
    }

    public void updateBatchRequestId(String orderInfo,String batchRequestId){
        AurOrderMaster searchOrder = isExistOrderByOrderInfo(orderInfo);
        searchOrder.setCariCode(batchRequestId);
        updateOrderToStatus(orderInfo,"OUT_PROGRESS");
    }

    public void updateOrderToStatus(String orderInfo,String status){
        AurOrderMaster searchOrder = isExistOrderByOrderInfo(orderInfo);
        searchOrder.setStatus(status);
        updateOrderDetailStatus(searchOrder.getId(),status);
    }

    public AurOrderMaster updateBelgeNo(String orderInfo,String belgeNo,OrderStatus orderStatus){
        AurOrderMaster searchOrder = isExistOrderByOrderInfo(orderInfo);

        if(searchOrder.getBelgeNo() == null){
            searchOrder.setBelgeNo("Dev");
        }
        searchOrder.setBelgeNo(searchOrder.getBelgeNo().concat(":").concat(belgeNo));
        updateOrderToStatus(orderInfo, orderStatus.toString());
        return searchOrder;
    }

    public CloseOrderDto generateCloseOrderDto(String orderInfo){
        AurOrderMaster searchOrder = isExistOrderByOrderInfo(orderInfo);
        if(!searchOrder.getStatus().equals(BatchRequestStatus.SUCCESS.getValue())){
            throw new RuntimeException("Siparis statüsü uygun değildir");
        }
        List<IrsaliyeDetailRequestDTO> orderDetailList = new ArrayList<>();
        List<AurOrderDetail> tmpDetails = getDetailById(searchOrder.getId());
        CloseOrderDto closeOrderDto = new CloseOrderDto();
        String depoNo = AurHelper.addSpecificCharToAnyIndex(String.valueOf(searchOrder.getDepoNo()),2);
        closeOrderDto.setDepoNo(depoNo);
        closeOrderDto.setFirmCode("2022C");
        closeOrderDto.setOrderNo(tmpDetails.get(0).getSiparisNo());
        tmpDetails.forEach(item -> {
            IrsaliyeDetailRequestDTO dto = new IrsaliyeDetailRequestDTO();
            dto.setStokKodu(item.getStokKodu());
            dto.setHareketMiktar(item.getTeslimMiktar());
            dto.setSipUid(item.getSipUid());
            orderDetailList.add(dto);
        });
        closeOrderDto.setOrderDetailList(orderDetailList);

        return closeOrderDto;
    }

    public Optional<AurOrderMaster> partialUpdate(AurOrderMaster orderMaster) {
        log.debug("Request to partially update AurOrderMaster : {}", orderMaster);
        return aurOrderMasterRepository.findById(orderMaster.getId()).map(existingAurOrderMaster -> {
            if (existingAurOrderMaster.getStatus() != null) {
                existingAurOrderMaster.setStatus(orderMaster.getStatus());
                updateOrderDetailStatus(existingAurOrderMaster.getId(),orderMaster.getStatus());
            }

            return existingAurOrderMaster;
        }).map(aurOrderMasterRepository::save);
    }

    public void transferOrderControlArea(AurOrderMaster aurOrderMaster){
        aurOrderDetailService.validatePartialItemsOnClose(aurOrderMaster.getId());
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        Integer warehouseCode = aurOrderMaster.getDepoNo();
        aurOrderMaster.getDetails().forEach(detail -> {
            if(detail.getTeslimMiktar() == 0){
                detail.setStatus(OrderStatus.SUSPENDED.name());
            }
            else if(!detail.getStatus().equals(OrderStatus.SUSPENDED.name())){
                detail.setStatus(OrderStatus.OUT_PROGRESS.name());
            }

        });

        aurOrderMaster.getDetails().stream()
            .filter(detail -> !detail.getStatus().equals(OrderStatus.SUSPENDED.name()))
            .collect(Collectors.groupingBy(AurOrderDetail::getStokKodu))
            .forEach((stockCode, group) -> {
                AurOrderDetail first = group.get(0);

                ProductAddressSaveDTO dto = new ProductAddressSaveDTO();
                dto.setStokKod(first.getStokKodu());
                dto.setBarcode(first.getBarkod());
                dto.setStokAdi(first.getStokAdi());
                dto.setOrderNo(first.getSiparisNo());
                dto.setUrunAdres(aurOrderMaster.getControlAddress().getAdres());
                dto.setCompanyCode(companyCode);
                dto.setDepoCode(String.valueOf(warehouseCode));
                dto.setMiktar(group.stream()
                    .mapToDouble(d -> d.getTeslimMiktar() != null ? d.getTeslimMiktar() : 0.0)
                    .sum());
                dto.setSktDateList(group.stream()
                    .flatMap(d -> d.getAurTmpDetailSktList().stream())
                    .collect(Collectors.toList()));

                aurDepoUrunAdresStokService.assignProductToAddress(dto);
            });
    }

    public void completeDispatcher(SevkiyatRequestDto sevkiyatRequestDto) throws Exception {
        log.debug("Dispatcher complement service is started : {}",sevkiyatRequestDto);
        AurOrderMaster orderMaster = findById(sevkiyatRequestDto.getOrderId()).orElseThrow(InvalidOrderException::new);
        AurOrderMasterDTO aurOrderMasterDTO = aurOrderMasterMapper.masterToDTO(orderMaster);
        aurOrderMasterDTO.setStatus(OrderStatus.DONE.name());
        aurOrderMasterDTO.setCompanyLogistics(sevkiyatRequestDto.getCompanyLogistics());
        aurOrderMasterDTO.setTransportationType(sevkiyatRequestDto.getTransportationType());
        aurOrderMasterDTO.setCarryType(sevkiyatRequestDto.getCarryType());
        //TODO Driver id olarak güncellenecek
        aurOrderMasterDTO.setSoforAdi(sevkiyatRequestDto.getSoforAdi());
        aurOrderMasterDTO.setSoforTel(sevkiyatRequestDto.getSoforTel());
        aurOrderMasterDTO.setSoforPlaka(sevkiyatRequestDto.getAracPlakaNo() + sevkiyatRequestDto.getDorsePlakaNo());
        List<String> sipUids = sevkiyatRequestDto.getOrderDetailList().stream().map(SevkiyatOrderLineItemDto::getSipUid).collect(Collectors.toList());
        List<AurOrderDetail> orderDetails = aurOrderDetailService.findBySipUidInAndIsPieceAndOrderIdAndStatus(sipUids,false,orderMaster.getId(),OrderStatus.OUT_PROGRESS.name());
        orderDetails.forEach(detail -> detail.setStatus(OrderStatus.DONE.name()));
        aurOrderMasterDTO.setAurTmpDetailList(aurOrderDetailMapper.toDto(orderDetails));
        saveAurOrderList(aurOrderMasterDTO);
        palletBarcodeOrderRelService.completePalletBarcodes(orderMaster.getId());
    }

    public void completeReceiving(MalKabulRequestDto malKabulRequestDto){
        AurOrderMaster orderMaster = findById(malKabulRequestDto.getOrderId()).orElseThrow(InvalidOrderException::new);
        orderMaster.setBelgeNo(malKabulRequestDto.getOrderNo());
        orderMaster.setCompanyLogistics(malKabulRequestDto.getCompanyLogistics());
        orderMaster.setTransportationType(malKabulRequestDto.getTransportationType());
        orderMaster.setCarryType(malKabulRequestDto.getCarryType());
        orderMaster.setSoforAdi(malKabulRequestDto.getSoforAdi());
        orderMaster.setSoforPlaka(malKabulRequestDto.getAracPlakaNo() + "-" + malKabulRequestDto.getDorsePlakaNo());
        orderMaster.setSoforTcNo(malKabulRequestDto.getSoforTckn());
        orderMaster.setSoforTel(malKabulRequestDto.getSoforTel());
        updateOrderToStatus(orderMaster.getOrderInfo(),OrderStatus.DONE.name());
    }
}
