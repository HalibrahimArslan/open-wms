package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurSayimUrun;
import com.hisarresearch.wms.service.dto.counting.CountingDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {ProductWithoutAddressMapper.class})
public interface CountingDetailMapper extends EntityMapper<CountingDetailDTO, AurSayimUrun>{

    @Mapping(target = "aurSayimTanim.id",source = "countingDefinitionId")
    AurSayimUrun toEntity(CountingDetailDTO countingDetailDTO);

    @Mapping(target = "countingDefinitionId", source = "aurSayimTanim.id")
    CountingDetailDTO toDto(AurSayimUrun aurSayimUrun);
}
