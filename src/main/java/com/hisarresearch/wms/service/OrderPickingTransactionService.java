package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.OrderPickingTransaction;

import java.util.List;
import java.util.Optional;

import com.hisarresearch.wms.domain.enumeration.TransactionType;
import com.hisarresearch.wms.service.dto.DepolarArasiTransferDto;
import com.hisarresearch.wms.service.dto.TransactionResponseByDocNoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link OrderPickingTransaction}.
 */
public interface OrderPickingTransactionService {
    /**
     * Save a orderPickingTransaction.
     *
     * @param orderPickingTransaction the entity to save.
     * @return the persisted entity.
     */
    OrderPickingTransaction save(OrderPickingTransaction orderPickingTransaction);

    /**
     * Partially updates a orderPickingTransaction.
     *
     * @param orderPickingTransaction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderPickingTransaction> partialUpdate(OrderPickingTransaction orderPickingTransaction);

    /**
     * Get all the orderPickingTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderPickingTransaction> findAll(Pageable pageable);

    /**
     * Get the "id" orderPickingTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderPickingTransaction> findOne(Long id);


    List<OrderPickingTransaction> findByAurTmpDetailIdAndTransactionType(Long aurTmpDetailId, TransactionType transactionType);



    Optional<OrderPickingTransaction> findByTransactionTypeAndDescriptionAndStockCodeAndAurTmpDetailId(TransactionType transactionType,String description,String stockCode, Long aurTmpDetailId);

    /**
     * Delete the "id" orderPickingTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Save the transaction of Counting Transaction entity wrt transaction_type is counting
     *
     */
    void saveByAurSayimUrun(Long countingDefinitionId,Long addressId, Double transactionAmount, String stockCode);
    void saveByAurTmpDetailDto(AurOrderDetail aurOrderDetail, Long addressId, Double transactionAmount);
    void saveByAurTmpDetailDto(AurOrderDetail aurOrderDetail, Long addressId, Double transactionAmount, String barcode);

    List<TransactionResponseByDocNoDto> getOrderTransactions(List<AurOrderDetail> aurOrderDetails, Boolean assignOrder);

    void saveByInterWarehouseTransferTransactions(DepolarArasiTransferDto dto,String erpDocumentNo);

    void saveByPalletBarcodeCounting(Long countingDefinitionId,String palletBarcode,String description);


}
