package com.hisarresearch.wms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

    private ApiSnapshots() {}

    static void assertMatches(String name, String actualJson) throws Exception {
        JsonNode actual = MAPPER.readTree(actualJson);
        stripVolatile(actual);
        String normalized = normalize(actual);

        Path file = DIR.resolve(name + ".json");
        if (Boolean.getBoolean("snapshot.update") || !Files.exists(file)) {
            Files.createDirectories(DIR);
            Files.writeString(file, normalized, StandardCharsets.UTF_8);
            fail("Snapshot yazildi: %s. Icerigi kontrol edip commit edin ve testi yeniden calistirin.", file);
        }
        // Iki taraf da ayni sekilde yazildigi icin metin karsilastirmasi alan, deger ve dizi sirasina
        // birebir bakar; nesne anahtarlarinin sirasi anlam tasimadigi icin siralanir.
        JsonNode recorded = MAPPER.readTree(file.toFile());
        stripVolatile(recorded);
        String expected = normalize(recorded);
        assertThat(normalized).as("api-snapshots/%s.json", name).isEqualTo(expected);
    }

    private static String normalize(JsonNode node) throws Exception {
        return MAPPER.writeValueAsString(MAPPER.treeToValue(node, Object.class)) + "\n";
    }

    private static void stripVolatile(JsonNode node) {
        if (node.isObject()) {
            ((ObjectNode) node).remove(VOLATILE_FIELDS);
            // Dogrulama hatalari sirasiz bir kumeden gelir; alan adina gore siralanir.
            JsonNode fieldErrors = node.get("fieldErrors");
            if (fieldErrors != null && fieldErrors.isArray()) {
                List<JsonNode> sorted = new ArrayList<>();
                fieldErrors.forEach(sorted::add);
                sorted.sort(Comparator.comparing((JsonNode e) -> e.path("field").asText()).thenComparing(e -> e.path("message").asText()));
                ((ArrayNode) fieldErrors).removeAll().addAll(sorted);
            }
        }
        node.forEach(ApiSnapshots::stripVolatile);
    }
}
