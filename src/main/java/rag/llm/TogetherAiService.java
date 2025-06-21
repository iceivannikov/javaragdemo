package rag.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import rag.config.ConfigLoader;
import rag.config.TogetherAIConfig;

import java.io.IOException;

public class TogetherAiService implements LLMService {
    private final TogetherAIConfig config;
    private final OkHttpClient client;

    public TogetherAiService(TogetherAIConfig config, OkHttpClient client) {
        this.config = config;
        this.client = client;
    }

    @Override
    public String generate(String prompt) {
        try {
            String jsonBody = buildRequestJson(prompt);
            Request request = buildHttpRequest(jsonBody);
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                return parseResponse(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildRequestJson(String prompt) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestJson = mapper.createObjectNode();
        requestJson.put("model", config.model());
        ArrayNode messages = mapper.createArrayNode();
        ObjectNode userMessage = mapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        requestJson.set("messages", messages);
        return mapper.writeValueAsString(requestJson);
    }

    private Request buildHttpRequest(String jsonBody) {
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
        return new Request.Builder()
                .url(config.endpoint())
                .post(body)
                .addHeader("Authorization", "Bearer " + config.apiKey())
                .build();
    }

    private String parseResponse(Response response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String responseBody = response.body().string();
        JsonNode responseJson = mapper.readTree(responseBody);
        JsonNode contentJson = responseJson
                .path("choices")
                .path(0)
                .path("message")
                .path("content");
        if (contentJson.isMissingNode() || contentJson.isNull()) {
            throw new IOException("No content in LLM response: " + responseBody);
        }
        return contentJson.asText();
    }
}
