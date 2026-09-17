package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurCompany;
import com.hisarresearch.wms.repository.AurCompanyRepository;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import com.hisarresearch.wms.service.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
public class CompanyService {
    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private static final String ENTITY_NAME = "company";

    private final AurCompanyRepository aurCompanyRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(AurCompanyRepository aurCompanyRepository,CompanyMapper companyMapper) {
        this.aurCompanyRepository = aurCompanyRepository;
        this.companyMapper = companyMapper;
    }

    public AurCompanyDTO findByCompanyCode(String companyCode) {
        AurCompany company = aurCompanyRepository.findByCompanyCode(Integer.valueOf(companyCode));
        if(company == null){
            throw new EntityNotFoundException("AurCompany with code " + companyCode + " not found");
        }
        return companyMapper.toDto(company);
    }
}
