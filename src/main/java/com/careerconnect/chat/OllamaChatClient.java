package com.careerconnect.chat;

import com.careerconnect.exception.ServiceUnavailableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class OllamaChatClient implements ChatClient {

    private static final Logger logger = LoggerFactory.getLogger(OllamaChatClient.class);

    private final String baseUrl;
    private final String model;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OllamaChatClient(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.model}") String model,
            @Value("${ollama.connect-timeout-seconds}") int connectTimeout,
            @Value("${ollama.read-timeout-seconds}") int readTimeout) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.objectMapper = new ObjectMapper();

        var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connectTimeout));
        factory.setReadTimeout(Duration.ofSeconds(readTimeout));
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public String chat(String systemPrompt, String userMessage) {
        String url = baseUrl + "/api/chat";

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)
                ),
                "stream", false
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            logger.info("Sending chat request to Ollama at {} using model {}", url, model);

            String response = restTemplate.postForObject(url, request, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode messageContent = root.path("message").path("content");

            if (messageContent.isMissingNode() || messageContent.asText().isBlank()) {
                logger.warn("Ollama returned an empty response");
                return "The assistant could not generate a response at this time. Please try again.";
            }

            return messageContent.asText();

        } catch (ResourceAccessException e) {
            logger.error("Could not connect to Ollama: {}", e.getMessage());
            throw new ServiceUnavailableException("Ollama service is not available. Please make sure Ollama is running on " + baseUrl);
        } catch (Exception e) {
            logger.error("Error communicating with Ollama: {}", e.getMessage());
            throw new ServiceUnavailableException("Failed to get response from Ollama: " + e.getMessage());
        }
    }

    public String getModel() {
        return model;
    }
}
