package com.hisarresearch.wms.service.impl;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.OrderPickingTransaction;
import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.domain.enumeration.TransactionType;
import com.hisarresearch.wms.repository.OrderPickingTransactionRepository;
import com.hisarresearch.wms.service.AurDepoUrunAdresStokService;
import com.hisarresearch.wms.service.OrderPickingTransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.dto.AddressTransactionsDetailDto;
import com.hisarresearch.wms.service.dto.DepolarArasiTransferDto;
import com.hisarresearch.wms.service.dto.TransactionResponseByDocNoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderPickingTransaction}.
 */
@Service
@Transactional
public class OrderPickingTransactionServiceImpl implements OrderPickingTransactionService {

    private final Logger log = LoggerFactory.getLogger(OrderPickingTransactionServiceImpl.class);

    @Autowired
    private OrderPickingTransactionRepository orderPickingTransactionRepository;

    @Autowired
    private AurDepoUrunAdresStokService aurDepoUrunAdresStokService;

    @Override
    public OrderPickingTransaction save(OrderPickingTransaction orderPickingTransaction) {
        log.debug("Request to save OrderPickingTransaction : {}", orderPickingTransaction);

        Optional<OrderPickingTransaction> opt = orderPickingTransactionRepository.findByReferenceIdAndAddress_UrunAdresIdAndStockCodeAndTransactionType(orderPickingTransaction.getReferenceId(),orderPickingTransaction.getAddress().getUrunAdresId(),orderPickingTransaction.getStockCode(),TransactionType.NONE_COUNTABLE_ITEM);
        if(!opt.isPresent()){
            orderPickingTransaction.setTransactionAmount(0.0);
            orderPickingTransaction.setStatus(true);
            return orderPickingTransactionRepository.save(orderPickingTransaction);

        }
        else {
            return opt.get();
        }
    }

