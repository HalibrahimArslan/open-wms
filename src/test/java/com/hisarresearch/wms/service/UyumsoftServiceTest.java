package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.AurLookupTable;
import com.hisarresearch.wms.service.erp.UyumsoftService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UyumsoftServiceTest {
    @Mock
    private AurLookupService lookupService;

    @InjectMocks
    private UyumsoftService uyumsoftService;

    private final String invoiceParam = "UYUMSOFT_INVOICE";

    private List<AurLookupTable> noResponse  = new ArrayList<>();
    private List<AurLookupTable> active  = new ArrayList<>();
    private List<AurLookupTable> deActive  = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        AurLookupTable aurLookupTable = new AurLookupTable();
        aurLookupTable.setLookupName(invoiceParam);
        aurLookupTable.setLookupCode("1");
        active.add(aurLookupTable);
        AurLookupTable deactivated = new AurLookupTable();
        deactivated.setLookupName(invoiceParam);
        deactivated.setLookupCode("0");
        deActive.add(deactivated);
    }

    @Test
    void testInvoiceStatusWhenNoResponse(){
        //Given
        when(lookupService.getByLookupName(invoiceParam)).thenReturn(noResponse);

        boolean result = uyumsoftService.getInvoiceStatus();

        Assert.assertEquals("No Response Test is executed",false, result);
    }

    @Test
    void testInvoiceStatusWhenActive(){
        //Given
        when(lookupService.getByLookupName(invoiceParam)).thenReturn(active);

        boolean result = uyumsoftService.getInvoiceStatus();

        Assert.assertEquals("Active Scenario Test is executed",true, result);
    }

    @Test
    void testInvoiceStatusWhenDeactivate(){
        //Given
        when(lookupService.getByLookupName(invoiceParam)).thenReturn(deActive);

        boolean result = uyumsoftService.getInvoiceStatus();

        Assert.assertEquals("Deactivated Scenario Test is executed",false, result);
    }
}
