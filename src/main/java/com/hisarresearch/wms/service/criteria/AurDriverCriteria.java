package com.hisarresearch.wms.service.criteria;

import com.hisarresearch.wms.framework.service.Criteria;
import com.hisarresearch.wms.framework.service.filter.InstantFilter;
import com.hisarresearch.wms.framework.service.filter.LongFilter;
import com.hisarresearch.wms.framework.service.filter.StringFilter;

import java.io.Serializable;

public class AurDriverCriteria implements Serializable, Criteria {
    private LongFilter id;
    private StringFilter driverName;
    private StringFilter identityNumber;
    private StringFilter phoneNumber;
    private StringFilter licensePlate;
    private StringFilter trailerPlate;
    private InstantFilter lastModifiedDate;
    private StringFilter query;
    @Override
    public AurDriverCriteria copy() {
        return new AurDriverCriteria(this);
    }

    public AurDriverCriteria() {}

    public AurDriverCriteria(AurDriverCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.driverName = other.driverName == null ? null : other.driverName.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.licensePlate = other.licensePlate == null ? null : other.licensePlate.copy();
        this.trailerPlate = other.trailerPlate == null ? null : other.trailerPlate.copy();
        this.query = other.query == null ? null : other.query.copy();
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDriverName() {
        return driverName;
    }

    public void setDriverName(StringFilter driverName) {
        this.driverName = driverName;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StringFilter getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(StringFilter identityNumber) {
        this.identityNumber = identityNumber;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(StringFilter licensePlate) {
        this.licensePlate = licensePlate;
    }

    public StringFilter getTrailerPlate() {
        return trailerPlate;
    }

    public void setTrailerPlate(StringFilter trailerPlate) {
        this.trailerPlate = trailerPlate;
    }

    public StringFilter getQuery() {
        return query;
    }

    public void setQuery(StringFilter query) {
        this.query = query;
    }
}
