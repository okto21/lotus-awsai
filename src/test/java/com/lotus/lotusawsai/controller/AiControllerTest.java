package com.lotus.lotusawsai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotus.lotusawsai.service.AiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiService aiService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void health_ShouldReturnHealthyStatus() throws Exception {
        mockMvc.perform(get("/api/ai/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("healthy"))
                .andExpect(jsonPath("$.service").value("lotus-awsai"));
    }

    @Test
    void generateWithClaude_Success() throws Exception {
        // Given
        String prompt = "Hello, how are you?";
        String expectedResponse = "I'm doing well, thank you for asking!";
        
        Map<String, String> request = Map.of("prompt", prompt);
        
        when(aiService.generateTextWithClaude(prompt))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/ai/claude")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse));
    }

    @Test
    void generateWithClaude_EmptyPrompt_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> request = Map.of("prompt", "");

        // When & Then
        mockMvc.perform(post("/api/ai/claude")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("프롬프트가 필요합니다."));
    }

    @Test
    void generateWithClaude_MissingPrompt_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> request = Map.of("other", "value");

        // When & Then
        mockMvc.perform(post("/api/ai/claude")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("프롬프트가 필요합니다."));
    }

    @Test
    void generateWithClaude_ServiceException_ShouldReturnInternalServerError() throws Exception {
        // Given
        String prompt = "Hello, how are you?";
        Map<String, String> request = Map.of("prompt", prompt);
        
        when(aiService.generateTextWithClaude(anyString()))
                .thenThrow(new RuntimeException("AI 모델 호출 중 오류가 발생했습니다."));

        // When & Then
        mockMvc.perform(post("/api/ai/claude")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("AI 모델 호출 중 오류가 발생했습니다: AI 모델 호출 중 오류가 발생했습니다."));
    }

    @Test
    void generateWithTitan_Success() throws Exception {
        // Given
        String prompt = "Hello, how are you?";
        String expectedResponse = "I'm doing well, thank you for asking!";
        
        Map<String, String> request = Map.of("prompt", prompt);
        
        when(aiService.generateTextWithTitan(prompt))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/ai/titan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse));
    }

    @Test
    void generateWithTitan_EmptyPrompt_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> request = Map.of("prompt", "");

        // When & Then
        mockMvc.perform(post("/api/ai/titan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("프롬프트가 필요합니다."));
    }

    @Test
    void generateWithTitan_ServiceException_ShouldReturnInternalServerError() throws Exception {
        // Given
        String prompt = "Hello, how are you?";
        Map<String, String> request = Map.of("prompt", prompt);
        
        when(aiService.generateTextWithTitan(anyString()))
                .thenThrow(new RuntimeException("AI 모델 호출 중 오류가 발생했습니다."));

        // When & Then
        mockMvc.perform(post("/api/ai/titan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("AI 모델 호출 중 오류가 발생했습니다: AI 모델 호출 중 오류가 발생했습니다."));
    }
}