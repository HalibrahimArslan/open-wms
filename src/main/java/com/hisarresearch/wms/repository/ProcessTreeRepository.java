package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.ProcessTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessTreeRepository extends JpaRepository<ProcessTree, Long>, JpaSpecificationExecutor<ProcessTree> {
    Optional<ProcessTree> findByProcessChildIdAndStatusAndDepoCodeAndCompanyCode(Long processChildId, Boolean status, Long depoCode, Long companyCode);
}
