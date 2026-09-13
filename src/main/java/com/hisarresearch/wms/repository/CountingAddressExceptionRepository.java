package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.CountingAddressException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountingAddressExceptionRepository extends JpaRepository<CountingAddressException,Long>, JpaSpecificationExecutor<CountingAddressException> {
    Optional<CountingAddressException> findByAddress_UrunAdresIdAndCountingDefinition_IdAndStatusTrue(Long addressId, Long countingDefinitionId);
    List<CountingAddressException> findByCountingDefinition_IdAndStatus(Long countingDefinitionId, boolean status);
}
