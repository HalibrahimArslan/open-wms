package com.hisarresearch.wms.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * {@code aur_company.api_parameters} icindeki ERP sifresini DB'ye yazmadan once
 * sifreler, okurken cozer (AES/GCM, geri donduruleblir). Tek yonlu hash (User
 * girisinde oldugu gibi bcrypt) burada kullanilamaz: ErpTokenService bu sifreyi
 * ERP'ye acik metin olarak gondermek zorunda, yani duz metni geri elde etmemiz
 * gerekiyor.
 *
 * <p>Ayri bir secret uretip yeni bir env var eklemek yerine JHipster'in zaten
 * var olan {@code jhipster.security.authentication.jwt.base64-secret} degeri
 * kullanilir (prod'da {@code JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET}
 * ile override edilir). Bu deger AES anahtari icin dogru uzunlukta olmadigindan
 * (64 byte), SHA-256 ile 256 bit'lik bir AES anahtarina indirgenir.
 *
 * <p>Sifreleme {@code AurCompanyResource} kaydederken, cozme {@code ErpTokenService}
 * token isterken yapilir; entity ve DTO'lar sifreli degeri tasir. Statik metotlar
 * Spring bean'i olmayan yerlerden de cagrilabilsin diye anahtar static tutulur ve
 * bu bean ayaga kalkarken doldurulur.
 */
@Component
public class ApiPasswordCipher {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int GCM_IV_LENGTH_BYTES = 12;

    private static volatile SecretKey secretKey;

    public ApiPasswordCipher(@Value("${jhipster.security.authentication.jwt.base64-secret:}") String jwtBase64Secret) {
        secretKey = StringUtils.hasText(jwtBase64Secret) ? deriveAesKey(jwtBase64Secret) : null;
    }

    private static SecretKey deriveAesKey(String jwtBase64Secret) {
        try {
            byte[] jwtSecretBytes = Base64.getDecoder().decode(jwtBase64Secret);
            byte[] aesKeyBytes = MessageDigest.getInstance("SHA-256").digest(jwtSecretBytes);
            return new SecretKeySpec(aesKeyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("AES anahtari turetilemedi", e);
        }
    }

    /**
     * Anahtar tanimli degilse (orn. lokal gelistirme) sifreleme atlanir ve deger
     * oldugu gibi dondurulur; boylece {@code WMS_API_PASSWORD_SECRET} verilmeden
     * de uygulama calisir.
     */
    public static String encrypt(String plainText) {
        SecretKey key = secretKey;
        if (plainText == null || key == null) {
            return plainText;
        }
        try {
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("apiParameters.password sifrelenemedi", e);
        }
    }

    public static String decrypt(String cipherTextBase64) {
        SecretKey key = secretKey;
        if (cipherTextBase64 == null || key == null) {
            return cipherTextBase64;
        }
        try {
            byte[] combined = Base64.getDecoder().decode(cipherTextBase64);
            byte[] iv = new byte[GCM_IV_LENGTH_BYTES];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv));
            byte[] plain = cipher.doFinal(combined, iv.length, combined.length - iv.length);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("apiParameters.password cozulemedi", e);
        }
    }
}
