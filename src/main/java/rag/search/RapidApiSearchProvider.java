package rag.search;

import okhttp3.*;
import rag.config.ApiConfig;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RapidApiSearchProvider implements SearchProvider {
    private final ApiConfig apiConfig;
    private final OkHttpClient client;

    public RapidApiSearchProvider(ApiConfig apiConfig, OkHttpClient client) {
        this.apiConfig = apiConfig;
        this.client = client;
    }

    @Override
    public String search(String query) throws IOException {
        query = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = String.format(apiConfig.endpoint(), query);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", apiConfig.key())
                .addHeader("x-rapidapi-host", apiConfig.host())
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();
        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            String body = response.body() != null ? response.body().string() : "";
            System.out.println("Status code: " + statusCode);
            System.out.println("Response body: " + body);
            return body;
        }
    }
}
