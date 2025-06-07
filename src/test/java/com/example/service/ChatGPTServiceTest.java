package com.example.service;

import com.example.dto.ChatRequest;
import com.example.exception.OpenAIServiceException;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatGPTServiceTest {

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private ChatGPTService chatGPTService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set default values for testing, as @Value might not be injected in unit tests without Spring context
        ReflectionTestUtils.setField(chatGPTService, "defaultModel", "text-davinci-003");
        ReflectionTestUtils.setField(chatGPTService, "defaultTemperature", 0.7);
        ReflectionTestUtils.setField(chatGPTService, "defaultMaxTokens", 150);
    }

    @Test
    void getChatResponse_shouldReturnExpectedResponse_withDefaultParams() {
        ChatRequest chatRequest = new ChatRequest("Hello", null, null, null);
        String expectedResponse = "Hi there!";

        CompletionChoice choice = new CompletionChoice();
        choice.setText(expectedResponse);
        CompletionResult completionResult = new CompletionResult();
        completionResult.setChoices(Collections.singletonList(choice));

        ArgumentCaptor<CompletionRequest> captor = ArgumentCaptor.forClass(CompletionRequest.class);
        when(openAiService.createCompletion(captor.capture())).thenReturn(completionResult);

        String actualResponse = chatGPTService.getChatResponse(chatRequest);

        assertEquals(expectedResponse, actualResponse);
        assertEquals("text-davinci-003", captor.getValue().getModel());
        assertEquals(0.7, captor.getValue().getTemperature());
        assertEquals(150, captor.getValue().getMaxTokens());
    }

    @Test
    void getChatResponse_shouldUseRequestParamsWhenProvided() {
        ChatRequest chatRequest = new ChatRequest("Test prompt", "gpt-4", 0.5, 100);
        String expectedResponse = "GPT-4 response";

        CompletionChoice choice = new CompletionChoice();
        choice.setText(expectedResponse);
        CompletionResult completionResult = new CompletionResult();
        completionResult.setChoices(Collections.singletonList(choice));

        ArgumentCaptor<CompletionRequest> captor = ArgumentCaptor.forClass(CompletionRequest.class);
        when(openAiService.createCompletion(captor.capture())).thenReturn(completionResult);

        String actualResponse = chatGPTService.getChatResponse(chatRequest);

        assertEquals(expectedResponse, actualResponse);
        assertEquals("gpt-4", captor.getValue().getModel());
        assertEquals(0.5, captor.getValue().getTemperature());
        assertEquals(100, captor.getValue().getMaxTokens());
    }

    @Test
    void getChatResponse_shouldThrowOpenAIServiceException_whenApiCallFails() {
        ChatRequest chatRequest = new ChatRequest("Hello", null, null, null);
        when(openAiService.createCompletion(any(CompletionRequest.class))).thenThrow(new RuntimeException("API Error"));

        OpenAIServiceException exception = assertThrows(OpenAIServiceException.class, () -> {
            chatGPTService.getChatResponse(chatRequest);
        });
        assertEquals("Failed to get response from OpenAI: API Error", exception.getMessage());
    }
}