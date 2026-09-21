package com.hisarresearch.wms.web.rest;


import com.hisarresearch.wms.domain.AurDispatchAreaControl;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.service.*;
import com.hisarresearch.wms.service.dto.*;
import com.hisarresearch.wms.service.mapper.AurOrderDetailMapper;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hisarresearch.wms.framework.web.util.HeaderUtil;
import com.hisarresearch.wms.framework.web.util.ResponseUtil;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AurOrderDetailResource {

    private static final String ENTITY_NAME = "aurTmpDetail";

    private final Logger log = LoggerFactory.getLogger(AurOrderDetailResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    private final AurOrderMasterService aurOrderMasterService;

    private final AurOrderDetailService aurOrderDetailService;

    private final AurDispatchAreaControlService aurDispatchAreaControlService;

    private final AurOrderDetailMapper aurOrderDetailMapper;


    public AurOrderDetailResource(AurOrderMasterService aurOrderMasterService, AurOrderDetailService aurOrderDetailService,
                                  AurDispatchAreaControlService aurDispatchAreaControlService, AurOrderDetailMapper aurOrderDetailMapper
    ) {
        this.aurOrderMasterService = aurOrderMasterService;
        this.aurOrderDetailService = aurOrderDetailService;
        this.aurDispatchAreaControlService = aurDispatchAreaControlService;
        this.aurOrderDetailMapper = aurOrderDetailMapper;
    }


    @GetMapping("/aur-tmp-order")
    public List<AurOrderMasterDTO> getAurOrder() {
        return aurOrderMasterService.getAurTmpOrderList();
    }

    @PostMapping("/aur-tmp-detail")
    public void saveAurOrder(@RequestBody AurOrderMasterDTO aurOrderMasterDTO) throws Exception {
        aurOrderMasterService.saveAurOrderList(aurOrderMasterDTO);
    }

    @PostMapping("/aur-tmp-detail-without-assign")
    public AurOrderMaster saveAurOrderWithoutAssign(@RequestBody @Valid AurOrderMasterDTO aurOrderMasterDTO) throws Exception {
        return aurOrderMasterService.saveAurOrderListWithoutAssign(aurOrderMasterDTO);
    }


    @GetMapping("/aur-picking-tmp-detail/{opType}/{depoNo}/{info}")
    public List<AurOrderDetailWithPartialDTO> getPickingTmpOrderByUserName(@PathVariable String opType, @PathVariable Integer depoNo, @PathVariable String info) {
        return aurOrderMasterService.getPickingTmpOrderByUserName(opType,depoNo,info);
    }

    @GetMapping("/aur-tmp-detail/{opType}/{depoNo}")
    public List<AurOrderMasterDTO> getTmpOrderByUserName(@PathVariable String opType, @PathVariable Integer depoNo) {
        return aurOrderMasterService.getAurTmpOrderListByUserName(opType,depoNo);
    }


    @PostMapping("/aur-order-defined")
    public List<AurOrderDetail> getAurOrderUserDefined(@RequestBody AurOrderUserDefinedDto aurOrderUserDefinedDto) {
        return aurOrderMasterService.getAurOrderListByUserDefined(
            aurOrderUserDefinedDto.getFirmCode(),
            aurOrderUserDefinedDto.getStatus(),
            aurOrderUserDefinedDto.getOpType(),
            aurOrderUserDefinedDto.getDepoNo()
        );
    }


    @GetMapping("/aur-done-order-user-tmp/{opType}/{depoNo}/{controlAddressId}")
    public List<AurOrderMasterDTO> getAurOrderDone(@PathVariable String opType, @PathVariable Integer depoNo, @PathVariable Long controlAddressId)  {
        return aurOrderMasterService.getDoneAurOrderList(opType,depoNo,controlAddressId);
    }

    @GetMapping("/suspend-order/{id}")
    public String suspendOrder(@PathVariable Long id) {
        return aurOrderMasterService.suspendOrder(id);
    }

    @GetMapping("/aur-tmp-order-by-id/{id}")
    public List<AurOrderMasterDTO> getTmpListById(@PathVariable Long id) {
        return aurOrderMasterService.getById(id);
    }

    @PostMapping("/aur-reform-order")
    public String reformOrder(@RequestBody AurOrderDetailReformDTO aurTmpDetailReformDto) {
        return aurOrderMasterService.reformAurOrderTmpDetail(aurTmpDetailReformDto);
    }
    @PostMapping("/aur-reform-order-sevkiyat")
    public String reformOrderSevkiyat(@RequestBody AurOrderDetailReformSevkiyatDTO aurTmpDetailReformSevkiyatDto) {
        return aurOrderMasterService.reformAurOrderTmpDetailSevkiyat(aurTmpDetailReformSevkiyatDto);
    }

    @GetMapping("/aur-tmp-order/{depoCode}/{firmCode}/{opType}/{orderNo}")
    public List<AurOrderDetailResponseDTO> findOrderByFirmCodeAndOrderNo(@PathVariable int depoCode, @PathVariable String firmCode, @PathVariable String opType, @PathVariable String orderNo) {
        return aurOrderMasterService.findTmpOrderByFirmCodeAndOrderNo(depoCode, firmCode, opType, orderNo);
    }

    @PostMapping("/aur-tmp-order-list")
    public List<AurOrderDetailResponseDTO> findOrderListByFirmCodeAndOrderNo(@RequestBody AurOrderWithoutAssignDto aurOrderWithoutAssignDto) {
        List<AurOrderDetailResponseDTO> searchList = new ArrayList<>();

        for(String item : aurOrderWithoutAssignDto.getOrderList()){
            List<AurOrderDetailResponseDTO> response = aurOrderMasterService.findTmpOrderByFirmCodeAndOrderNo(aurOrderWithoutAssignDto.getDepoCode(),aurOrderWithoutAssignDto.getFirmCode(),aurOrderWithoutAssignDto.getOpType(),item);
            if(!response.isEmpty()){
                searchList.addAll(response);
            }
        }

        return searchList;
    }

    @PostMapping("/aur-tmp-order-list-at-dispatch-area")
    public List<AurOrderDetail> findOrderListByFirmCodeAndOrderNoAtDispatchArea(@RequestBody AurOrderWithoutAssignDto aurOrderWithoutAssignDto) {
        List<AurOrderDetail> tmpList = new ArrayList<>();

        for(String item : aurOrderWithoutAssignDto.getOrderList()){
            List<AurOrderDetail> response = aurOrderMasterService.findTmpOrderByFirmCodeAndOrderNoAtDispatchArea(aurOrderWithoutAssignDto.getDepoCode(),aurOrderWithoutAssignDto.getFirmCode(),aurOrderWithoutAssignDto.getOpType(),item);
            if(!response.isEmpty()){
                tmpList.addAll(response);
            }

        }

        return tmpList;
    }

    @GetMapping("/aur-done-order-by-order-info/{orderInfo}")
    public List<AurOrderMasterDTO> getAurOrderDone(@PathVariable String orderInfo) throws CloneNotSupportedException {
        return aurOrderMasterService.getAurTmpListWithStatusDone(orderInfo);
    }

    @GetMapping("/aur-tmp-detail/{id}")
    public ResponseEntity<AurOrderDetail> getAurTmpDetailById(@PathVariable Long id) {
        log.debug("REST request to get AurTmpDetail : {}", id);
        Optional<AurOrderDetail> aurTmpDetail = aurOrderDetailService.findById(id);
        return ResponseUtil.wrapOrNotFound(aurTmpDetail);
    }

    @PatchMapping(value = "/aur-tmp-detail/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AurOrderDetail> partialUpdateAurTmpDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AurOrderDetail aurOrderDetail
    ) {
        log.debug("REST request to partial update AurTmpDetail partially : {}, {}", id, aurOrderDetail);
        if (aurOrderDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurOrderDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (aurOrderDetailService.findById(id).isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurOrderDetail> result = aurOrderDetailService.partialUpdate(aurOrderDetail);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aurOrderDetail.getId().toString())
        );
    }

    @GetMapping("/order-transactions/{documentNo}")
    public ResponseEntity<List<TransactionResponseByDocNoDto>> getTransactionsByDocumentNo(@PathVariable String documentNo){
        log.debug("REST request to get transactions by document no {}", documentNo);
        return ResponseEntity.ok().body(aurOrderMasterService.getTransactionsByDocumentNo(documentNo));
    }

    @GetMapping("/order-transactions-by-order-info/{orderInfo}")
    public ResponseEntity<List<TransactionResponseByDocNoDto>> getTransactionsByOrderInfo(@PathVariable String orderInfo){
        log.debug("REST request to get transactions by orderInfo {}", orderInfo);
        return ResponseEntity.ok().body(aurOrderMasterService.getTransactionsOrderInfo(orderInfo));
    }

    @GetMapping("/order-transactions-by-tmp-detail-id/{orderId}/{sipUid}/{partialItemId}")
    public ResponseEntity<TransactionResponseByDocNoDto> getTransactionsByOrderInfo(@PathVariable Long orderId,@PathVariable String sipUid,@PathVariable Long partialItemId){
        log.debug("REST request to get transactions by orderId {} , sipUid {} and partialItemId {}", orderId, sipUid, partialItemId);
        return ResponseEntity.ok().body(aurOrderMasterService.getTransactionsByTmpDetailId(sipUid,orderId, partialItemId));
    }

    @PutMapping("/order-transactions-by-tmp-detail-id/{transactionId}")
    public ResponseEntity<Void> revertOrderTransaction(@PathVariable Long transactionId) throws CloneNotSupportedException {
        log.debug("REST request to get transactions by sipUid {}", transactionId);
        aurOrderMasterService.revertTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/firm-related-orders/{orderInfo}")
    public ResponseEntity<List<TmpOrderDto>> getOtherOrdersAtDispatchArea(@PathVariable String orderInfo){
        log.debug("REST request to get other orders of orderInfos related firm {}",orderInfo);
        return ResponseEntity.ok().body(aurOrderMasterService.getOrderAtDispatchArea(orderInfo));
    }

    @PostMapping("/combine-orders")
    public ResponseEntity<Void> combineOrders(@RequestBody CombineOrdersDto combineOrdersDto){
        log.debug("REST request to combine orders of same customer or vendor orders {}",combineOrdersDto);

        aurOrderMasterService.combineRelatedOrder(combineOrdersDto);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName,true,ENTITY_NAME,combineOrdersDto.toString()))
            .build();
    }

    @Operation(summary = "Fetch aur_order_detail type is lazy")
    @GetMapping("/dispatch-area-control-list")
    public ResponseEntity<List<AurDispatchAreaControl>> getAllAurDispatchAreaControlList(){
        return ResponseEntity.ok(aurDispatchAreaControlService.findAll());
    }

    @GetMapping("/update-order-detail-from-micro/{orderInfo}")
    public ResponseEntity<List<AurOrderDetail>> updateOrderFromMicro(@PathVariable String orderInfo) throws Exception {
        return ResponseEntity.ok(aurOrderMasterService.updateOrderDetailFromMicro(orderInfo));
    }


}
