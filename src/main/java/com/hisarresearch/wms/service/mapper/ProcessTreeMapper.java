package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.ProcessTree;
import com.hisarresearch.wms.service.dto.process.ProcessTreeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProcessLeafMapper.class})
public interface ProcessTreeMapper extends EntityMapper<ProcessTreeDto,ProcessTree> {
}
