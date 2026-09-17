package com.hisarresearch.wms.service.mapper;

import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.service.dto.AurOrderMasterDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AurOrderMasterMapper {

    @Mapping(source = "cariBaglantiTipi", target = "baglantiTipi")
    @Mapping(source = "sevkAddressId", target = "customerAddress.addressId")
    @Mapping(source = "firmCode",target = "customerAddress.cariCode")
    @Mapping(source = "sevkAddress",target = "customerAddress.sevkAddress")
    @Mapping(source = "sevkTel",target = "customerAddress.sevkTel")
    @Mapping(source = "sevkMuhatap",target = "customerAddress.sevkMuhatap")
    @Mapping(source = "sevkAcikAdres",target = "customerAddress.sevkAcikAdress")
    @Mapping(source = "aurUserId",target = "aurUser.id")
    AurOrderMaster dtoToMaster(AurOrderMasterDTO dto);

    @Mapping(source = "baglantiTipi", target = "cariBaglantiTipi")
    @Mapping(source = "customerAddress.addressId", target = "sevkAddressId")
    @Mapping(source = "customerAddress.cariCode",target = "firmCode")
    @Mapping(source = "customerAddress.sevkAddress",target = "sevkAddress")
    @Mapping(source = "customerAddress.sevkTel",target = "sevkTel")
    @Mapping(source = "customerAddress.sevkMuhatap",target = "sevkMuhatap")
    @Mapping(source = "customerAddress.sevkAcikAdress",target = "sevkAcikAdres")
    @Mapping(source = "aurUser.id",target = "aurUserId")
    AurOrderMasterDTO masterToDTO(AurOrderMaster master);

    @AfterMapping
    default void nullifyCustomerAddressIfKeysMissing(
        @MappingTarget AurOrderMaster target,
        AurOrderMasterDTO source
    ) {
        if (source.getSevkAddressId() == null || source.getFirmCode() == null) {
            target.setCustomerAddress(null);
        }
    }



}
