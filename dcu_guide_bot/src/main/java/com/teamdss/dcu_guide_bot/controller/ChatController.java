package com.teamdss.dcu_guide_bot.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        try {
            String answer = geminiService.getAnswer(request.get("message"));
            return ResponseEntity.ok(Map.of("answer", answer));
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 스택트레이스 출력
            System.out.println("에러 발생: " + e.getMessage()); // 간단한 메시지도 함께 출력
            
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "챗봇 응답 생성 실패"));
        }
    }
}
