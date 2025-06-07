package com.example.controller;

import com.example.dto.ChatRequest;
import com.example.dto.ChatResponse;
import com.example.service.ChatGPTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatGPTController.class)
class ChatGPTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatGPTService chatGPTService;

    @Test
    void getChatResponse_shouldReturnExpectedResponse() throws Exception {
        ChatRequest chatRequest = new ChatRequest("Hello", "text-davinci-003", 0.7, 100);
        String serviceResponse = "Hi there!";

        when(chatGPTService.getChatResponse(any(ChatRequest.class))).thenReturn(serviceResponse);

        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(serviceResponse));
    }

    @Test
    void getChatResponse_whenPromptIsBlank_shouldReturnBadRequest() throws Exception {
        ChatRequest chatRequest = new ChatRequest("", "text-davinci-003", 0.7, 100);

        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isBadRequest());
                // More specific error message checking can be added here if GlobalExceptionHandler is configured for it
    }

     @Test
    void getChatResponse_whenRequestBodyIsEmpty_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}