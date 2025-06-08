package com.teamdss.dcu_guide_bot.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamdss.dcu_guide_bot.service.GeminiService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiService geminiService;

    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
    

    @PostMapping
    public ResponseEntity<?> chat(
        @RequestBody Map<String, String> request,
        @CookieValue(value = "sessionId", required = false) String sessionIdFromCookie
    ) {
        String sessionId = sessionIdFromCookie;
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
        String message = request.get("message");
        try {
            String answer = geminiService.getAnswer(sessionId, message);
            // Set-Cookie로 sessionId 내려주기
            ResponseCookie cookie = ResponseCookie.from("sessionId", sessionId)
                .path("/")
                .httpOnly(true)
                .build();
            return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                    "sessionId", sessionId,
                    "answer", answer
                ));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "챗봇 응답 생성 실패"));
        }
    }


}

