package com.hisarresearch.wms.domain;

import com.hisarresearch.wms.domain.enumeration.FeedbackStatus;
import com.hisarresearch.wms.domain.enumeration.FeedbackTitle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aur_driver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AurDriver extends AbstractAuditingEntityWithoutJsonIgnore implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aurDriverGenerator")
    @SequenceGenerator(name = "aurDriverGenerator", sequenceName = "aur_driver_seq", allocationSize = 1)
    private Long id;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "trailer_plate")
    private String trailerPlate;

    @Column(name = "op_type")
    private String opType;

    @Column(name = "identity_number")
    private String identityNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getTrailerPlate() {
        return trailerPlate;
    }

    public void setTrailerPlate(String trailerPlate) {
        this.trailerPlate = trailerPlate;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }
}
