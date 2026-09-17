package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.service.dto.address.AurDepoUrunAdresStokDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {  })
public interface AurDepoUrunAdresStokMapper extends EntityMapper<AurDepoUrunAdresStokDto, AurDepoUrunAdresStok> {
}
