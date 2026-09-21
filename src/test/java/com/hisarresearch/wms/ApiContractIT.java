package com.hisarresearch.wms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Arayuzun kullandigi okuma uclarinin seed verisi uzerindeki cevaplarini kayitli
 * snapshot'larla karsilastirir. Surum gecisinde bir cevabin sekli degisirse burada yakalanir.
 */
class ApiContractIT extends AbstractIntegrationTest {

    private String token;

    @BeforeEach
    void authenticate() throws Exception {
        token = adminToken();
    }

    @ParameterizedTest(name = "GET {1}")
    @CsvSource(
        {
            "account,                 /api/account",
            // Uc siralama belirtilmezse satirlari fiziksel sirayla doner; guncellenen satir sona kayar
            "admin-users,             '/api/admin/users?sort=id,asc'",
            "company-info,            /api/users/companyInfo",
            "menus,                   /api/aur-menus",
            "warehouses,              /api/warehouse",
            "depo-list,               /api/depoList",
            "products,                /api/product",
            "address-list,            /api/address-list",
            "erp-adapters,            /api/erp/adapters",
            "firm-list-receiving,     /api/firmList/1/1",
            "firm-list-shipment,      /api/firmList/1/0",
            "order-detail-receiving,  /api/orderDetail/A-1001/1/1",
            "order-detail-shipment,   /api/orderDetail/S-3001/0/1",
            "order-picking,           /api/order-picking-transactions",
            "counting-definitions,    /api/aur-sayim-tanims",
            "produce-barcode-local,   /api/produceBarkod/STK-001",
        }
    )
    void getEndpointMatchesSnapshot(String snapshot, String url) throws Exception {
        assertSnapshot(snapshot, get(url));
    }

    @Test
    void trailingSlashStillMatches() throws Exception {
        // Boot 2 davranisi: sonu / ile biten istek ayni uca gider (WebConfigurer.trailingSlashFilter)
        assertSnapshot("depo-list", get("/api/depoList/"));
    }

    @ParameterizedTest(name = "POST {1}")
    @CsvSource(
        delimiter = '|',
        value = {
            "firm-order-list-receiving | /api/firmOrderList | {\"depoList\":[1],\"firmCode\":\"320.01.001\",\"sipTip\":1}",
            "firm-order-list-shipment  | /api/firmOrderList | {\"depoList\":[1],\"firmCode\":\"320.02.001\",\"sipTip\":0}",
        }
    )
    void postEndpointMatchesSnapshot(String snapshot, String url, String body) throws Exception {
        assertSnapshot(snapshot, post(url).contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private void assertSnapshot(String snapshot, MockHttpServletRequestBuilder request) throws Exception {
        String body = mockMvc.perform(request.header("Authorization", token)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        ApiSnapshots.assertMatches(snapshot, body);
    }
}
