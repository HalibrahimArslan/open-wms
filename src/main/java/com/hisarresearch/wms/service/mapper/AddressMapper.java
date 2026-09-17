package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.address.AurDepoUrunAdres;
import com.hisarresearch.wms.service.dto.address.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface AddressMapper extends EntityMapper<AddressDTO, AurDepoUrunAdres> {
    @Mapping(target = "id", source = "urunAdresId")
    @Mapping(target = "address", source = "adres")
    @Mapping(target = "warehouseCode", source = "depoNo")
    @Mapping(target = "addressType", source = "adresTipi")
    AddressDTO toDto(AurDepoUrunAdres urunAdres);

    @Mapping(target = "urunAdresId", source = "id")
    @Mapping(target = "adres", source = "address")
    @Mapping(target = "depoNo", source = "warehouseCode")
    @Mapping(target = "adresTipi", source = "addressType")
    AurDepoUrunAdres toEntity(AddressDTO addressDTO);
}
