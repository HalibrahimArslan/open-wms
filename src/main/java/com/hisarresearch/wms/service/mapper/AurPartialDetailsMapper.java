package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.service.dto.AurPartialDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AurPartialDetails} and its DTO {@link AurPartialDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { AurPartialItemMapper.class })
public interface AurPartialDetailsMapper extends EntityMapper<AurPartialDetailsDTO, AurPartialDetails> {
    @Mapping(target = "aurPartialItem", source = "aurPartialItem", qualifiedByName = "id")
    AurPartialDetailsDTO toDto(AurPartialDetails s);
}
