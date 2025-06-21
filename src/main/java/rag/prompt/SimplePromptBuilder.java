package rag.prompt;

import rag.parser.SearchResult;

import java.util.List;

public class SimplePromptBuilder implements PromptBuilder {
    @Override
    public String buildPrompt(String userQuery, List<SearchResult> results) {
        StringBuilder sb = new StringBuilder();
        if (results.isEmpty()) {
            sb.append("Нет результатов поиска").append("\n");
            return sb.toString();
        }
        sb.append("Запрос: ").append(userQuery).append("\n");
        sb.append("Результаты поиска:").append("\n\n");
        for (int i = 0; i < results.size(); i++) {
            SearchResult sr = results.get(i);
            sb.append(i + 1).append(". ").append(sr.title()).append("\n")
                    .append("URL: ").append(sr.url()).append("\n")
                    .append("Описание: ").append(sr.description()).append("\n\n");
        }
        return sb.toString();
    }
}
