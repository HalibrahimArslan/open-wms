package com.hisarresearch.wms.service.dto;

import com.hisarresearch.wms.config.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AurDriverDTO implements Serializable {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String driverName;

    @Pattern(regexp = Constants.PHONE_REGEX, message = "Telefon numarası geçersiz! Lütfen 05 ile başlayan 11 haneli bir numara giriniz. 05112223344")
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 50)
    private String licensePlate;


    private String trailerPlate;


    private String opType;

    @NotBlank(message = "Kimlik numarası boş olamaz")
    @Size(min = 11, max = 11, message = "Kimlik numarası 11 haneli olmalıdır")
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

    public AurDriverDTO(String driverName, String phoneNumber, String licensePlate, String trailerPlate, String opType, String identityNumber) {
        this.driverName = driverName;
        this.phoneNumber = phoneNumber;
        this.licensePlate = licensePlate;
        this.trailerPlate = trailerPlate;
        this.opType = opType;
        this.identityNumber = identityNumber;
    }

    public AurDriverDTO() {
    }
}
