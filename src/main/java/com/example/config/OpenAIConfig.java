package com.example.config;

import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenAIConfig {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.api-timeout:30s}") // Default timeout 30 seconds
    private Duration apiTimeout;

    @Bean
    public OpenAiService openAiService() {
        // The OpenAiService constructor that takes a client is available in newer versions.
        // For older versions, you might need to use a different approach or rely on system properties for timeouts if supported.
        // Assuming a version that supports custom OkHttpClient for timeout configuration.
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(apiTimeout)
                .readTimeout(apiTimeout)
                .writeTimeout(apiTimeout)
                .build();
        return new OpenAiService(apiKey, client);
        // If the constructor OpenAiService(apiKey, client) is not available,
        // and you are using a version like 0.16.0, it might use a default client.
        // In that case, timeouts are harder to configure directly via this bean.
        // You might need to rely on the library's default or explore if it picks up system properties.
        // For 0.16.0, it seems to use Retrofit which in turn uses OkHttp. Default timeouts apply.
        // return new OpenAiService(apiKey, apiTimeout); // This constructor might not exist in 0.16.0, check library docs for specific version
    }
}