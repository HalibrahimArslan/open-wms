package com.hisarresearch.wms.domain;

import javax.persistence.*;

@Entity
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "getSupplierOrder",
            query = "select supplier_id, supplier_name from aur_vw_supplier_order_exists where company_code = :companyCode",
            resultClass = AurVwSupplierOrderExists.class
        ),
    }
)
public class AurVwSupplierOrderExists {

    @Id
    private Integer supplierId;

    @Column(name = "supplier_name")
    private String supplierName;

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
