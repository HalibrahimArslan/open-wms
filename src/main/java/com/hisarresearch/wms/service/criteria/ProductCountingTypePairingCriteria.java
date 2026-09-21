package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.domain.enumeration.CountingType;
import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.Filter;
import com.hisarresearch.wms.framework.service.filter.IntegerFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;

public class ProductCountingTypePairingCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;
    /**
     * Class for filtering CountingType
     */
    public static class CountingTypeFilter extends Filter<CountingType> {

        public CountingTypeFilter() {}

        public CountingTypeFilter(CountingTypeFilter filter) {
            super(filter);
        }

        @Override
        public CountingTypeFilter copy() {
            return new CountingTypeFilter(this);
        }
    }

    private IntegerFilter warehouseCode;

    private StringFilter barcode;

    private StringFilter companyCode;

    private CountingTypeFilter countingType;

    public ProductCountingTypePairingCriteria() {
    }

    public ProductCountingTypePairingCriteria(ProductCountingTypePairingCriteria other) {
        this.warehouseCode = other.warehouseCode == null ? null : other.warehouseCode.copy();
        this.countingType = other.countingType == null ? null : other.countingType.copy();
        this.barcode = other.barcode == null ? null : other.barcode.copy();
        this.companyCode = other.companyCode == null ? null : other.companyCode.copy();
    }

    @Override
    public ProductCountingTypePairingCriteria copy() {
        return new ProductCountingTypePairingCriteria(this);
    }

    public IntegerFilter getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(IntegerFilter warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public CountingTypeFilter getCountingType() {
        return countingType;
    }

    public void setCountingType(CountingTypeFilter countingType) {
        this.countingType = countingType;
    }

    public StringFilter getBarcode() {
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
    }

    public StringFilter getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(StringFilter companyCode) {
        this.companyCode = companyCode;
    }
}
