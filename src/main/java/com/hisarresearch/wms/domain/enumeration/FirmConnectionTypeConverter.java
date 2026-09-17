package com.hisarresearch.wms.domain.enumeration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FirmConnectionTypeConverter implements AttributeConverter<FirmConnectionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FirmConnectionType attribute) {
        return attribute != null ? Integer.parseInt(attribute.getValue()) : null;
    }

    @Override
    public FirmConnectionType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? FirmConnectionType.fromValue(dbData) : null;
    }
}

