package com.hisarresearch.wms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisarresearch.wms.service.WebSocketClientService;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

/**
 * Uygulamanin tamamini gercek bir PostgreSQL uzerinde ayaga kaldiran testlerin tabani.
 * <p>
 * Tum *IT siniflari ayni Spring context'ini ve ayni konteyneri paylasir. Sema Liquibase
 * ile kurulur, uzerine lokal gelistirmede kullanilan {@code seed/local-seed.sql} bir kez
 * yuklenir. Testler bu veriyi okur; veriyi degistiren test kendi kaydini olusturmalidir.
 */
@SpringBootTest(classes = WmsApp.class)
@AutoConfigureMockMvc
@ActiveProfiles({ "test", "api-docs" })
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18")
        .withDatabaseName("wms")
        .withUsername("wms")
        .withPassword("wms")
        .withEnv("TZ", "Europe/Istanbul");

    private static boolean seedLoaded;

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    /** Acilista baglanti denemesiyle bekleten gercek istemcinin yerine gecer. */
    @MockBean
    protected WebSocketClientService webSocketClientService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void loadSeedOnce() throws Exception {
        synchronized (AbstractIntegrationTest.class) {
            if (seedLoaded) {
                return;
            }
            // Liquibase context acilirken calisti; seed semanin ustune yuklenir.
            POSTGRES.copyFileToContainer(MountableFile.forHostPath(Path.of("seed/local-seed.sql")), "/tmp/local-seed.sql");
            ExecResult result = POSTGRES.execInContainer(
                "psql",
                "-v",
                "ON_ERROR_STOP=1",
                "-U",
                POSTGRES.getUsername(),
                "-d",
                POSTGRES.getDatabaseName(),
                "-f",
                "/tmp/local-seed.sql"
            );
            if (result.getExitCode() != 0) {
                throw new IllegalStateException("seed/local-seed.sql yuklenemedi:\n" + result.getStderr());
            }
            seedLoaded = true;
        }
    }

    /** {@code /api/authenticate} ile giris yapar ve JWT dondurur. */
    protected String login(String username, String password) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("username", username, "password", password));
        String response = mockMvc
            .perform(post("/api/authenticate").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return objectMapper.readTree(response).get("id_token").asText();
    }

    protected String adminToken() throws Exception {
        return "Bearer " + login("admin", "admin");
    }
}
