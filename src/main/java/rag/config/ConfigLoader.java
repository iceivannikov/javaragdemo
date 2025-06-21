package rag.config;

import okhttp3.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ConfigLoader {
    private static final Properties PROPERTIES = new Properties();

    public static ApiConfig loadApiConfig() {
        ApiConfig apiConfig;
        try (InputStream inputStream = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")
        ) {
            PROPERTIES.load(inputStream);
            apiConfig = new ApiConfig(
                    PROPERTIES.getProperty("rapidapi.key"),
                    PROPERTIES.getProperty("rapidapi.host"),
                    PROPERTIES.getProperty("rapidapi.endpoint")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apiConfig;
    }

    public static ProxyConfig loadProxyConfig() {
        ProxyConfig proxyConfig;
        try (InputStream inputStream = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")
        ) {
            PROPERTIES.load(inputStream);
            proxyConfig = new ProxyConfig(
                    PROPERTIES.getProperty("proxy.host"),
                    PROPERTIES.getProperty("proxy.port"),
                    PROPERTIES.getProperty("proxy.username"),
                    PROPERTIES.getProperty("proxy.password")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return proxyConfig;
    }

    public static OkHttpClient buildOkHttpClient(ProxyConfig proxyConfig) {
        return new OkHttpClient.Builder()
                .proxy(proxyConfig.toJavaNetProxy())
                .proxyAuthenticator(proxyConfig.toProxyAuthenticator())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    public static TogetherAIConfig loadTogetherAIConfig() {
        TogetherAIConfig togetherAIConfig;
        try (InputStream inputStream = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")
        ) {
            PROPERTIES.load(inputStream);
            togetherAIConfig = new TogetherAIConfig(
                    PROPERTIES.getProperty("together.ai.model"),
                    PROPERTIES.getProperty("together.ai.key"),
                    PROPERTIES.getProperty("together.ai.endpoint")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return togetherAIConfig;
    }

    private ConfigLoader(Properties properties) {
    }
}
