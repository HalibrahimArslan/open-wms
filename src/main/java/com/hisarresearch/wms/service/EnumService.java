package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.enumeration.AddressMovementType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnumService {
    public List<AddressMovementType> getAddressMovementTypes(){
        return List.of(AddressMovementType.values());
    }
}
