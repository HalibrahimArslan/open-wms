package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.sevkplani.AurTmpSevkPlani;
import com.hisarresearch.wms.service.dto.AurTmpSevkPlaniDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {})
public interface AurTmpSevkPlaniMapper extends EntityMapper<AurTmpSevkPlaniDTO, AurTmpSevkPlani> {
    AurTmpSevkPlani toEntity(AurTmpSevkPlaniDTO dto);

    // Mapping: Entityden DTO ya
    AurTmpSevkPlaniDTO toDto(AurTmpSevkPlani entity);
}
