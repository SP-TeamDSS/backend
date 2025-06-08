package com.teamdss.dcu_guide_bot;
import java.io.IOException;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.teamdss.dcu_guide_bot.service.ChatHistoryService;
import com.teamdss.dcu_guide_bot.service.GeminiService;
import com.teamdss.dcu_guide_bot.utils.UniversityDataLoader;

@TestConfiguration
public class TestConfig {

    @Bean
    public UniversityDataLoader universityDataLoader() throws IOException {
        return new UniversityDataLoader();
    }

    @Bean
    public ChatHistoryService chatHistoryService() {
        return new ChatHistoryService();
    }

    @Bean
    public GeminiService geminiService() throws IOException {
        return new GeminiService(
            universityDataLoader(),
            chatHistoryService()
        );
    }
}
