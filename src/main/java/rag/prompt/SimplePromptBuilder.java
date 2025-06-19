package rag.prompt;

import javax.naming.directory.SearchResult;
import java.util.List;

public class SimplePromptBuilder implements PromptBuilder {
    @Override
    public String buildPrompt(String userQuery, List<SearchResult> results) {
        return "";
    }
}
