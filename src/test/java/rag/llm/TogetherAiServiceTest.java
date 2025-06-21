package rag.llm;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rag.config.TogetherAIConfig;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class TogetherAiServiceTest {
    @Test
    void whenResponseValidThenReturnsContent() throws IOException {
        TogetherAIConfig config = new TogetherAIConfig(
                "deepseek-ai/DeepSeek-V3",
                "fake-key",
                "https://api.together.xyz/v1/chat/completions"
        );
        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);
        String answerText = "Hello from LLM!";
        String jsonResponse = """
        {
          "choices": [
            {
              "message": {
                "role": "assistant",
                "content": "%s"
              }
            }
          ]
        }
        """.formatted(answerText);
        ResponseBody responseBody = ResponseBody.create(
                jsonResponse, MediaType.parse("application/json")
        );
        Response response = new Response.Builder()
                .request(new Request.Builder().url(config.endpoint()).build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();
        Mockito.when(client.newCall(Mockito.any(Request.class))).thenReturn(call);
        Mockito.when(call.execute()).thenReturn(response);
        TogetherAiService service = new TogetherAiService(config, client);
        String result = service.generate("Hi!");
        assertThat(result).isEqualTo(answerText);
    }

    @Test
    void whenResponseNotSuccessfulThenThrowsException() throws IOException {
        TogetherAIConfig config = new TogetherAIConfig(
                "deepseek-ai/DeepSeek-V3",
                "fake-key",
                "https://api.together.xyz/v1/chat/completions"
        );
        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);
        Response response = new Response.Builder()
                .request(new Request.Builder().url(config.endpoint()).build())
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .body(ResponseBody.create("", MediaType.parse("application/json")))
                .build();
        Mockito.when(client.newCall(Mockito.any(Request.class))).thenReturn(call);
        Mockito.when(call.execute()).thenReturn(response);
        TogetherAiService service = new TogetherAiService(config, client);
        assertThatThrownBy(() -> service.generate("Hi!"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unexpected code");
    }

    @Test
    void whenResponseWithoutContentThenThrowsException() throws IOException {
        TogetherAIConfig config = new TogetherAIConfig(
                "deepseek-ai/DeepSeek-V3",
                "fake-key",
                "https://api.together.xyz/v1/chat/completions"
        );
        OkHttpClient client = Mockito.mock(OkHttpClient.class);
        Call call = Mockito.mock(Call.class);
        String jsonResponse = """
        {
          "choices": [
            {
              "message": {
                "role": "assistant"
              }
            }
          ]
        }
        """;
        ResponseBody responseBody = ResponseBody.create(jsonResponse, MediaType.parse("application/json"));
        Response response = new Response.Builder()
                .request(new Request.Builder().url(config.endpoint()).build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();
        Mockito.when(client.newCall(Mockito.any(Request.class))).thenReturn(call);
        Mockito.when(call.execute()).thenReturn(response);
        TogetherAiService service = new TogetherAiService(config, client);
        assertThatThrownBy(() -> service.generate("Hi!"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No content in LLM response");
    }
}