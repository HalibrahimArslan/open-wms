package com.hisarresearch.wms.repository;

import com.hisarresearch.wms.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {
    List<Upload> findByCompanyCode(String companyCode);

    boolean existsByUrlEndingWithAndCompanyCode(String urlSuffix, String companyCode);
}
