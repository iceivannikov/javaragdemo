package rag;

import okhttp3.OkHttpClient;
import rag.config.ApiConfig;
import rag.config.ConfigLoader;
import rag.config.ProxyConfig;
import rag.input.QuerySource;
import rag.input.TestQuerySource;
import rag.llm.LLMService;
import rag.llm.TogetherAiService;
import rag.parser.RapidApiSearchResultParser;
import rag.parser.SearchResult;
import rag.parser.SearchResultParser;
import rag.prompt.PromptBuilder;
import rag.prompt.SimplePromptBuilder;
import rag.search.RapidApiSearchProvider;
import rag.search.SearchProvider;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        QuerySource source = new TestQuerySource();
        String query = source.getQuery();
        ApiConfig apiConfig = ConfigLoader.loadApiConfig();
        ProxyConfig proxyConfig = ConfigLoader.loadProxyConfig();
        OkHttpClient client = ConfigLoader.buildOkHttpClient(proxyConfig);
        SearchProvider provider = new RapidApiSearchProvider(apiConfig, client);
        String search = provider.search(query);
//        System.out.println("RAW SEARCH RESPONSE: " + search);
//        if (search == null || search.isBlank()) {
//            System.out.println("Search response is empty! Проверь параметры запроса и ключи.");
//            return;
//        }
        SearchResultParser parser = new RapidApiSearchResultParser();
        List<SearchResult> parse = parser.parse(search);
        parse.forEach(System.out::println);
        PromptBuilder builder = new SimplePromptBuilder();
        String prompt = builder.buildPrompt(query, parse);
        LLMService service = new TogetherAiService();
        String generate = service.generate(prompt);
        System.out.println(generate);
    }
}