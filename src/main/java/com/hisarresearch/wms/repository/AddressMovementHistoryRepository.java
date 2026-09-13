package com.hisarresearch.wms.repository;


import com.hisarresearch.wms.domain.AddressMovementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMovementHistoryRepository extends JpaRepository<AddressMovementHistory,Long>, JpaSpecificationExecutor<AddressMovementHistory> {
}
