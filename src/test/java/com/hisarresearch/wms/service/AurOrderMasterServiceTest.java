package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurOrderDetail;
import com.hisarresearch.wms.domain.AurOrderMaster;
import com.hisarresearch.wms.repository.AurOrderMasterRepository;
import com.hisarresearch.wms.repository.AurOrderDetailRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hisarresearch.wms.service.dto.AurOrderDetailDTO;
import com.hisarresearch.wms.service.dto.AurOrderMasterDTO;
import com.hisarresearch.wms.exception.api.BadRequestAlertException;
import com.hisarresearch.wms.service.mapper.AurOrderDetailMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AurOrderMasterServiceTest {

    @Mock
    private AurOrderMasterRepository aurOrderMasterRepository;

    @Mock
    private AurOrderDetailService aurOrderDetailService;

    @Spy
    @InjectMocks
    private AurOrderMasterService aurOrderMasterService;

    @Mock
    private AurOrderDetailRepository aurOrderDetailRepository;

    private List<AurOrderDetail> aurOrderDetails;
    private List<AurOrderDetailDTO> aurOrderDetailDTOs;

    @BeforeEach
    public void setUp() {
        AurOrderMaster aurOrderMaster1 = new AurOrderMaster();
        aurOrderMaster1.setId(1L);
        aurOrderMaster1.setOpType("MSK");

        AurOrderMaster aurOrderMaster2 = new AurOrderMaster();
        aurOrderMaster2.setId(2L);
        aurOrderMaster2.setOpType("MSK");

        AurOrderDetail aurOrderDetail1 = new AurOrderDetail();
        aurOrderDetail1.setOrder(aurOrderMaster1);

        AurOrderDetail aurOrderDetail2 = new AurOrderDetail();
        aurOrderDetail2.setOrder(aurOrderMaster2);

        aurOrderDetails = new ArrayList<>();
        aurOrderDetails.add(aurOrderDetail1);
        aurOrderDetails.add(aurOrderDetail2);

        AurOrderDetailDTO dto1 = new AurOrderDetailDTO();
        dto1.setOrderId(aurOrderMaster1.getId());

        AurOrderDetailDTO dto2 = new AurOrderDetailDTO();
        dto2.setOrderId(aurOrderMaster2.getId());

        aurOrderDetailDTOs = new ArrayList<>();
        aurOrderDetailDTOs.add(dto1);
        aurOrderDetailDTOs.add(dto2);
    }

    @Test
    void testHasPreviousOrderRecordBySipUid_WhenRecordExists_ShouldReturnTrue() {
        List<String> sipUids = List.of("UID1", "UID2");
        String operationType = "MSK";

        when(aurOrderDetailService.findBySipUidInAndStatusIn(eq(sipUids), anyList()))
            .thenReturn(aurOrderDetails);

        when(aurOrderMasterRepository.findById(1L))
            .thenReturn(Optional.empty());

        AurOrderMaster orderMasterById2 = new AurOrderMaster();
        orderMasterById2.setOpType(operationType);
        when(aurOrderMasterRepository.findById(2L))
            .thenReturn(Optional.of(orderMasterById2));

        boolean result = aurOrderMasterService.hasPreviousOrderRecordBySipUid(sipUids, operationType);

        assertTrue(result, "Açık bir sipariş kaydı bulunduğunda true dönmeli");
        verify(aurOrderDetailService, times(1))
            .findBySipUidInAndStatusIn(eq(sipUids), anyList());
    }

    @Test
    void testHasPreviousOrderRecordBySipUid_WhenNoRecordExists_ShouldReturnFalse() {
        List<String> sipUids = List.of("UID1", "UID2");
        String operationType = "MSK";

        when(aurOrderDetailService.findBySipUidInAndStatusIn(eq(sipUids), anyList()))
            .thenReturn(aurOrderDetails);

        when(aurOrderMasterRepository.findById(1L))
            .thenReturn(Optional.empty());
        when(aurOrderMasterRepository.findById(2L))
            .thenReturn(Optional.empty());

        boolean result = aurOrderMasterService.hasPreviousOrderRecordBySipUid(sipUids, operationType);

        assertFalse(result, "Açık bir sipariş kaydı bulunmadığından false dönmeli");
    }

    @Test
    void testSaveAurOrderList_WhenHasPreviousOrderRecord_ShouldThrowException() {
        AurOrderMasterDTO aurOrderMasterDTO = new AurOrderMasterDTO();
        aurOrderMasterDTO.setAurTmpDetailList(aurOrderDetailDTOs);
        aurOrderMasterDTO.setOpType("MSK");

        when(aurOrderMasterRepository.findByOrderInfo(any())).thenReturn(Optional.empty());
        doReturn(true).when(aurOrderMasterService).hasPreviousOrderRecordBySipUid(anyList(), anyString());

        Exception exception = assertThrows(
            BadRequestAlertException.class,
            () -> aurOrderMasterService.saveAurOrderList(aurOrderMasterDTO)
        );

        assertEquals("Ilgili siparis nolara ait devam eden kayıt var", exception.getMessage());
    }
}
