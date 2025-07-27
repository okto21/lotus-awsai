package com.lotus.lotusawsai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final BedrockRuntimeClient bedrockRuntimeClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.bedrock.models.claude-3-sonnet}")
    private String claudeModelId;

    @Value("${aws.bedrock.models.titan-text}")
    private String titanModelId;

    /**
     * Claude 3 Sonnet 모델을 사용하여 텍스트 생성
     */
    public String generateTextWithClaude(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("anthropic_version", "bedrock-2023-05-31");
            requestBody.put("max_tokens", 1000);
            requestBody.put("temperature", 0.7);
            requestBody.put("messages", new Object[]{
                    Map.of("role", "user", "content", prompt)
            });

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            SdkBytes body = SdkBytes.fromUtf8String(jsonBody);

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(claudeModelId)
                    .body(body)
                    .build();

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(request);
            String responseBody = response.body().asUtf8String();
            
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            String content = jsonResponse.path("content")
                    .path(0)
                    .path("text")
                    .asText();

            log.info("Claude response: {}", content);
            return content;

        } catch (Exception e) {
            log.error("Error calling Claude model", e);
            throw new RuntimeException("AI 모델 호출 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * Titan Text 모델을 사용하여 텍스트 생성
     */
    public String generateTextWithTitan(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputText", prompt);
            requestBody.put("textGenerationConfig", Map.of(
                    "maxTokenCount", 1000,
                    "temperature", 0.7,
                    "topP", 0.9
            ));

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            SdkBytes body = SdkBytes.fromUtf8String(jsonBody);

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(titanModelId)
                    .body(body)
                    .build();

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(request);
            String responseBody = response.body().asUtf8String();
            
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            String content = jsonResponse.path("results")
                    .path(0)
                    .path("outputText")
                    .asText();

            log.info("Titan response: {}", content);
            return content;

        } catch (Exception e) {
            log.error("Error calling Titan model", e);
            throw new RuntimeException("AI 모델 호출 중 오류가 발생했습니다.", e);
        }
    }
}