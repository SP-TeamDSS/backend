package com.teamdss.dcu_guide_bot.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.teamdss.dcu_guide_bot.utils.UniversityDataLoader;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;
    
    private final UniversityDataLoader dataLoader;
    private final RestClient restClient;

    public GeminiService(UniversityDataLoader dataLoader) {
        this.dataLoader = dataLoader;
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models")
                .build();
    }

    public String getAnswer(String question) throws IOException {
        String systemPrompt = dataLoader.buildSystemPrompt();
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(
            Map.of("role", "user", 
                "parts", List.of(
                    Map.of("text", systemPrompt),
                    Map.of("text", question)
                )
            )
        ));

        // Gemini API 호출 및 응답 파싱
        Map response = restClient.post()
        .uri("/gemini-pro:generateContent?key=" + apiKey)
        .body(requestBody)
        .retrieve()
        .body(Map.class);

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

