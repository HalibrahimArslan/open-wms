package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurCompany;
import com.hisarresearch.wms.service.dto.AurCompanyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<AurCompanyDTO, AurCompany>{
}
