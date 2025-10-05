package com.rex.guessthenumberab.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/callback2")
public class WebhookController {
    @Value("${line.bot.channel-secret}")
    private String channelSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("X-Line-Signature") String signature) {
        log.info("收到 LINE Webhook 請求！");
        log.info("Signature: {}", signature);
        log.info("Payload: {}", payload);

        // 這裡可以做 signature 驗證與事件解析處理

        return ResponseEntity.ok("OK");
    }

    public boolean verifySignature(String channelSecret, String payload, String xLineSignature) {
        try {
            SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] source = payload.getBytes(StandardCharsets.UTF_8);
            String signature = Base64.getEncoder().encodeToString(mac.doFinal(source));
            return signature.equals(xLineSignature);
        } catch (Exception e) {
            return false;
        }
    }
}
