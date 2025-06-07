package com.example.service;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ChatGPTServiceTest {

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private ChatGPTService chatGPTService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChatResponse_shouldReturnExpectedResponse() {
        String prompt = "Hello";
        String expectedResponse = "Hi there!";

        CompletionChoice choice = new CompletionChoice();
        choice.setText(expectedResponse);
        CompletionResult completionResult = new CompletionResult();
        completionResult.setChoices(Collections.singletonList(choice));

        when(openAiService.createCompletion(anyString())).thenReturn(completionResult);

        String actualResponse = chatGPTService.getChatResponse(prompt);

        assertEquals(expectedResponse, actualResponse);
    }
}