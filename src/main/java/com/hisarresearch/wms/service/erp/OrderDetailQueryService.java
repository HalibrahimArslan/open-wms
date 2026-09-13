package com.hisarresearch.wms.service.erp;

import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.service.AurPartialItemService;
import com.hisarresearch.wms.service.ProductService;
import com.hisarresearch.wms.service.UserService;
import com.hisarresearch.wms.service.dto.AurCariOrderDetailDto;
import com.hisarresearch.wms.service.dto.AurPartialDetailsDTO;
import com.hisarresearch.wms.service.dto.AurPartialResponseDto;
import com.hisarresearch.wms.service.dto.mikro.StockDetailResponseDto;
import com.hisarresearch.wms.service.dto.product.ProductWithoutAddressDTO;
import com.hisarresearch.wms.service.mapper.ProductWithoutAddressMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderDetailQueryService {

    private final ErpOrderGateway erpOrderGateway;
    private final ProductService productService;
    private final AurPartialItemService aurPartialItemService;
    private final UserService userService;
    private final ProductWithoutAddressMapper productWithoutAddressMapper;

    public OrderDetailQueryService(ErpOrderGateway erpOrderGateway, ProductService productService,
                                   AurPartialItemService aurPartialItemService, UserService userService,
                                   ProductWithoutAddressMapper productWithoutAddressMapper) {
        this.erpOrderGateway = erpOrderGateway;
        this.productService = productService;
        this.aurPartialItemService = aurPartialItemService;
        this.userService = userService;
        this.productWithoutAddressMapper = productWithoutAddressMapper;
    }

    public List<AurCariOrderDetailDto> getOrderDetail(String token, String apiPath, String orderNo,
                                                      Integer sipTip, Integer depoNo) throws Exception {
        List<AurCariOrderDetailDto> detailList = erpOrderGateway.getOrderDetail(token, apiPath, orderNo, sipTip, depoNo);

        String companyCode = String.valueOf(userService.getUserCompanyInfo().getCompanyCode());
        List<String> barcodes = detailList.stream()
            .map(AurCariOrderDetailDto::getBarkod)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        Map<String, Product> productMap = productService.findByBarcodes(barcodes, companyCode);

        for (AurCariOrderDetailDto item : detailList) {
            List<String> packageCode = new ArrayList<>();
            packageCode.add(item.getStokKodu());
            List<AurPartialResponseDto> aurPartialResponse = aurPartialItemService.getPartialItemDetail(packageCode);
            if (!aurPartialResponse.isEmpty()) {
                item.setHasPiece(true);
                item.setPartialList(aurPartialResponse);
                enrichPartsWithProduct(aurPartialResponse, companyCode, token, apiPath, depoNo);
            } else {
                item.setPartialList(new ArrayList<>());
            }

            Product product = productMap.get(item.getBarkod());
            if (product != null) {
                item.setLotBasedTracking(product.getLotBasedTracking());
            }
        }
        return detailList;
    }

    private void enrichPartsWithProduct(List<AurPartialResponseDto> partialResponse, String companyCode,
                                        String token, String apiPath, Integer depoNo) throws Exception {
        List<AurPartialDetailsDTO> allParts = partialResponse.stream()
            .map(AurPartialResponseDto::getPackageDetail)
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .collect(Collectors.toList());

        if (allParts.isEmpty()) {
            return;
        }

        List<String> partBarcodes = allParts.stream()
            .map(AurPartialDetailsDTO::getBarcode)
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());

        Map<String, Product> partProductMap = productService.findByBarcodes(partBarcodes, companyCode);

        List<String> barcodesToQuery = partBarcodes.stream()
            .filter(barcode -> needsStockDetail(partProductMap.get(barcode)))
            .collect(Collectors.toList());

        Map<String, StockDetailResponseDto> stockDetailMap = barcodesToQuery.isEmpty()
            ? Collections.emptyMap()
            : erpOrderGateway.getStockDetails(token, apiPath, barcodesToQuery, depoNo);

        updateMissingPhysicalAttributes(partProductMap, stockDetailMap);

        allParts.forEach(part ->
            part.setProduct(resolvePartProduct(part, partProductMap, stockDetailMap, companyCode)));
    }

    private void updateMissingPhysicalAttributes(Map<String, Product> partProductMap,
                                                 Map<String, StockDetailResponseDto> stockDetailMap) {
        List<Product> productsToUpdate = new ArrayList<>();
        partProductMap.forEach((barcode, product) -> {
            StockDetailResponseDto stockDetail = stockDetailMap.get(barcode);
            if (stockDetail != null && isPhysicalAttributesEmpty(product.getPhysicalAttributes())) {
                product.setPhysicalAttributes(stockDetail.getPhysicalAttributes());
                productsToUpdate.add(product);
            }
        });
        productService.updatePhysicalAttributes(productsToUpdate);
    }

    private ProductWithoutAddressDTO resolvePartProduct(AurPartialDetailsDTO part,
                                                        Map<String, Product> partProductMap,
                                                        Map<String, StockDetailResponseDto> stockDetailMap,
                                                        String companyCode) {
        String barcode = part.getBarcode();
        if (barcode == null) {
            return null;
        }
        StockDetailResponseDto stockDetail = stockDetailMap.get(barcode);
        Product product = partProductMap.get(barcode);
        if (product != null) {
            ProductWithoutAddressDTO dto = productWithoutAddressMapper.toDto(product);
            if (stockDetail != null && isPhysicalAttributesEmpty(dto.getPhysicalAttributes())) {
                dto.setPhysicalAttributes(stockDetail.getPhysicalAttributes());
            }
            return dto;
        }
        if (stockDetail == null) {
            return null;
        }
        ProductWithoutAddressDTO dto = new ProductWithoutAddressDTO();
        dto.setBarcode(stockDetail.getBarkod());
        dto.setCompanyCode(companyCode);
        dto.setStokKodu(stockDetail.getStokKodu());
        dto.setStokAdi(stockDetail.getStokAdi());
        dto.setStokBirimi(stockDetail.getStokBirimi());
        dto.setKategoriAdi(stockDetail.getKategoriAdi());
        dto.setAnaGrup(stockDetail.getAnagrupAdi());
        dto.setMiktar(stockDetail.getDepodakiMiktar());
        dto.setPhysicalAttributes(stockDetail.getPhysicalAttributes());
        dto.setLotBasedTracking(false);
        return dto;
    }

    private boolean needsStockDetail(Product product) {
        return product == null || isPhysicalAttributesEmpty(product.getPhysicalAttributes());
    }

    private boolean isPhysicalAttributesEmpty(Map<String, Object> physicalAttributes) {
        if (physicalAttributes == null || physicalAttributes.isEmpty()) {
            return true;
        }
        return physicalAttributes.values().stream()
            .allMatch(value -> value == null
                || (value instanceof Number && ((Number) value).doubleValue() == 0.0));
    }
}
