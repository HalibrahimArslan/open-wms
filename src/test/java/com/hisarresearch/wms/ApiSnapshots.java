package com.hisarresearch.wms;

import static org.assertj.core.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * Bir ucun JSON cevabini {@code src/test/resources/api-snapshots/<ad>.json} ile karsilastirir.
 * <p>
 * Surum gecislerinde {@code open-wms-app}'in bagli oldugu cevap seklinin (alan adlari,
 * null'lar, tarih bicimi, sira) degismedigini yakalamak icindir. Kayit yoksa ya da
 * {@code -Dsnapshot.update=true} verildiyse dosya yazilir ve test basarisiz olur; yazilan
 * dosya gozden gecirilip commit edilir.
 */
final class ApiSnapshots {

    private static final Path DIR = Path.of("src/test/resources/api-snapshots");

    /** Her calistirmada degisen alanlar; karsilastirmadan once cikarilir. */
    private static final Set<String> VOLATILE_FIELDS = Set.of("createdDate", "lastModifiedDate");

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private ApiSnapshots() {}

    static void assertMatches(String name, String actualJson) throws Exception {
        JsonNode actual = MAPPER.readTree(actualJson);
        stripVolatile(actual);
        String normalized = MAPPER.writeValueAsString(actual) + "\n";

        Path file = DIR.resolve(name + ".json");
        if (Boolean.getBoolean("snapshot.update") || !Files.exists(file)) {
            Files.createDirectories(DIR);
            Files.writeString(file, normalized, StandardCharsets.UTF_8);
            fail("Snapshot yazildi: %s. Icerigi kontrol edip commit edin ve testi yeniden calistirin.", file);
        }
        JSONAssert.assertEquals(name, Files.readString(file, StandardCharsets.UTF_8), normalized, JSONCompareMode.STRICT);
    }

    private static void stripVolatile(JsonNode node) {
        if (node.isObject()) {
            ((ObjectNode) node).remove(VOLATILE_FIELDS);
        }
        node.forEach(ApiSnapshots::stripVolatile);
    }
}
