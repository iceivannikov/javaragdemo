package rag.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class RapidApiSearchResultParser implements SearchResultParser {
    @Override
    public List<SearchResult> parse(String rawResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(rawResponse);
        JsonNode results = jsonNode.get("results");
        List<SearchResult> searchResults = new ArrayList<>();
        for (JsonNode result : results) {
            SearchResult searchResult = new SearchResult(
                    result.get("url").asText(""),
                    result.get("title").asText(""),
                    result.get("description").asText("")
            );
            searchResults.add(searchResult);
        }
        if (results.isEmpty() || !results.isArray()) {
            // Можно вывести лог или вернуть пустой список
            return List.of();
        }
        return searchResults;
    }
}
