package rag.config;

import okhttp3.Authenticator;
import okhttp3.Credentials;

import java.net.InetSocketAddress;
import java.net.Proxy;

public record ProxyConfig(String host, String port, String username, String password) {
    public Proxy toJavaNetProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.host(), Integer.parseInt(this.port())));
    }

    public Authenticator toProxyAuthenticator() {
        return (route, response) -> {
            String credential = Credentials.basic(this.username(), this.password());
            return response.request().newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build();
        };
    }
}
