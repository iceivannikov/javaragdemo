package rag.search;

import okhttp3.*;
import rag.config.ApiConfig;
import rag.config.ProxyConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RapidApiSearchProvider implements SearchProvider {
    public static final String X_RAPIDAPI_KEY = "x-rapidapi-key";
    public static final String X_RAPIDAPI_HOST = "x-rapidapi-host";
    private final ApiConfig apiConfig;
    private final ProxyConfig proxyConfig;

    public RapidApiSearchProvider(ApiConfig apiConfig, ProxyConfig proxyConfig) {
        this.apiConfig = apiConfig;
        this.proxyConfig = proxyConfig;
    }

    @Override
    public String search(String query) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        proxyConfig.host(),
                        Integer.parseInt(proxyConfig.port()))))
                .proxyAuthenticator((route, response) -> {
                    String credential = Credentials.basic(proxyConfig.username(), proxyConfig.password());
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                })
                .build();
        query = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = String.format(apiConfig.endpoint(), query);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(X_RAPIDAPI_KEY, apiConfig.key())
                .addHeader(X_RAPIDAPI_HOST, apiConfig.host())
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
