package com.hisarresearch.wms.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Uygulama acilirken yapilandirilan bucket'in var olup olmadigina bakar, yoksa olusturur.
 * Boylece obje deposunu hazirlamak icin ayrica bir kurulum adimi (mc mb) gerekmez.
 * Depo o an erisilemezse acilis engellenmez; sadece uyari basilir, dosya yukleme
 * istekleri kendi hatasini dondurur.
 */
@Component
public class MinioBucketInitializer implements ApplicationRunner {

    private final Logger log = LoggerFactory.getLogger(MinioBucketInitializer.class);

    private final MinioClient minioClient;

    @Value("${minio.bucketName:}")
    private String bucketName;

    public MinioBucketInitializer(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!StringUtils.hasText(bucketName)) {
            log.warn("minio.bucketName tanimli degil, bucket kontrolu atlandi");
            return;
        }
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (exists) {
                log.debug("Minio bucket mevcut: {}", bucketName);
                return;
            }
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Minio bucket olusturuldu: {}", bucketName);
        } catch (Exception e) {
            log.warn("Minio bucket hazirlanamadi ({}): {}", bucketName, e.getMessage());
        }
    }
}
