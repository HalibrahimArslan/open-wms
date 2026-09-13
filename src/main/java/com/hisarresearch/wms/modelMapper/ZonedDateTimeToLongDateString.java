package com.hisarresearch.wms.modelMapper;

import com.hisarresearch.wms.constants.AurConstants;
import java.time.ZonedDateTime;
import org.modelmapper.AbstractConverter;

public class ZonedDateTimeToLongDateString extends AbstractConverter<ZonedDateTime, String> {

    // ZonedDateTime olarak gelen bir tarihi uzun tarih olarak string'e çevirir. Genel bir metottur. Ornek result: "201705281430"
    // dto'larda tarih alanları genelde string olarak tutulur.
    @Override
    protected String convert(ZonedDateTime source) {
        String tarih = null;

        if (source == null) {
            return null;
        }

        tarih = AurConstants.ZONED_DATETIME_FORMATTER.format(source);

        return tarih;
    }
}
