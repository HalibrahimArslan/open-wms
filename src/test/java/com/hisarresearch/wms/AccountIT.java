package com.hisarresearch.wms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * /api/account: sirketi olmayan kullanici ve veritabaninin uygulama disindan degistirilmesi.
 * <p>
 * Kullanici hem Spring onbelleginde (usersByLogin) hem Hibernate ikinci seviye onbelleginde
 * tutulur. Dogrudan SQL ile yapilan degisiklik (ornegin seed/local-seed.sql) onbellek
 * temizlenene ya da suresi dolana kadar gorunmez; DELETE /management/caches ikisini de bosaltir.
 */
class AccountIT extends AbstractIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TransactionTemplate transactionTemplate;

    @AfterEach
    void restoreCompanyCode() throws Exception {
        sql("UPDATE aur_user SET company_code = 1 WHERE login IN ('admin', 'user')");
        clearCaches();
    }

    @Test
    void accountWorksForUserWithoutCompany() throws Exception {
        sql("UPDATE aur_user SET company_code = NULL WHERE login = 'user'");
        clearCaches();

        mockMvc
            .perform(get("/api/account").header("Authorization", "Bearer " + login("user", "user")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.login").value("user"))
            .andExpect(jsonPath("$.companyCode").doesNotExist());
    }

    @Test
    void companyInfoIsBusinessErrorForUserWithoutCompany() throws Exception {
        sql("UPDATE aur_user SET company_code = NULL WHERE login = 'user'");
        clearCaches();

        // Eskiden ModelMapper null kaynakta IllegalArgumentException firlatiyor, 500 donuyordu
        mockMvc
            .perform(get("/api/users/companyInfo").header("Authorization", "Bearer " + login("user", "user")))
            .andExpect(status().isExpectationFailed())
            .andExpect(jsonPath("$.title").value("Kullanıcının şirketi tanımlı değil"))
            .andExpect(jsonPath("$.errorKey").value("userCompanyNotFound"));
    }

    @Test
    void companyInfoIsBusinessErrorWhenCompanyDoesNotExist() throws Exception {
        sql("UPDATE aur_user SET company_code = 999 WHERE login = 'user'");
        clearCaches();

        mockMvc
            .perform(get("/api/users/companyInfo").header("Authorization", "Bearer " + login("user", "user")))
            .andExpect(status().isExpectationFailed())
            .andExpect(jsonPath("$.errorKey").value("userCompanyNotFound"));
    }

    @Test
    void directDatabaseChangeIsVisibleAfterCacheEviction() throws Exception {
        String token = adminToken();
        mockMvc.perform(get("/api/account").header("Authorization", token)).andExpect(jsonPath("$.companyCode").value(1));

        sql("UPDATE aur_user SET company_code = 2 WHERE login = 'admin'");
        // Onbellek temizlenmeden eski deger doner (beklenen davranis)
        mockMvc.perform(get("/api/account").header("Authorization", token)).andExpect(jsonPath("$.companyCode").value(1));

        mockMvc.perform(delete("/management/caches").header("Authorization", token)).andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/api/account").header("Authorization", token)).andExpect(jsonPath("$.companyCode").value(2));
    }

    /** Havuz auto-commit kapali calistigi icin degisiklik acik bir transaction'la commit edilir. */
    private void sql(String statement) {
        transactionTemplate.executeWithoutResult(status -> jdbcTemplate.update(statement));
    }

    private void clearCaches() throws Exception {
        mockMvc.perform(delete("/management/caches").header("Authorization", adminToken())).andExpect(status().is2xxSuccessful());
    }
}
