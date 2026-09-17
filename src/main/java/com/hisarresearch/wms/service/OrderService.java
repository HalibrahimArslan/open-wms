package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.Order;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.WmOperationType;
import com.hisarresearch.wms.repository.OrderRepository;
import com.hisarresearch.wms.repository.OrderRowRepository;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.dto.OrderDTO;
import com.hisarresearch.wms.service.dto.OrderRowDTO;
import com.hisarresearch.wms.service.dto.OrderStatusDTO;
import com.hisarresearch.wms.service.dto.base.RequestDto;
import com.hisarresearch.wms.service.dto.productaddress.ProductAddressDefinitionDTO;
import com.hisarresearch.wms.service.dto.interwarehouse.InterwarehouseOrderRequestDTO;
import com.hisarresearch.wms.service.dto.mikro.MicroOrderDetailDto;
import com.hisarresearch.wms.service.dto.mikro.MicroOrderDto;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.mapper.OrderMapper;
import com.hisarresearch.wms.service.mapper.OrderStatusMapper;
import com.hisarresearch.wms.service.erp.ErpTokenService;
import com.hisarresearch.wms.utility.AurHelper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final String ENTITY_NAME = "order";

    private final OrderRepository orderRepository;

    private final OrderRowRepository orderRowRepository;

    private final OrderMapper orderMapper;

    private final OrderStatusMapper orderStatusMapper;

    private final UserService userService;

    private final ErpTokenService erpTokenService;

    private final OrderRowService orderRowService;

    private final OrderStatusService orderStatusService;

    private final HttpService httpService;

    private final AddressService addressService;

    private final AurOrderMasterService aurOrderMasterService;

    private final AurOrderDetailService aurOrderDetailService;

    private final TranslationService translationService;

    private final AurDepoUrunAdresStokService productAddressService;

    public OrderService(OrderRepository orderRepository, OrderRowRepository orderRowRepository,
                        OrderMapper orderMapper, OrderStatusMapper orderStatusMapper,
                        UserService userService, ErpTokenService erpTokenService,
                        OrderRowService orderRowService, OrderStatusService orderStatusService,
                        HttpService httpService, AddressService addressService, AurOrderMasterService aurOrderMasterService, AurOrderDetailService aurOrderDetailService,
                        TranslationService translationService, AurDepoUrunAdresStokService productAddressService
                        ) {
        this.orderRepository = orderRepository;
        this.orderRowRepository = orderRowRepository;
        this.orderMapper = orderMapper;
        this.orderStatusMapper = orderStatusMapper;
        this.userService = userService;
        this.erpTokenService = erpTokenService;
        this.orderRowService = orderRowService;
        this.orderStatusService = orderStatusService;
        this.httpService = httpService;
        this.addressService = addressService;
        this.aurOrderMasterService = aurOrderMasterService;
        this.aurOrderDetailService = aurOrderDetailService;
        this.translationService = translationService;
        this.productAddressService = productAddressService;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        if (orderDTO.getDocumentType() != 1 && orderDTO.getEntranceWarehosue().equals(orderDTO.getTransferWarehouse())) {
            throw new BadRequestAlertException(translationService.getErrorMessage("order.crossWarehouse"), ENTITY_NAME, "crossWarehouse");
        }
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        Order finalOrder = order;
        finalOrder.setOrderNo("AUR-".concat(String.valueOf(finalOrder.getId())));
        order.getOrderRows().forEach(orderRow -> {
            orderRow.setOrder(finalOrder);
            orderRowRepository.save(orderRow);
        });
        return orderMapper.toDto(order);
    }

    /**
     * Partially update a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(
                existingOrder -> {
                    orderMapper.partialUpdate(existingOrder, orderDTO);

                    return existingOrder;
                }
            )
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    /**
     * Get all the orders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        log.debug("Request to get all Orders");
        return orderRepository.findAll().stream().map(orderMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    /**
     * Transfer order to Micro API by id
     *
     * @param id the id of the entity
     */
    public OrderDTO transferMicro(Long id) throws Exception {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("There is no related order by " + id));

        if (order.getIsTransferred()) {
            throw new RuntimeException("Related order is transferred" + id);
        }

        String userName = userService.getUserName();
        List<InterwarehouseOrderRequestDTO> transferDto;
        MicroOrderDto transferOrderDto;
        RequestDto requestDto = new RequestDto();

        if (order.getDocumentType() == 0) {
            transferDto = orderRowService.findByOrderId(id)
                .stream()
                .map(item -> orderRowDTOToIWarehouse(item, order))
                .collect(Collectors.toList());
            requestDto.setServiceName("siparislerService.depolarArasiSiparisOlustur");
            requestDto.setData(transferDto);
        }
        if (order.getDocumentType() == 1) {
            List<OrderRowDTO> orderRowDTOList = orderRowService.findByOrderId(id);
            transferOrderDto = orderRowToMicroOrder(orderRowDTOList, order);
            requestDto.setServiceName("siparislerService.siparisOlustur");
            requestDto.setData(transferOrderDto);
        }

        AurCompanyDTO aurCompanyDto = userService.getUserCompanyInfo();
        String token = erpTokenService.getToken(aurCompanyDto.getApiEndPoint(), aurCompanyDto.getApiParameters());
        String response = (String) httpService.executeService(token, aurCompanyDto.getApiEndPoint(), requestDto);
        order.setOrderNo(response);
        order.setIsTransferred(true);
        OrderStatusDTO orderStatusDTO = orderStatusService.findOne(2L).get();
        order.setOrderStatus(orderStatusMapper.toEntity(orderStatusDTO));
        order.setMicroTransferDate(Instant.now());
        order.setMicroTransferBy(userName);

        return orderMapper.toDto(order);
    }

    InterwarehouseOrderRequestDTO orderRowDTOToIWarehouse(OrderRowDTO dto, Order order) {
        InterwarehouseOrderRequestDTO interwarehouseOrderRequestDTO = new InterwarehouseOrderRequestDTO();

        interwarehouseOrderRequestDTO.setSsipStokKod(dto.getStockCode());
        interwarehouseOrderRequestDTO.setSsipMiktar(Double.valueOf(dto.getProductQuantity()));
        interwarehouseOrderRequestDTO.setSsipAciklama("WMS");
        interwarehouseOrderRequestDTO.setSsipGirdepo(order.getEntranceWarehosue());
        interwarehouseOrderRequestDTO.setSsipCikdepo(order.getTransferWarehouse());

        return interwarehouseOrderRequestDTO;
    }

    MicroOrderDto orderRowToMicroOrder(List<OrderRowDTO> orderRowDTOList, Order order) {
        MicroOrderDto microOrderDto = new MicroOrderDto();
        microOrderDto.setOrderDate(AurHelper.getDate());
        microOrderDto.setOrderNo(order.getOrderNo());
        microOrderDto.setCariKod(order.getCariCode());
        microOrderDto.setDepoNo(order.getEntranceWarehosue());
        List<MicroOrderDetailDto> orderDetailDto = orderRowDTOList.stream().map(item -> {
            MicroOrderDetailDto detail = new MicroOrderDetailDto();
            detail.setStokKodu(item.getStockCode());
            detail.setHareketMiktar(item.getProductQuantity());
            return detail;
        }).collect(Collectors.toList());
        microOrderDto.setOrderDetailList(orderDetailDto);
        return microOrderDto;
    }

    public void changeStatus(String orderNo, int girdepo, int cikDepo, String operationType, String erpInfo) {
        Optional<Order> searchOrder = Optional.empty();
        String userName = userService.getUserName();
        if (WmOperationType.DEPOLAR_ARASI_SEVKIYAT.getOperationType().equals(operationType)) {
            searchOrder = orderRepository.findByOrderNoAndEntranceWarehosueAndTransferWarehouse(orderNo, girdepo, cikDepo);
        }
        if (WmOperationType.DEPOLAR_ARASI_KABUL.getOperationType().equals(operationType)) {
            searchOrder = orderRepository.findByErpDocumentInfoAndEntranceWarehosueAndTransferWarehouse(orderNo, girdepo, cikDepo);
        }
        if (searchOrder.isPresent()) {
            if (WmOperationType.DEPOLAR_ARASI_SEVKIYAT.getOperationType().equals(operationType)) {
                OrderStatusDTO orderStatusDTO = getTransferStatus();
                searchOrder.get().setOrderStatus(orderStatusMapper.toEntity(orderStatusDTO));
                searchOrder.get().setErpDocumentInfo(erpInfo);
                searchOrder.get().setShipmentDate(Instant.now());
                searchOrder.get().setShipmentBy(userName);
            }
            if (WmOperationType.DEPOLAR_ARASI_KABUL.getOperationType().equals(operationType)) {
                OrderStatusDTO orderStatusDTO = orderStatusService.findOne(4L).orElseThrow(() -> new BusinessException("Invalid Order Status", ENTITY_NAME, "invalidOrderStatus"));
                searchOrder.get().setOrderStatus(orderStatusMapper.toEntity(orderStatusDTO));
                searchOrder.get().setAcceptanceDate(Instant.now());
                searchOrder.get().setAcceptanceBy(userName);
            }

        }
    }

    @Transactional
    public Optional<Order> findByOrderNoAndEntranceWarehouseAndTransferWarehouse(String orderNo, int entranceWarehouse, int transferWarehouse) {
        return orderRepository.findByOrderNoAndEntranceWarehosueAndTransferWarehouse(orderNo, entranceWarehouse, transferWarehouse);
    }

    @Transactional
    public Optional<Order> findByErpDocumentInfoAndEntranceWarehouseAndTransferWarehouse(String erpInfo, int entranceWarehouse, int transferWarehouse) {
        return orderRepository.findByErpDocumentInfoAndEntranceWarehosueAndTransferWarehouse(erpInfo, entranceWarehouse, transferWarehouse);
    }

    public void completeOrder(Long id, Long aurOrderId) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("There is no related order by " + id));
        List<AurOrderDetail> aurOrderDetails = aurOrderDetailService.findByAurOrderId(aurOrderId);
        if (aurOrderDetails.isEmpty()) {
            String errorMessage = translationService.getErrorMessage("order.invalidWmsOrder");
            throw new BusinessException(errorMessage, ENTITY_NAME, "order.invalidWmsOrder");
        }

        if (order.getOrderStatus().getId().equals(1L)) {
            transferInterWarehouseOrder(order, aurOrderDetails);
            return;
        }
        if (order.getOrderStatus().getId().equals(3L)) {
            receiveInterWarehouseOrder(order, aurOrderDetails);
        }
    }

    public void transferInterWarehouseOrder(Order order, List<AurOrderDetail> aurOrderDetails) {

        long emptyRows = order.getOrderRows()
            .stream()
            .filter(orderRow -> orderRow.getTransferAmount() != null)
            .filter(orderRow -> orderRow.getTransferAmount() == 0).count();

        if (emptyRows == (long) order.getOrderRows().size()) {
            throw new BusinessException(translationService.getErrorMessage("order.emptyRow"), ENTITY_NAME, "emptyRow");
        }
        order.getOrderRows().forEach(orderRow -> {
            aurOrderDetails.stream()
                .filter(q -> q.getBarkod().equals(orderRow.getBarcode()))
                .findFirst()
                .ifPresent(q -> {
                    orderRow.setTransferAmount(q.getTeslimMiktar());
                    orderRow.setReceivingAmount(q.getTeslimMiktar());
                });
        });
        order.setOrderStatus(orderStatusMapper.toEntity(getTransferStatus()));
    }

    public void receiveInterWarehouseOrder(Order order, List<AurOrderDetail> aurOrderDetails) {
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        String depoCode = String.valueOf(order.getEntranceWarehosue());
        List<AurDepoUrunAdres> temporaryAddresses = addressService.findByDepoNoAndCompanyCodeAndGeciciAdres(depoCode, companyCode, true);

        if (temporaryAddresses.isEmpty()) {
            String errorMessage = translationService.getErrorMessage("temporaryAddress.notFound");
            throw new BusinessException(errorMessage, ENTITY_NAME, "temporaryAddress.notFound");
        }

        order.getOrderRows().forEach(orderRow -> {
            if (orderRow.getReceivingAmount() > 0) {
                ProductAddressDefinitionDTO sayimSaveDto = new ProductAddressDefinitionDTO();
                sayimSaveDto.setDepoNo(depoCode);
                sayimSaveDto.setMiktar(orderRow.getReceivingAmount());
                sayimSaveDto.setBarcode(orderRow.getBarcode());
                sayimSaveDto.setUrunAdresId(temporaryAddresses.get(0).getUrunAdresId());
                sayimSaveDto.setStokKodu(orderRow.getStockCode());
                sayimSaveDto.setStokAdi(orderRow.getStockName());
                productAddressService.productAddressDefinition(sayimSaveDto);
            }
        });
        String orderInfo = "AUR-" + aurOrderDetails.get(0).getOrder().getId();
        aurOrderMasterService.updateOrderToStatus(orderInfo, "DONE");

        order.setOrderStatus(orderStatusMapper.toEntity(getReceivedStatus()));

    }

    public OrderStatusDTO getTransferStatus() {
        return orderStatusService.findOne(3L).orElseThrow(() -> new BusinessException("Invalid Order Status", ENTITY_NAME, "invalidOrderStatus"));
    }

    public OrderStatusDTO getReceivedStatus() {
        return orderStatusService.findOne(4L).orElseThrow(() -> new BusinessException("Invalid Order Status", ENTITY_NAME, "invalidOrderStatus"));
    }

    public OrderStatusDTO getCancelledStatus() {
        return orderStatusService.findOne(6L).orElseThrow(() -> new BusinessException("Invalid Order Status", ENTITY_NAME, "invalidOrderStatus"));
    }

    public OrderDTO cancelOrder(Long id, Long aurOrderId) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("There is no related order by " + id));
        order.setOrderStatus(orderStatusMapper.toEntity(getCancelledStatus()));
        aurOrderMasterService.suspendOrder(aurOrderId);
        return orderMapper.toDto(order);
    }

}
