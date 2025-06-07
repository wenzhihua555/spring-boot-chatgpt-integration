package com.example.controller;

import com.example.dto.ChatRequest;
import com.example.dto.ChatResponse;
import com.example.service.ChatGPTService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping
    public ChatResponse getChatResponse(@Valid @RequestBody ChatRequest chatRequest) {
        String reply = chatGPTService.getChatResponse(chatRequest);
        return new ChatResponse(reply);
    }
}