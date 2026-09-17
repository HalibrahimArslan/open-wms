package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.ProductCountingTypePairing;
import com.hisarresearch.wms.service.dto.ProductCountingTypePairingDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductWithoutAddressMapper.class})
public interface ProductCountingTypePairingMapper extends EntityMapper<ProductCountingTypePairingDTO, ProductCountingTypePairing> {
}