    @Override
    public Optional<OrderPickingTransaction> partialUpdate(OrderPickingTransaction orderPickingTransaction) {
        log.debug("Request to partially update OrderPickingTransaction : {}", orderPickingTransaction);

        return orderPickingTransactionRepository
            .findById(orderPickingTransaction.getId())
            .map(
                existingOrderPickingTransaction -> {
                    if (orderPickingTransaction.getReferenceId() != null) {
                        existingOrderPickingTransaction.setReferenceId(orderPickingTransaction.getReferenceId());
                    }
                    if (orderPickingTransaction.getAddress() != null) {
                        existingOrderPickingTransaction.setAddress(orderPickingTransaction.getAddress());
                    }
                    if (orderPickingTransaction.getStatus() != null) {
                        existingOrderPickingTransaction.setStatus(orderPickingTransaction.getStatus());
                    }
                    if (orderPickingTransaction.getTransactionAmount() != null) {
                        existingOrderPickingTransaction.setTransactionAmount(orderPickingTransaction.getTransactionAmount());
                    }

                    return existingOrderPickingTransaction;
                }
            )
            .map(orderPickingTransactionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderPickingTransaction> findAll(Pageable pageable) {
        log.debug("Request to get all OrderPickingTransactions");
        return orderPickingTransactionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderPickingTransaction> findOne(Long id) {
        log.debug("Request to get OrderPickingTransaction : {}", id);
        return orderPickingTransactionRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<OrderPickingTransaction> findByTransactionTypeAndDescriptionAndStockCodeAndAurTmpDetailId(TransactionType transactionType,String description,String stockCode,Long aurSayimTanimId){
        log.debug("Rest to get unique transaction");
        return orderPickingTransactionRepository.findByTransactionTypeAndDescriptionAndStockCodeAndReferenceId(transactionType,description,stockCode,aurSayimTanimId);
    }

    @Transactional(readOnly = true)
    public List<OrderPickingTransaction> findByAurTmpDetailIdAndTransactionType(Long id,TransactionType transactionType) {
        log.debug("Request to get OrderPickingTransaction : {}", id);
        return orderPickingTransactionRepository.findByReferenceIdAndTransactionTypeAndStatus(id,transactionType,true);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderPickingTransaction : {}", id);
        orderPickingTransactionRepository.deleteById(id);
    }

    @Override
    public void saveByAurSayimUrun(Long countingDefinitionId,Long addressId, Double transactionAmount, String stockCode){
        log.debug("Request to saveByAurSayimUrun : {} {} {} {}",countingDefinitionId,addressId,transactionAmount,stockCode);
        OrderPickingTransaction opt = new OrderPickingTransaction();
        opt.setReferenceId(countingDefinitionId);
        opt.setAddress(new AurDepoUrunAdres(addressId));
        opt.setStatus(true);
        opt.setTransactionAmount(transactionAmount);
        opt.setStockCode(stockCode);
        opt.setTransactionType(TransactionType.COUNTING);

        orderPickingTransactionRepository.save(opt);

    }

    @Override
    public void saveByAurTmpDetailDto(AurOrderDetail aurOrderDetail, Long addressId , Double transactionAmount){
        log.debug("Request to save order amount transaction by aurTmpDetail,addressId and transactionAmount : {} {} {}", aurOrderDetail,addressId,transactionAmount);
        OrderPickingTransaction opt = new OrderPickingTransaction();
        opt.setReferenceId(aurOrderDetail.getId());
        opt.setAddress(new AurDepoUrunAdres(addressId));
        opt.setStatus(true);
        opt.setTransactionAmount(transactionAmount);
        opt.setStockCode(aurOrderDetail.getStokKodu());
        opt.setTransactionType(TransactionType.PICKING);
        opt.setBarcode(aurOrderDetail.getBarkod());

        orderPickingTransactionRepository.save(opt);
    }

    @Override
    public void saveByAurTmpDetailDto(AurOrderDetail aurOrderDetail, Long addressId , Double transactionAmount, String barcode){
        log.debug("Request to save order amount transaction by aurTmpDetail,addressId,transactionAmount and barcode : {} {} {} {}", aurOrderDetail,addressId,transactionAmount,barcode);
        OrderPickingTransaction opt = new OrderPickingTransaction();
        opt.setReferenceId(aurOrderDetail.getId());
        opt.setAddress(new AurDepoUrunAdres(addressId));
        opt.setStatus(true);
        opt.setTransactionAmount(transactionAmount);
        opt.setStockCode(aurOrderDetail.getStokKodu());
        opt.setTransactionType(TransactionType.PICKING);
        opt.setBarcode(barcode);

        orderPickingTransactionRepository.save(opt);
    }

    @Override
    public List<TransactionResponseByDocNoDto> getOrderTransactions(List<AurOrderDetail> tmpDetails, Boolean assignedOrder){
        List<TransactionResponseByDocNoDto> responseDto = new ArrayList<>();

        List<String> distinctBarcodes = tmpDetails.stream()
            .filter(q -> !q.getStatus().equals("SUSPENDED"))
            .map(AurOrderDetail::getBarkod)
            .distinct()
            .collect(Collectors.toList());

        distinctBarcodes.forEach(barcode -> {
            List<AurOrderDetail> instantProduct = tmpDetails.stream().filter(q -> q.getBarkod().equals(barcode)).collect(Collectors.toList());
            responseDto.add(getOrderTransactionItem(instantProduct.get(0),assignedOrder));
        });

        return responseDto;
    }


    public void revertOrderTransaction(AurOrderDetail aurOrderDetail, Double previousAmount, Long addressId){
        log.debug("Request to revertOrderTransaction order : {} previousAmount : {} addressId: {}", aurOrderDetail,previousAmount,addressId);
        OrderPickingTransaction orderPickingTransaction = new OrderPickingTransaction();
        orderPickingTransaction.setReferenceId(aurOrderDetail.getId());
        orderPickingTransaction.setAddress(new AurDepoUrunAdres(addressId));
        orderPickingTransaction.setStatus(true);
        orderPickingTransaction.setTransactionAmount(previousAmount);
        orderPickingTransaction.setStockCode(aurOrderDetail.getStokKodu());
        orderPickingTransaction.setTransactionType(TransactionType.BACKWARD_PICKING);

        orderPickingTransactionRepository.save(orderPickingTransaction);
    }

    public TransactionResponseByDocNoDto getOrderTransactionItem(AurOrderDetail aurOrderDetail, Boolean assignedOrder){
        TransactionResponseByDocNoDto transactionItem = new TransactionResponseByDocNoDto();
        List<AddressTransactionsDetailDto> addressTransactionsDetailDtos = new ArrayList<>();
        transactionItem.setBarcode(aurOrderDetail.getBarkod());
        transactionItem.setStockCode(aurOrderDetail.getStokKodu());
        transactionItem.setStockName(aurOrderDetail.getStokAdi());
        transactionItem.setOrderAmount(aurOrderDetail.getSiparisMiktar());
        transactionItem.setProcessAmount(aurOrderDetail.getTeslimMiktar());
        transactionItem.setObserverAmount(aurOrderDetail.getObserverAmount());


        orderPickingTransactionRepository.findByReferenceIdAndTransactionTypeAndStatus(aurOrderDetail.getId(),TransactionType.PICKING,true)
            .forEach(opt -> {
                AddressTransactionsDetailDto optDto = new AddressTransactionsDetailDto();
                if(opt.getTransactionAmount() != 0){
                    optDto.setAddress(opt.getAddress() != null ? opt.getAddress().getAdres() : "");
                    optDto.setTransactionAmount(opt.getTransactionAmount());
                    optDto.setProcessUser(opt.getCreatedBy());
                    optDto.setProcessDate(String.valueOf(opt.getCreatedDate()));
                    optDto.setTransactionId(opt.getId());
                    optDto.setStatus(opt.getStatus());
                    optDto.setTransactionType(opt.getTransactionType());

                    addressTransactionsDetailDtos.add(optDto);
                }

            });

        transactionItem.setTransactionsDetailList(addressTransactionsDetailDtos);
        return transactionItem;
    }

    public void saveOrderCombineTransactions(Long masterId,Long oldMasterId){
        OrderPickingTransaction opt = new OrderPickingTransaction();

        opt.setReferenceId(masterId);
        opt.setAddress(new AurDepoUrunAdres(oldMasterId));
        opt.setStatus(true);
        opt.transactionAmount(0.0);
        opt.setTransactionType(TransactionType.ORDER_COMBINING);

        orderPickingTransactionRepository.save(opt);

    }

    @Override
    public void saveByInterWarehouseTransferTransactions(DepolarArasiTransferDto dto,String erpDocumentNo){
        OrderPickingTransaction opt = new OrderPickingTransaction();
        opt.setReferenceId((long) dto.getGirisDepo());
        opt.setAddress(new AurDepoUrunAdres(dto.getUrunAdresId()));
        opt.setStatus(true);
        opt.setTransactionAmount(dto.getMiktar());
        opt.setStockCode(dto.getBarcode());
        opt.setDescription(dto.getDescription().concat("-").concat("Erp_document_no : " + erpDocumentNo));
        opt.setTransactionType(TransactionType.INTER_WAREHOUSE_TRANSFER);
        orderPickingTransactionRepository.save(opt);

    }

    public void saveProductAddressDefinitionTransaction(Long urunAdresId,Double amount,String stockCode){
        log.debug("Request to saveProductAddressDefinitionTransaction : {}",stockCode);

    }

    @Override
    public void saveByPalletBarcodeCounting(Long countingDefinitionId,String palletBarcode,String description){
        OrderPickingTransaction opt = new OrderPickingTransaction();
        opt.setReferenceId(countingDefinitionId);
        opt.setAddress(null);
        opt.setStatus(true);
        opt.setTransactionAmount(0.0);
        opt.setStockCode(palletBarcode);
        opt.setDescription(description);
        opt.setTransactionType(TransactionType.PALLET_BARCODE_COUNTING);
        orderPickingTransactionRepository.save(opt);
    }


}
