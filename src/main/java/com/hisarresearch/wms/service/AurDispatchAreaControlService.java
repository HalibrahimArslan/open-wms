package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurDispatchAreaControl;
import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.repository.AurDispatchAreaControlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AurDispatchAreaControlService {

    private final Logger log = LoggerFactory.getLogger(AurDispatchAreaControlService.class);


    @Autowired
    private AurDispatchAreaControlRepository aurDispatchAreaControlRepository;

    @Autowired
    private AurOrderMasterService aurOrderMasterService;

    @Autowired
    private AurOrderDetailService aurOrderDetailService;

    @Autowired
    private PalletBarcodeOrderRelService palletBarcodeOrderRelService;

    /**
     * Get all the aurDispatchAreaControls.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AurDispatchAreaControl> findAll() {
        log.debug("Request to get all AurDispatchAreaControls");
        return aurDispatchAreaControlRepository.findAll();
    }

    public void saveDispatchMovement(AurOrderDetail aurOrderDetail){
        Optional<AurDispatchAreaControl> observedProduct = aurDispatchAreaControlRepository.findByAurTmpDetail_IdAndStatus(aurOrderDetail.getId(),true);
        if(observedProduct.isPresent()){
            observedProduct.get().setStatus(false);
            saveAurDispatchArea(aurOrderDetail);
        }
        else{
            saveAurDispatchArea(aurOrderDetail);
        }

    }

    void saveAurDispatchArea(AurOrderDetail aurOrderDetail){
        AurDispatchAreaControl aurDispatchAreaControl = new AurDispatchAreaControl();
        aurDispatchAreaControl.setAurTmpDetail(aurOrderDetail);
        aurDispatchAreaControl.setStatus(true);
        aurDispatchAreaControl.setObservedAmount(aurOrderDetail.getObserverAmount());
        aurDispatchAreaControlRepository.save(aurDispatchAreaControl);
    }

    public Collection<String> getDispatchListByOrderId(Long orderId)  {
        Collection<String> availableList = new ArrayList<>();
        aurOrderMasterService.getDetailById(orderId)
            .stream()
            .filter(tmpItem -> tmpItem.getStatus().equals("OUT_PROGRESS") && !tmpItem.getPiece())
            .forEach(item -> aurDispatchAreaControlRepository.findByAurTmpDetail_IdAndStatus(item.getId(),true)
                .ifPresent(q -> availableList.add(q.getAurTmpDetail().getStokKodu())));
        return availableList;
    }

    public void saveByPalletBarcode(String palletBarcode){
        palletBarcodeOrderRelService.getPalletInfo(palletBarcode).forEach(item -> saveDispatchMovement(aurOrderDetailService.findById(item.getAurTmpDetailId()).get()));

    }

}
