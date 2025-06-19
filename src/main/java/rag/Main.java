package rag;

import rag.input.ConsoleQuerySource;
import rag.input.QuerySource;
import rag.llm.LLMService;
import rag.llm.TogetherAiService;
import rag.parser.RapidApiSearchResultParser;
import rag.parser.SearchResultParser;
import rag.prompt.PromptBuilder;
import rag.prompt.SimplePromptBuilder;
import rag.search.RapidApiSearchProvider;
import rag.search.SearchProvider;
import rag.search.SearchService;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        QuerySource source = new ConsoleQuerySource();
        String query = source.getQuery();
        SearchProvider provider = new RapidApiSearchProvider();
        String search = provider.search("");
        SearchResultParser parser = new RapidApiSearchResultParser();
        List<SearchResult> parse = parser.parse(search);
        PromptBuilder builder = new SimplePromptBuilder();
        String prompt = builder.buildPrompt(query, parse);
        LLMService service = new TogetherAiService();
        String generate = service.generate(prompt);
        System.out.println(generate);

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://google-web-search1.p.rapidapi.com/?query=World%20Cup&limit=20&related_keywords=true"))
//                .header("x-rapidapi-key", "440c96542bmsh233d489c035f700p17793bjsndfa3c6b2fc99")
//                .header("x-rapidapi-host", "google-web-search1.p.rapidapi.com")
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
}