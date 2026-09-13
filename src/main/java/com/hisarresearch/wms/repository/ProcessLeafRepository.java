package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.ProcessLeaf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessLeafRepository extends JpaRepository<ProcessLeaf,Long> {
    List<ProcessLeaf> findByProcess_IdAndStatus(Long processId, Boolean status);
    Optional<ProcessLeaf> findByProcess_IdAndBarcodeAndStatus(Long processId,String barcode,Boolean status);

}
