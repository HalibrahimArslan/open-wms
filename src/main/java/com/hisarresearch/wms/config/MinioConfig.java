package com.hisarresearch.wms.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Obje deposu istege baglidir: {@code minio.url} bos birakilirsa istemci hic kurulmaz
 * ve uygulama MinIO olmadan acilir. Dosya islemleri o durumda
 * {@link com.hisarresearch.wms.service.MinioService} icinden anlasilir bir hata dondurur.
 */
@Configuration
public class MinioConfig {

    @Value("${minio.url:}")
    private String url;

    @Value("${minio.accessKey:}")
    private String accessKey;

    @Value("${minio.secretKey:}")
    private String secretKey;

    @Bean
    @ConditionalOnExpression("!'${minio.url:}'.isBlank()")
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build();
    }
}
