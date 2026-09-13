package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.AurLookupTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AurLookupTableRepository extends JpaRepository<AurLookupTable, Long> {
    List<AurLookupTable> findByLookupName(String lookupName);
    List<AurLookupTable> findByLookupCode(String lookupCode);
    List<AurLookupTable> findByLookupNameIn(List<String> lookupNames);

}
