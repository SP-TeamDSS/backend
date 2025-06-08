package com.teamdss.dcu_guide_bot;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.teamdss.dcu_guide_bot.service.GeminiService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestConfig.class)
class ChatIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private GeminiService geminiService;

     @Test
    void chatApi_returnsAnswer() throws Exception {
        doReturn("통합 테스트 답변")
            .when(geminiService)
            .getAnswer(anyString(), anyString());

        // When & Then
        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"질문\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answer").value("통합 테스트 답변"));
    }
}
