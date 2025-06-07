package com.example.service;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    private final OpenAiService openAiService;

    public ChatGPTService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public String getChatResponse(String prompt) {
        return openAiService.createCompletion(prompt)
                .getChoices().get(0).getText();
    }
}