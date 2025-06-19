package rag.parser;

import javax.naming.directory.SearchResult;
import java.util.List;

public class RapidApiSearchResultParser implements SearchResultParser {
    @Override
    public List<SearchResult> parse(String rawResponse) {
        return List.of();
    }
}
