package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.Tag;
import com.hisarresearch.wms.service.dto.tag.TagDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
}
