package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurVwFirmOrderDetail;
import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailListDto;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.exception.business.BusinessException;
import net.sf.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class WarehouseService {

    private final String ENTITY_NAME = "warehouse";

    private final EntityManager em;

    private final WarehouseRepository warehouseRepository;

    private final UserService userService;

    private final UserDepoRelService userDepoRelService;

    public WarehouseService(EntityManager em, WarehouseRepository warehouseRepository,
                            UserService userService,UserDepoRelService userDepoRelService) {
        this.em = em;
        this.warehouseRepository = warehouseRepository;
        this.userService = userService;
        this.userDepoRelService = userDepoRelService;
    }

    @Transactional(readOnly = true)
    public Optional<Warehouse> findByCode(String code, String companyCode){
        return warehouseRepository.findByCodeAndCompanyCode(code, companyCode);
    }

    public List<AurCariOrderDetailListDto> getFirmStockOrderList(JSONObject data) {
        Query q = em.createNamedQuery("getFirmOrderDetail");

        q.setParameter("sipTip", data.getInt("sipTip"));
        q.setParameter("cariBaglantiTipi", String.valueOf(data.getInt("sipTip")));
        q.setParameter("depoNo", data.getInt("depoNo"));
        q.setParameter("musteriKod", data.getString("firmCode"));


        ModelMapper mm = new ModelMapper();
        List<AurVwFirmOrderDetail> dbResultList = q.getResultList();
        return dbResultList
            .stream()
            .map(domain -> mm.map(domain, AurCariOrderDetailListDto.class))
            .collect(Collectors.toList());
    }

    public String createVirtualWarehouse(String code, String name) {
        Warehouse warehouse = new Warehouse();
        String companyCode = String.valueOf(userService.getUserCompanyCode());
        Optional<Warehouse> isExistDepoCode = warehouseRepository.findByCodeAndCompanyCode(code, companyCode);
        Optional<Warehouse> isExistDepoName =  warehouseRepository.findByNameAndCompanyCode(name, companyCode);

        if(isExistDepoCode.isPresent() || isExistDepoName.isPresent()){
            throw new BadRequestAlertException("Invalid depoCode or depoName request params","Depo","depoParamsErr");
        }
        else{
            warehouse.setCode(code);
            warehouse.setName(name);
            warehouse.setCompanyCode(companyCode);
            warehouse.setReal(true);
            warehouse.setCountable(false);
            warehouse.setAutoScan(false);
            warehouseRepository.save(warehouse);
        }

        return "success";

    }


    public void clearRelatedUserCaches(long id){
        Optional<Warehouse> searchWarehouse = warehouseRepository.findById(id);
        searchWarehouse.ifPresent(warehouse -> userDepoRelService.findByWarehouse(warehouse.getId()).forEach(userDepoRel -> {
            userService.clearUserCaches(userDepoRel.getUser());
        }));
    }

    public Warehouse findByDepoCodeAndCompanyCode(String depoCode, String companyCode ){
        return warehouseRepository.findByCodeAndCompanyCode(depoCode,companyCode).orElseThrow(() -> new BusinessException("Invalid warehouse code",ENTITY_NAME,"invalidWarehouseCode"));
    }

    public Boolean hasUniquePickingAddress(String depoCode,String companyCode){
        Warehouse warehouse = findByDepoCodeAndCompanyCode(transformWarehouseCode(depoCode),companyCode);
        return warehouse.getUniquePickingAddress();
    }

    public String transformWarehouseCode(String depoCode){
        String warehouseCode = depoCode;
        ErpConnectionType erpType = userService.getUserErpType();
        if(erpType == ErpConnectionType.UYUMSOFT && warehouseCode.length() > 2){
            warehouseCode = warehouseCode.substring(0, 2) + " " + warehouseCode.substring(2);
        }
        return warehouseCode;
    }




}
