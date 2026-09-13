package com.hisarresearch.wms.service.dto.barcode;

import com.hisarresearch.wms.service.barcode.statemachine.UniqueBarcodeState;
import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;

public class UniqueBarcodeCreateDTO {

    @Valid
    @NotNull
    private ProductWithoutAddressDTO product;

    @NotBlank
    private String customerCode;

    @NotBlank
    private String erpOrderNo;

    private UniqueBarcodeState status;

    @NotBlank
    @Pattern(
        regexp = "\\d{6}",
        message = "receivingDate yyMMdd formatında olmalıdır (örn: 251126)"
    )
    private String receivingDate;

    @NotNull
    @Positive
    @Digits(integer = 9, fraction = 0)
    private Integer adet;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Positive
    private BigDecimal referenceAmount;

    private Map<String, Object> description;

    public ProductWithoutAddressDTO getProduct() {
        return product;
    }

    public void setProduct(ProductWithoutAddressDTO product) {
        this.product = product;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getErpOrderNo() {
        return erpOrderNo;
    }

    public void setErpOrderNo(String erpOrderNo) {
        this.erpOrderNo = erpOrderNo;
    }

    public UniqueBarcodeState getStatus() {
        return status;
    }

    public void setStatus(UniqueBarcodeState status) {
        this.status = status;
    }

    public String getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(String receivingDate) {
        this.receivingDate = receivingDate;
    }

    public Integer getAdet() {
        return adet;
    }

    public void setAdet(Integer adet) {
        this.adet = adet;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getReferenceAmount() {
        return referenceAmount;
    }

    public void setReferenceAmount(BigDecimal referenceAmount) {
        this.referenceAmount = referenceAmount;
    }

    public Map<String, Object> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Object> description) {
        this.description = description;
    }
}
