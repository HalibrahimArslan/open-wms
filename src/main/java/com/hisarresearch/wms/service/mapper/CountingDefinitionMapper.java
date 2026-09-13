package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurSayimTanim;
import com.hisarresearch.wms.service.dto.counting.CountingDefinitionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {})
public interface CountingDefinitionMapper extends EntityMapper<CountingDefinitionDTO, AurSayimTanim> {
}
