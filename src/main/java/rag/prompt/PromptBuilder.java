package rag.prompt;

import rag.parser.SearchResult;

import java.util.List;

public interface PromptBuilder {
    /**
     * Собирает prompt из вопроса пользователя и результатов поиска.
     *
     * @param userQuery исходный запрос пользователя
     * @param results   структурированные результаты поиска
     * @return итоговый prompt для LLM
     */
    String buildPrompt(String userQuery, List<SearchResult> results);
}
