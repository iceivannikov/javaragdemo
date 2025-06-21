package rag.search;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rag.config.ApiConfig;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RapidApiSearchProviderTest {
    @Test
    void whenQueryIsValidThenReturnsExpectedResponse() throws IOException {
        ApiConfig apiConfig = Mockito.mock(ApiConfig.class);
        Mockito.when(apiConfig.endpoint()).thenReturn("https://testapi.com/search?q=%s");
        Mockito.when(apiConfig.key()).thenReturn("test-key");
        Mockito.when(apiConfig.host()).thenReturn("test-host");
        OkHttpClient mockClient = Mockito.mock(OkHttpClient.class);
        Call mockCall = Mockito.mock(Call.class);
        ResponseBody mockBody = ResponseBody.create("mocked response", MediaType.parse("application/json"));
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://testapi.com/search?q=test").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(mockBody)
                .build();
        Mockito.when(mockClient.newCall(Mockito.any(Request.class))).thenReturn(mockCall);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        RapidApiSearchProvider provider = new RapidApiSearchProvider(apiConfig, mockClient);
        String result = provider.search("test");
        assertThat(result).isEqualTo("mocked response");
    }

    @Test
    void whenResponseBodyIsNullThenReturnsEmptyString() throws IOException {
        ApiConfig apiConfig = Mockito.mock(ApiConfig.class);
        Mockito.when(apiConfig.endpoint()).thenReturn("https://testapi.com/search?q=%s");
        Mockito.when(apiConfig.key()).thenReturn("test-key");
        Mockito.when(apiConfig.host()).thenReturn("test-host");
        OkHttpClient mockClient = Mockito.mock(OkHttpClient.class);
        Call mockCall = Mockito.mock(Call.class);
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://testapi.com/search?q=test").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(new byte[0], MediaType.parse("application/json")))
                .build();
        Mockito.when(mockClient.newCall(Mockito.any(Request.class))).thenReturn(mockCall);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        RapidApiSearchProvider provider = new RapidApiSearchProvider(apiConfig, mockClient);
        String result = provider.search("test");
        assertThat(result).isEmpty();
    }

    @Test
    void whenCallThrowsIOExceptionThenExceptionIsPropagated() throws IOException {
        ApiConfig apiConfig = Mockito.mock(ApiConfig.class);
        Mockito.when(apiConfig.endpoint()).thenReturn("https://testapi.com/search?q=%s");
        Mockito.when(apiConfig.key()).thenReturn("test-key");
        Mockito.when(apiConfig.host()).thenReturn("test-host");
        OkHttpClient mockClient = Mockito.mock(OkHttpClient.class);
        Call mockCall = Mockito.mock(Call.class);
        Mockito.when(mockClient.newCall(Mockito.any(Request.class))).thenReturn(mockCall);
        Mockito.when(mockCall.execute()).thenThrow(new IOException("Test IO exception"));
        RapidApiSearchProvider provider = new RapidApiSearchProvider(apiConfig, mockClient);
        assertThrows(
                IOException.class,
                () -> provider.search("test")
        );
    }

    @Test
    void whenResponseStatusIsNot200ThenBodyStillReturned() throws IOException {
        ApiConfig apiConfig = Mockito.mock(ApiConfig.class);
        Mockito.when(apiConfig.endpoint()).thenReturn("https://testapi.com/search?q=%s");
        Mockito.when(apiConfig.key()).thenReturn("test-key");
        Mockito.when(apiConfig.host()).thenReturn("test-host");
        OkHttpClient mockClient = Mockito.mock(OkHttpClient.class);
        Call mockCall = Mockito.mock(Call.class);
        ResponseBody mockBody = ResponseBody.create("error page", MediaType.parse("application/json"));
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://testapi.com/search?q=test").build())
                .protocol(Protocol.HTTP_1_1)
                .code(404)
                .message("Not Found")
                .body(mockBody)
                .build();
        Mockito.when(mockClient.newCall(Mockito.any(Request.class))).thenReturn(mockCall);
        Mockito.when(mockCall.execute()).thenReturn(mockResponse);
        RapidApiSearchProvider provider = new RapidApiSearchProvider(apiConfig, mockClient);
        String result = provider.search("test");
        assertThat(result).isEqualTo("error page");
    }
}