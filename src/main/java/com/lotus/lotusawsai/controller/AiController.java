package com.lotus.lotusawsai.controller;

import com.lotus.lotusawsai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * Claude 3 Sonnet 모델을 사용한 텍스트 생성
     */
    @PostMapping("/claude")
    public ResponseEntity<Map<String, String>> generateWithClaude(@RequestBody Map<String, String> request) {
        try {
            String prompt = request.get("prompt");
            if (prompt == null || prompt.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "프롬프트가 필요합니다."));
            }

            String response = aiService.generateTextWithClaude(prompt);
            return ResponseEntity.ok(Map.of("response", response));

        } catch (Exception e) {
            log.error("Claude API 호출 중 오류", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "AI 모델 호출 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * Titan Text 모델을 사용한 텍스트 생성
     */
    @PostMapping("/titan")
    public ResponseEntity<Map<String, String>> generateWithTitan(@RequestBody Map<String, String> request) {
        try {
            String prompt = request.get("prompt");
            if (prompt == null || prompt.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "프롬프트가 필요합니다."));
            }

            String response = aiService.generateTextWithTitan(prompt);
            return ResponseEntity.ok(Map.of("response", response));

        } catch (Exception e) {
            log.error("Titan API 호출 중 오류", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "AI 모델 호출 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 건강 상태 확인
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "healthy", "service", "lotus-awsai"));
    }
}