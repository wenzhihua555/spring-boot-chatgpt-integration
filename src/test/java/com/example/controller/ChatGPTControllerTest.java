package com.example.controller;

import com.example.service.ChatGPTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatGPTController.class)
class ChatGPTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatGPTService chatGPTService;

    @Test
    void getChatResponse_shouldReturnExpectedResponse() throws Exception {
        String prompt = "Hello";
        String expectedResponse = "Hi there!";

        when(chatGPTService.getChatResponse(prompt)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"" + prompt + "\"")) // Send prompt as JSON string
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}