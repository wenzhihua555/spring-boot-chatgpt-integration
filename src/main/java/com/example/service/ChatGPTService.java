package com.example.service;

import com.example.dto.ChatRequest;
import com.example.exception.OpenAIServiceException;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ChatGPTService {

    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

    private final OpenAiService openAiService;

    @Value("${openai.default-model:text-davinci-003}")
    private String defaultModel;

    @Value("${openai.default-temperature:0.7}")
    private Double defaultTemperature;

    @Value("${openai.max-tokens:150}")
    private Integer defaultMaxTokens;

    public ChatGPTService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public String getChatResponse(ChatRequest chatRequest) {
        String model = chatRequest.getModel() != null ? chatRequest.getModel() : defaultModel;
        Double temperature = chatRequest.getTemperature() != null ? chatRequest.getTemperature() : defaultTemperature;
        Integer maxTokens = chatRequest.getMaxTokens() != null ? chatRequest.getMaxTokens() : defaultMaxTokens;

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(chatRequest.getPrompt())
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .echo(false) // Don't echo back the prompt
                .build();
        try {
            logger.info("Sending request to OpenAI with model: {}, temperature: {}, maxTokens: {}", model, temperature, maxTokens);
            // Note: The com.theokanning.openai-gpt3-java library's createCompletion might not directly support setting a timeout per request in older versions.
            // If using a version that wraps a client like OkHttpClient, timeout would be configured there (see OpenAIConfig).
            return openAiService.createCompletion(completionRequest).getChoices().get(0).getText().trim();
        } catch (Exception e) {
            logger.error("Error while calling OpenAI API: {}", e.getMessage(), e);
            throw new OpenAIServiceException("Failed to get response from OpenAI: " + e.getMessage(), e);
        }
    }
}