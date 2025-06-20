package rag.prompt;

import rag.parser.SearchResult;

import java.util.List;

public class SimplePromptBuilder implements PromptBuilder {
    @Override
    public String buildPrompt(String userQuery, List<SearchResult> results) {
        return "";
    }
}
