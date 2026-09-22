package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurReserve;
import com.hisarresearch.wms.domain.AurReserveStatus;
import com.hisarresearch.wms.domain.Product;
import com.hisarresearch.wms.repository.AurReserveRepository;
import com.hisarresearch.wms.service.dto.AurReserveDTO;
import com.hisarresearch.wms.service.dto.PartialDetailDto;
import com.hisarresearch.wms.service.mapper.AurReserveMapper;
import com.hisarresearch.wms.utility.AurHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class AurReserveService {

    private final Logger log = LoggerFactory.getLogger(AurReserveService.class);

    private final AurReserveRepository aurReserveRepository;
    private final AurReserveMapper aurReserveMapper;
    private final ProductService productService;

    public AurReserveService(AurReserveRepository aurReserveRepository, AurReserveMapper aurReserveMapper, ProductService productService) {
        this.aurReserveRepository = aurReserveRepository;
        this.aurReserveMapper = aurReserveMapper;
        this.productService = productService;
    }

    public List<AurReserveDTO> getAurReserves(String companyCode) {
        log.debug("Request getAurReserves by companyCode: {}", companyCode);
        return aurReserveMapper.toDto(aurReserveRepository.findByProduct_Id_CompanyCode(companyCode));
    }

    public List<AurReserveDTO> saveAurReserves(List<AurReserveDTO> aurReserveDTOs) {
        log.debug("Request to save AurReserves: {}", aurReserveDTOs);
        List<AurReserve> aurReserves = aurReserveMapper.toEntity(aurReserveDTOs);
        aurReserves.forEach(aurReserve -> {
            Optional<Product> product = productService.findById(aurReserve.getProduct().getId().getCompanyCode(), aurReserve.getProduct().getId().getBarkod());
            product.ifPresent(aurReserve::setProduct);
        });
        List<AurReserve> newAurReserves = aurReserveRepository.saveAll(aurReserves);
        return aurReserveMapper.toDto(newAurReserves);
    }

    public Optional<AurReserve> updateAurReserve(AurReserveDTO aurReserveDTO) {
        log.debug("Request to update AurReserve : {}", aurReserveDTO);

        Optional<AurReserve> aurReserve = aurReserveRepository.findById(aurReserveDTO.getId());
        aurReserve.ifPresent(reserve -> reserve.setStatus(aurReserveDTO.getStatus()));
        return aurReserve;
    }

    public void checkReserveStatus(List<PartialDetailDto> aurTmpDetails){
        AurHelper aurHelper = new AurHelper();
        List<String> orderNos = aurTmpDetails.stream().map(PartialDetailDto::getOrderNo).distinct().collect(Collectors.toList());
        List<String> barcodes = new ArrayList<>();
        aurTmpDetails.forEach(detail -> {
            if(detail.getPiece() && !barcodes.contains(detail.getPieceMaster().getBarcode())) {
                barcodes.add(detail.getPieceMaster().getBarcode());
            }
            else {
                barcodes.add(detail.getBarkod());
            }
        });
        List<AurReserve> reserveList = aurReserveRepository.findByProduct_Id_BarkodInAndOrderNoIn(barcodes,orderNos);
        aurTmpDetails.forEach(detail -> {
            Optional<AurReserve> reserveItem = reserveList.stream()
                .filter(reserve -> reserve.getProduct().getId().getBarkod().equals(detail.getPiece() ? detail.getPieceMaster().getBarcode() : detail.getBarkod()) &&
                    reserve.getOrderNo().equals(detail.getOrderNo()) && (reserve.getStatus().equals(AurReserveStatus.RESERVED)))
                .findAny();
            if(reserveItem.isPresent()){
                if(detail.getPiece()){
                    detail.getPieceMaster().setReserve(true);
                    detail.getPieceMaster().setReserveDescription(reserveItem.get().getDescription());
                }
                else {
                    detail.setReserve(true);
                    detail.setReserveDescription(reserveItem.get().getDescription());
                }
            }
        });

    }
}
