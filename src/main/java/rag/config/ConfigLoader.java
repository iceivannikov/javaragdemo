package rag.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    private ConfigLoader(Properties properties) {
    }
}
