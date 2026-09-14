package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.OrderPickingTransaction;
import com.hisarresearch.wms.domain.enumeration.TransactionType;
import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceDTO;
import com.hisarresearch.wms.service.dto.userPerformance.UserPerformanceDetailDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the OrderPickingTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderPickingTransactionRepository
    extends JpaRepository<OrderPickingTransaction, Long>, JpaSpecificationExecutor<OrderPickingTransaction> {
    Optional<OrderPickingTransaction> findByReferenceIdAndAddress_UrunAdresIdAndStockCodeAndTransactionType(Long referenceId, Long addressId,String stockCode, TransactionType transactionType);
    List<OrderPickingTransaction> findByReferenceIdAndTransactionType(Long referenceId,TransactionType transactionType);
    List<OrderPickingTransaction> findByReferenceIdAndTransactionTypeAndStatus(Long referenceId,TransactionType transactionType,Boolean status);

    Optional<OrderPickingTransaction> findByTransactionTypeAndDescriptionAndStockCodeAndReferenceId(TransactionType transactionType, String description, String stockCode,Long referenceId);


    @Query(
        value = "SELECT " +
            " ROW_NUMBER() OVER (ORDER BY  MAX(opt.last_modified_date) - MIN(opt.created_date) DESC) AS id, " +
            " aom.created_by AS kullanici, " +
            " TO_CHAR(MAX(opt.last_modified_date) - MIN(opt.created_date),'DD \"gün\" HH24:MI') as toplamSure ," +
            " COUNT(atd.id) AS toplamAdet, " +
            " TO_CHAR((MAX(opt.last_modified_date) - MIN(opt.created_date)) / COUNT(atd.id), 'HH24:MI:SS') AS ortalamaSure " +
            "FROM order_picking_transaction opt " +
            "JOIN aur_order_detail atd ON opt.reference_id = atd.id " +
            "JOIN aur_order_master aom ON atd.aur_order_id = aom.id " +
            "JOIN aur_user au on aom.aur_user_id = au.id " +
            "WHERE (:startDate IS NULL OR aom.created_date >= CAST(:startDate AS timestamp)) " +
            "  AND (:endDate IS NULL OR aom.last_modified_date <= CAST(:endDate AS timestamp)) " +
            "  AND (COALESCE(:createdBy, '') = '' OR aom.created_by = :createdBy) " +
            "  AND (:companyCode IS NULL OR :companyCode = 0 OR au.company_code = :companyCode) " +
            "GROUP BY aom.created_by " +
            "ORDER BY toplamSure DESC",
        nativeQuery = true
    )
    List<UserPerformanceDTO> getUserPerformance(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("createdBy") String createdBy,
        @Param("companyCode") Integer companyCode
    );

    @Query(value =
        "SELECT " +
            " ROW_NUMBER() OVER (ORDER BY MIN(opt.created_date) DESC) AS id, " +
            " aom.order_info AS siparisNo, " +
            " TO_CHAR(MIN(opt.created_date), 'YYYY-MM-DD HH24:MI:SS') AS baslangic, " +
            " TO_CHAR(MAX(opt.last_modified_date), 'YYYY-MM-DD HH24:MI:SS') AS bitis, " +
            " TO_CHAR(MAX(opt.last_modified_date) - MIN(opt.created_date),'HH24:MI:SS') as sure , " +
            " COUNT(atd.id) AS adet, " +
            " aom.created_by AS kullanici " +
            "FROM order_picking_transaction opt " +
            "JOIN aur_order_detail atd ON opt.reference_id = atd.id " +
            "JOIN aur_order_master aom ON atd.aur_order_id = aom.id " +
            "JOIN aur_user au on aom.aur_user_id = au.id " +
            "WHERE (:startDate IS NULL OR aom.created_date >= CAST(:startDate AS timestamp)) " +
            "  AND (:endDate IS NULL OR aom.last_modified_date <= CAST(:endDate AS timestamp)) " +
            "  AND (COALESCE(:createdBy, '') = '' OR aom.created_by = :createdBy) " +
            "  AND (:companyCode IS NULL OR :companyCode = 0 OR au.company_code = :companyCode) " +
            "GROUP BY aom.order_info, aom.created_by " +
            "ORDER BY baslangic DESC",
        nativeQuery = true)
    List<UserPerformanceDetailDTO> getUserPerformanceDetail(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("createdBy") String createdBy,
        @Param("companyCode") Integer companyCode);
}
