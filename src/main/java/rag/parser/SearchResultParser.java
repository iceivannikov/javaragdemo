package rag.parser;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface SearchResultParser {
    /**
     * Преобразует "сырую" строку ответа в список результатов поиска.
     *
     * @param rawResponse строка с JSON-ответом
     * @return список структурированных результатов поиска
     */
    List<SearchResult> parse(String rawResponse) throws JsonProcessingException;
}
