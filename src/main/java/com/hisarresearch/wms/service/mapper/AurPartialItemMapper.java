package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.service.dto.AurPartialItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AurPartialItem} and its DTO {@link AurPartialItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AurPartialItemMapper extends EntityMapper<AurPartialItemDTO, AurPartialItem> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "packageCode", source = "packageCode")
    @Mapping(target = "packageName", source = "packageName")
    @Mapping(target = "packageBarcode", source = "packageBarcode")
    @Mapping(target = "status",source = "status")
    AurPartialItemDTO toDto(AurPartialItem aurPartialItem);
}
