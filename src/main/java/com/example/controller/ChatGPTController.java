package com.example.controller;

import com.example.service.ChatGPTService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping
    public String getChatResponse(@RequestBody String prompt) {
        return chatGPTService.getChatResponse(prompt);
    }
}