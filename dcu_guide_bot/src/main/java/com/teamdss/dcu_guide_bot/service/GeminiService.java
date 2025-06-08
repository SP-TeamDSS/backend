package com.teamdss.dcu_guide_bot.service;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.teamdss.dcu_guide_bot.model.Message;
import com.teamdss.dcu_guide_bot.utils.UniversityDataLoader;

@Service
public class GeminiService {

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.model}")
    private String model;

    private final UniversityDataLoader dataLoader;
    private final RestClient restClient;
    private final ChatHistoryService chatHistoryService;

    public GeminiService(UniversityDataLoader dataLoader, ChatHistoryService chatHistoryService) {
        this.dataLoader = dataLoader;
        this.chatHistoryService = chatHistoryService;
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    public String getAnswer(String sessionId, String question) throws IOException {
        String systemPrompt = dataLoader.buildSystemPrompt();
        List<Message> history = chatHistoryService.getHistory(sessionId);
    
        List<Map<String, Object>> contents = new ArrayList<>();
    
        // 첫 요청 시: 시스템 프롬프트+질문을 하나의 메시지로
        if (history.isEmpty()) {
            Map<String, Object> initialMessage = new HashMap<>();
            initialMessage.put("role", "user");
            initialMessage.put("parts", List.of(
                Map.of("text", systemPrompt),
                Map.of("text", question)
            ));
            contents.add(initialMessage);
        } else {
            // 이후 요청: 히스토리 추가
            for (Message msg : history) {
                contents.add(Map.of(
                    "role", msg.getRole(),
                    "parts", List.of(Map.of("text", msg.getText()))
                ));
            }
            // 새 질문 추가
            contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", question))
            ));
        }
    
        // API 호출
        Map<String, Object> requestBody = Map.of("contents", contents);
    
        Map response = restClient.post()
            .uri("/models/" + model + ":generateContent?key=" + apiKey)
            .body(requestBody)
            .retrieve()
            .body(Map.class);
    
        // 응답 파싱
        String answer = extractAnswer(response);
    
        // 히스토리 저장 (질문/답변만 저장)
        chatHistoryService.addMessage(sessionId, new Message("user", question));
        chatHistoryService.addMessage(sessionId, new Message("model", answer));
    
        return answer;
    }
    



    // 응답에서 텍스트 추출하는 메서드
    private String extractAnswer(Map<String, Object> response) {
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates == null || candidates.isEmpty()) return "답변 없음";

        Map<String, Object> candidate = candidates.get(0);
        Map<String, Object> content = (Map<String, Object>) candidate.get("content");
        if (content == null) return "답변 없음";

        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        if (parts == null || parts.isEmpty()) return "답변 없음";

        Map<String, Object> part = parts.get(0);
        String text = (String) part.get("text");
        return text != null ? text : "답변 없음";
    }

}
    
