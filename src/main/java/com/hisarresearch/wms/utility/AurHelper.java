package com.hisarresearch.wms.utility;

import com.hisarresearch.wms.modelMapper.DateToLongDateString;
import com.hisarresearch.wms.modelMapper.ZonedDateTimeToLongDateString;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


public class AurHelper {

    public static String getDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    public static <T, T1> List<T> convertDomainListToDto(List<T1> domainList, Class<T> dtoType) {
        List<T> resultList = new ArrayList<T>();

        // ModelMapper objesi, convert yapmayı sağlayan objedir.
        ModelMapper modelMapper = new ModelMapper();

        // iki obje property'leri arasında nasıl bir stratejiyle (algoritmayla) map'leme yapılacağı bilgisi seçilir. 3 farklı strategy var.
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.addConverter(new DateToLongDateString()); // ***Tarih alanları otomatik olarak modelMapper sayesinde "DateToLongDateString" converter ile  Date'den string'e çevrilir.
        modelMapper.addConverter(new ZonedDateTimeToLongDateString());

        T dto;

        for (Object domain : domainList) {
            dto = (T) modelMapper.map(domain, dtoType);
            resultList.add(dto);
        }

        // *****************************************************************************************************************************
        // ***Tarih alanları otomatik olarak modelMapper sayesinde "DateToLongDateString" converter ile Date'den string'e çevrilir.
        // *****************************************************************************************************************************

        return resultList;
    }

    /**
     * Girilen dto liste objesini domain liste objesine çevirir.
     * @param dtoList
     * @param domainType
     * @return
     */
    public static <T, T1> List<T> convertDtoListToDomain(List<T1> dtoList, Class<T> domainType) {

        List<T> resultList = new ArrayList<T>();

        // ModelMapper objesi, convert yapmayı sağlayan objedir.
        ModelMapper modelMapper = new ModelMapper();
        // iki obje property'leri arasında nasıl bir stratejiyle (algoritmayla) map'leme yapılacağı bilgisi seçilir. 3 farklı strategy var.
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);


        T domain;

        for (Object dto : dtoList) {
            // Domain'i dto objesine convert edelim. Hem de tek satırda.
            domain = (T) modelMapper.map(dto, domainType);
            resultList.add(domain);
        }

        // *****************************************************************************************************************************
        // ***Tarih alanları otomatik olarak modelMapper sayesinde "StringToDateMap" converter ile string'den Date'e çevrilir.
        // *****************************************************************************************************************************

        return resultList;
    }

    public  <T> Predicate<T> distinctByKey(Function<T, Object> function) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(function.apply(t));
    }

    public static String getDateForMicro(){
        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        String[] splittedTime = currentZonedDateTime.toString().split("T");
        return splittedTime[0].concat("T00:00:00.000Z");
    }

    public static String addSpecificCharToAnyIndex(String originalString,int index){
        String editedValue = "";
        if (index >= 0 && index <= originalString.length()) {
            editedValue = originalString.substring(0, index) +
                ' ' +
                originalString.substring(index);

        }
        return editedValue;
    }

    public static Double roundAmount(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static DayOfWeek dayFinder() {
        LocalDate currentdate = LocalDate.now();
        return  currentdate.getDayOfWeek();
    }

    public boolean containsAnyCharacter(String str, char[] characters) {
        if (str == null || characters == null) {
            return false;
        }

        for (char character : characters) {
            if (str.contains(Character.toString(character))) {
                return true;
            }
        }
        return false;
    }


}
