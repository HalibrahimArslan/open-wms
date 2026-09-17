package com.hisarresearch.wms.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.annotation.PostConstruct;

@Service
public class WebSocketClientService {

    private WebSocketClient client;

    @Value("${websocket.uri}")
    private String websocketUri;

    @PostConstruct
    public void init() {
        try {
            connect();  // @Retryable tetiklenir
        } catch (Exception e) {
            System.out.println("❌ İlk bağlantı denemeleri başarısız oldu ama uygulama devam ediyor.");
        }
    }

    @Retryable(
        value = { Exception.class },
        maxAttempts = 5,
        backoff = @Backoff(delay = 5000) // 5 saniye bekle
    )
    public void connect() throws InterruptedException, URISyntaxException {
        client = new WebSocketClient(new URI(websocketUri)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("✅ WebSocket bağlantısı kuruldu.");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("📩 Gelen mesaj: " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("⚠️ Bağlantı kapandı: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("❌ Hata oluştu:");
                throw new RuntimeException("WebSocket bağlantı hatası", ex);
            }
        };
        client.connectBlocking();
    }

    @Recover
    public void recover(Exception e) {
        System.out.println("🚫 WebSocket bağlantısı 5 kez denenip başarısız oldu. Devam edilmeyecek.");
    }

    public void send(String message) {
        if (client != null && client.isOpen()) {
            client.send(message);
        } else {
            System.out.println("🔌 WebSocket kapalı. Mesaj gönderilemedi.");
        }
    }
}
