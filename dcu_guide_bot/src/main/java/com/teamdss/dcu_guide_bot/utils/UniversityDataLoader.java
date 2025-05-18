package com.teamdss.dcu_guide_bot.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import jakarta.annotation.PostConstruct; 

@Component
public class UniversityDataLoader { //로컬 데이터 로드
    
    private final Map<String, Object> universityData = new HashMap<>();
    
    @PostConstruct
    public void init() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        
        // data 폴더 내 모든 JSON 파일 로드
        loadData("classpath:data/*.json");
        // univDepartment 폴더 내 모든 JSON 파일 로드
        loadData("classpath:data/univDepartment/*.json");
    }
    
    private void loadData(String pathPattern) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(pathPattern);
        
        for (Resource resource : resources) {
            String filename = resource.getFilename().replace(".json", "");
            Object data = new ObjectMapper().readValue(resource.getInputStream(), Object.class);
            universityData.put(filename, data);
        }
    }
    
    public String buildSystemPrompt() {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 대학교 정보 안내 챗봇입니다. 다음 데이터를 참고해 답변하세요:\n\n");
        universityData.forEach((key, value) -> {
            prompt.append(String.format("# %s 데이터\n%s\n\n", key, new Gson().toJson(value)));
        });
        prompt.append("""
            [응답 규칙]
            1. 사용자 질문에 딱 맞는 정보만 제공
            2. 데이터에 없는 내용은 '모르겠습니다'라고 답변
            3. 전문 용어는 초보자도 이해할 수 있게 설명
            """);
        return prompt.toString();
    }
}
