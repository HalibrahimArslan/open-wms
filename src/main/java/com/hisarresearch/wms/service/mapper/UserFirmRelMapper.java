package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.*;
import com.hisarresearch.wms.service.dto.UserFirmRelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserFirmRel} and its DTO {@link UserFirmRelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserFirmRelMapper extends EntityMapper<UserFirmRelDTO, UserFirmRel> {}
