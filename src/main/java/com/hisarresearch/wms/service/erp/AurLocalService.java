package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.AurVwFirmOrderExists;
import com.hisarresearch.wms.domain.AurVwFirmOrderListBulk;
import com.hisarresearch.wms.domain.Warehouse;
import com.hisarresearch.wms.domain.enumeration.ErpConnectionType;
import com.hisarresearch.wms.repository.WarehouseRepository;
import com.hisarresearch.wms.service.dto.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hisarresearch.wms.service.mapper.WarehouseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class AurLocalService{

    /** Mikro sozlesmesinde "0" acik siparis satirini ifade eder. */
    private static final String OPEN_LINE_STATUS = "0";
    private static final String APPROVED = "Onayli";

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private com.hisarresearch.wms.service.AurOrderMasterService aurOrderMasterService;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private EntityManager em;

    public List<WarehouseDTO> getDepoList(String companyCode) {
        List<WarehouseDTO> realWarehouses = new ArrayList<>();
       warehouseRepository.findAll()
            .stream()
            .filter(Warehouse::getReal)
            .forEach(
                warehouse -> {
                    if ((companyCode.equals("0")) || (warehouse.getCompanyCode().equals(companyCode))) {
                        realWarehouses.add(warehouseMapper.toDto(warehouse));
                    }
                }
            );

        return realWarehouses;
    }

    public List<AurCariDto> getFirmList(Short sipTip, String cariBaglantiTipi, Short sipDepoNo) {
        Query q = em.createNamedQuery("getFirmOrderExists");

        q.setParameter("sipTip", sipTip);
        q.setParameter("cariBaglantiTipi",cariBaglantiTipi);
        q.setParameter("sipDepoNo",sipDepoNo);

        ModelMapper mm = new ModelMapper();
        List<AurVwFirmOrderExists> dbResultList = q.getResultList();
        List<AurCariDto> resultDtoList = dbResultList
            .stream()
            .map(domain -> mm.map(domain, AurCariDto.class))
            .collect(Collectors.toList());
        return resultDtoList;
    }

    /**
     * Mikro'nun {@code getCariOrderDetailList} yanitinin birebir ayni sekildeki yerel karsiligi.
     *
     * <p>Mikro tarafinda ham JSON {@code AurOrderMasterService.getFilteredDepoOrderDetails}
     * icinde ayiklanip zenginlestiriliyor. Burada ayni kurallar uygulanir:
     * yalnizca acik satirlar (durum "0" ve teslim &lt; siparis) donulur, {@code id} sirali
     * atanir, siparis basligi alanlari her satira kopyalanir ve son olarak ortak
     * {@code enrichDepoOrderDetails} zenginlestirmesinden gecirilir.
     */
    public List<AurCariOrderDetailListDto> getCariOrderDetailList(AurFirmListDto aurFirmListDto) {
        List<AurVwFirmOrderListBulk> rows = queryOrderListBulk(aurFirmListDto);

        Map<String, Integer> lineCountByOrder = new LinkedHashMap<>();
        for (AurVwFirmOrderListBulk row : rows) {
            if (isOpenLine(row)) {
                lineCountByOrder.merge(row.getOrderNo(), 1, Integer::sum);
            }
        }

        List<AurCariOrderDetailListDto> cariList = new ArrayList<>();
        int index = 0;
        for (AurVwFirmOrderListBulk row : rows) {
            if (!isOpenLine(row)) {
                continue;
            }
            AurCariOrderDetailListDto dto = new AurCariOrderDetailListDto();
            dto.setId(index++);
            dto.setOrderNo(row.getOrderNo());
            dto.setOrderDate(row.getOrderDate());
            dto.setOrderLineItemCount(lineCountByOrder.getOrDefault(row.getOrderNo(), 0));
            dto.setBarkod(row.getBarkod());
            dto.setStokKodu(row.getStokKodu());
            dto.setStokAdi(row.getStokAdi());
            dto.setStokBirimi(row.getStokBirimi());
            dto.setSiparisMiktar(nullSafe(row.getSiparisMiktar()));
            dto.setTeslimMiktar(nullSafe(row.getTeslimMiktar()));
            dto.setSipUid(row.getSipUid());
            dto.setDurum(OPEN_LINE_STATUS);
            // Yerel modda ERP onay akisi yok; satirlar onayli kabul edilir.
            dto.setOnayDurum(APPROVED);
            cariList.add(dto);
        }

        return aurOrderMasterService.enrichDepoOrderDetails(cariList);
    }

    @SuppressWarnings("unchecked")
    private List<AurVwFirmOrderListBulk> queryOrderListBulk(AurFirmListDto aurFirmListDto) {
        Query q = em.createNamedQuery("getFirmOrderDetailBulk");
        q.setParameter("sipTip", aurFirmListDto.getSipTip());
        q.setParameter("cariBaglantiTipi", String.valueOf(aurFirmListDto.getSipTip()));
        q.setParameter("depoList", aurFirmListDto.getDepoList());
        q.setParameter("musteriKod", aurFirmListDto.getFirmCode());
        return q.getResultList();
    }

    private static boolean isOpenLine(AurVwFirmOrderListBulk row) {
        return nullSafe(row.getTeslimMiktar()) < nullSafe(row.getSiparisMiktar());
    }

    private static double nullSafe(Double value) {
        return value == null ? 0d : value;
    }

    /**
     * Bir carinin siparislerini, ERP servisi yerine yerel {@code aur_erp_data} verisinden
     * siparis basligi bazinda gruplayarak dondurur.
     *
     * <p>{@code MikroServices.getCariOrderList} ile ayni sozlesme: yalnizca acik satirlar
     * (durum "0" ve teslim miktari siparis miktarindan kucuk) donulur.
     */
    public List<AurCariOrderDto> getCariOrderList(AurFirmListDto aurFirmListDto) {
        List<AurVwFirmOrderListBulk> rows = queryOrderListBulk(aurFirmListDto);

        Map<String, AurCariOrderDto> ordersByNo = new LinkedHashMap<>();
        for (AurVwFirmOrderListBulk row : rows) {
            if (!isOpenLine(row)) {
                continue;
            }

            AurCariOrderDetailDto detail = new AurCariOrderDetailDto();
            detail.setBarkod(row.getBarkod());
            detail.setStokKodu(row.getStokKodu());
            detail.setStokAdi(row.getStokAdi());
            detail.setStokBirimi(row.getStokBirimi());
            detail.setSiparisMiktar(nullSafe(row.getSiparisMiktar()));
            detail.setTeslimMiktar(nullSafe(row.getTeslimMiktar()));
            detail.setSipUid(row.getSipUid());
            detail.setDurum(OPEN_LINE_STATUS);

            AurCariOrderDto order = ordersByNo.computeIfAbsent(row.getOrderNo(), orderNo -> {
                AurCariOrderDto created = new AurCariOrderDto();
                created.setOrderNo(orderNo);
                created.setOrderDate(row.getOrderDate());
                created.setOrderDetail(new ArrayList<>());
                return created;
            });
            order.getOrderDetail().add(detail);
        }

        ordersByNo.values().forEach(order -> order.setOrderLineItemCount(order.getOrderDetail().size()));
        return new ArrayList<>(ordersByNo.values());
    }
}
