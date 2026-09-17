package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.service.dto.UserDepoRelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDepoRel} and its DTO {@link UserDepoRelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserDepoRelMapper extends EntityMapper<UserDepoRelDTO, UserDepoRel> {


}
