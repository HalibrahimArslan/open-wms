package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdresStok;
import com.hisarresearch.wms.service.dto.address.ProductAddressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {})
public interface ProductAddressMapper extends EntityMapper<ProductAddressDTO, AurDepoUrunAdresStok> {

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "barcode", source = "barcode")
    @Mapping(target = "miktar", source = "miktar")
    ProductAddressDTO toDtoId(AurDepoUrunAdresStok aurDepoUrunAdresStok);
}
