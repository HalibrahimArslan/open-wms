package com.hisarresearch.wms.domain.enumeration;
public enum ProductCategoryTypeEnum {
    FURNITURE(10,11,12,13,14,15,16),
    KINDERGARTEN(17,31,33),
    DECORATION(19,39),
    INDUSTRIAL(20,48),
    MECHANIC(41),
    ELECTRIC(45),
    CONSTRUCTION(35,36),
    CARPET( 32,34);

    private int[] value;


    ProductCategoryTypeEnum(int... value){
        this.value = value;
    }
    public int[] getValue() {
        return value;
    }



}
