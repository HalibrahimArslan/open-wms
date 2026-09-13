package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurOrderDetailSkt;
import com.hisarresearch.wms.service.dto.AurOrderDetailSktDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {})
public interface AurOrderDetailSktMapper extends EntityMapper<AurOrderDetailSktDTO, AurOrderDetailSkt> {

    @Override
    default List<AurOrderDetailSkt> toEntity(List<AurOrderDetailSktDTO> dtoList) {
        if (dtoList == null) return List.of();
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
