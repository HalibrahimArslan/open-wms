package com.hisarresearch.wms.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.net.URLEncoder;
import java.util.Set;

import static java.awt.SystemColor.text;


@Service
public class MetabaseService {
    private final Logger log = LoggerFactory.getLogger(MetabaseService.class);

    @Value("${metabase.secretKey}")
    private String secret;

    @Value("${metabase.url}")
    private String metabaseUrl;

    @Value("${metabase.dashboardCode}")
    private String metabaseDashboardCode;

    public MetabaseService() {
    }

    public String generateToken(String warehouseName) {
        byte[] keyBytes;
        keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        long now = System.currentTimeMillis() / 1000;
        //long expirationTime = now + (60 * 60);

        Map<String, Object> params = new HashMap<>();
        params.put("%C5%9Fube_ad%C4%B1", warehouseName);

        Map<String, Object> payload = new HashMap<>();
        payload.put("resource", Map.of("dashboard",Integer.parseInt(metabaseDashboardCode)));
        payload.put("params", params);
       // payload.put("exp", expirationTime);

        String token = Jwts.builder()
            .setClaims(payload)
            .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
            .compact();

        return metabaseUrl.concat("/embed/dashboard/").concat(token).concat("#bordered=false&titled=false");
    }

    public String generateMatabaseUrl(Map<String, String> params)  {

        Map<String, Object> payload = new HashMap<>();
        payload.put("resource", Map.of("dashboard", Integer.parseInt(metabaseDashboardCode)));
        payload.put("params", getStringObjectMap(params));

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        String token = Jwts.builder()
            .setClaims(payload)
            .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
            .compact();

        return metabaseUrl.concat("/embed/dashboard/").concat(token).concat("#bordered=false&titled=false");

    }

    private @NotNull Map<String, Object> getStringObjectMap(Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        Set<String> keySet = params.keySet();
        keySet.forEach(key -> {
            try {
                String encodedKey = key.replace("params[", "").replace("]","");
                encodedKey = URLEncoder.encode(encodedKey, StandardCharsets.UTF_8);
                encodedKey = encodedKey.replace("+", "_");
                result.put(encodedKey, params.get(key));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return result;
    }


}
