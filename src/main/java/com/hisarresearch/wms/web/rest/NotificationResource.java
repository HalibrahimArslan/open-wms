package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.service.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notify")
public class NotificationResource {

    @Autowired
    private WebSocketClientService webSocketClientService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        webSocketClientService.send(message);
        return ResponseEntity.ok("Mesaj WebSocket ile gönderildi");
    }
}

