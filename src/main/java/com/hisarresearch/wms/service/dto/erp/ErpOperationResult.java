package com.hisarresearch.wms.service.dto.erp;

/**
 * ERP'ye yazan islemler (mal kabul, sevkiyat, barkod uretimi) icin sade sonuc nesnesi.
 *
 * <p>Yerel modda bu islemlerin is kurallari heniz yazilmadi; adaptor cagriyi kabul edip
 * bu nesneyi dondurur, boylece istemci tarafi bos/500 yanit yerine islenebilir bir
 * sonuc alir. Is kurallari eklendiginde {@code reference} alanina uretilen belge/barkod
 * numarasi yazilabilir.
 */
public class ErpOperationResult {

    private boolean success;
    private String message;
    private String reference;

    public ErpOperationResult() {
    }

    public ErpOperationResult(boolean success, String message, String reference) {
        this.success = success;
        this.message = message;
        this.reference = reference;
    }

    public static ErpOperationResult notImplemented(String operation) {
        return new ErpOperationResult(false,
            operation + ": yerel modda bu islem heniz uygulanmadi", null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
