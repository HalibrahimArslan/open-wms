package com.hisarresearch.wms.modelMapper;

import com.hisarresearch.wms.constants.AurConstants;
import java.util.Date;
import org.modelmapper.AbstractConverter;

public class DateToLongDateString extends AbstractConverter<Date, String> {

    // Date olarak gelen bir tarihi uzun tarih olarak string'e çevirir. Genel bir metottur. Ornek result: "201705281430"
    // dto'larda tarih alanları genelde string olarak tutulur.
    @Override
    protected String convert(Date source) {
        String tarih = null;

        if (source == null) {
            return null;
        }

        tarih = AurConstants.DATETIME_FORMATTER.format(source);

        return tarih;
    }
}
