package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurDriver;
import com.hisarresearch.wms.service.dto.AurDriverDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AurDriverMapper extends EntityMapper<AurDriverDTO, AurDriver>{
}
