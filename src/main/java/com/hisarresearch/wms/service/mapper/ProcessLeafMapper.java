package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.ProcessLeaf;
import com.hisarresearch.wms.service.dto.process.ProcessLeafDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ProcessLeafMapper extends EntityMapper<ProcessLeafDto, ProcessLeaf> {
}
