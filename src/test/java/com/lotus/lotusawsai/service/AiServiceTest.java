package com.lotus.lotusawsai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AiServiceTest {

    @Mock
    private BedrockRuntimeClient bedrockRuntimeClient;

    @Mock
    private ObjectMapper objectMapper;

    private AiService aiService;

    @BeforeEach
    void setUp() {
        aiService = new AiService(bedrockRuntimeClient, objectMapper);
    }

    @Test
    void aiService_ShouldBeCreated() {
        assertNotNull(aiService);
    }

    @Test
    void generateTextWithClaude_ShouldThrowException_WhenNoCredentials() {
        assertThrows(RuntimeException.class, () -> {
            aiService.generateTextWithClaude("test prompt");
        });
    }

    @Test
    void generateTextWithTitan_ShouldThrowException_WhenNoCredentials() {
        assertThrows(RuntimeException.class, () -> {
            aiService.generateTextWithTitan("test prompt");
        });
    }
}