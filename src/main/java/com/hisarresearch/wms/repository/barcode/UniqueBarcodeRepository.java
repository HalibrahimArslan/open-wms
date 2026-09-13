package com.hisarresearch.wms.repository.barcode;

import com.hisarresearch.wms.domain.barcode.UniqueBarcode;
import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UniqueBarcodeRepository extends JpaRepository<UniqueBarcode, Long>, JpaSpecificationExecutor<UniqueBarcode> {

    Optional<UniqueBarcode> findByBarcode(String barcode);

    @Query(" select coalesce(max(tb.lotNumber), 0) from UniqueBarcode tb where tb.partiCode = :partiCode ")
    Long findMaxLotNumberByPartiCode(@Param("partiCode") String partiCode);

    @Query(value = "SELECT 1 FROM (SELECT pg_advisory_xact_lock(hashtext(:partiCode))) AS t", nativeQuery = true)
    Integer lockByPartiCode(@Param("partiCode") String partiCode);

    List<UniqueBarcode> findAllByPartiCode(String partiCode);

    List<UniqueBarcode> findAllByPartiCodeAndStatusOrderByLotNumberAsc(String partiCode, UniqueBarcodeState status);

    List<UniqueBarcode> findAllByAurOrderDetail_IdAndStatus(Long aurOrderDetailId, UniqueBarcodeState status);

    @Query("SELECT COALESCE(SUM(ub.quantity), 0) FROM UniqueBarcode ub " +
        "WHERE ub.product.id.barkod = :erpBarkod AND ub.product.id.companyCode = :companyCode " +
        "AND ub.address.id = :addressId AND ub.status = :status")
    BigDecimal sumQuantityByProductAndAddressAndStatus(
        @Param("erpBarkod") String erpBarkod,
        @Param("companyCode") String companyCode,
        @Param("addressId") Long addressId,
        @Param("status") UniqueBarcodeState status);
}
