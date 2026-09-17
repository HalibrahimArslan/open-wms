package com.hisarresearch.wms.constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class AurConstants {

    public static final DateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyyMMddHHmm");

    public static final DateTimeFormatter ZONED_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

}
