package rag.prompt;

import org.junit.jupiter.api.Test;
import rag.parser.SearchResult;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SimplePromptBuilderTest {
    @Test
    void whenResultsNotEmptyThenPromptContainsAllResults() {
        List<SearchResult> results = List.of(
                new SearchResult("https://test1.com", "Title1", "Description1"),
                new SearchResult("https://test2.com", "Title2", "Description2")
        );
        SimplePromptBuilder builder = new SimplePromptBuilder();
        String prompt = builder.buildPrompt("java books", results);
        assertThat(prompt).contains("Запрос: java books");
        assertThat(prompt).contains("Результаты поиска:");
        assertThat(prompt).contains("1. Title1");
        assertThat(prompt).contains("URL: https://test1.com");
        assertThat(prompt).contains("Описание: Description1");
        assertThat(prompt).contains("2. Title2");
        assertThat(prompt).contains("URL: https://test2.com");
        assertThat(prompt).contains("Описание: Description2");
    }

    @Test
    void whenResultsEmptyThenPromptMentionsNoResults() {
        SimplePromptBuilder builder = new SimplePromptBuilder();
        String prompt = builder.buildPrompt("some query", List.of());
        assertThat(prompt).contains("Нет результатов поиска");
        assertThat(prompt).doesNotContain("Результаты поиска:");
    }

    @Test
    void whenQueryIsEmptyThenPromptStillBuilds() {
        List<SearchResult> results = List.of(
                new SearchResult("https://test.com", "Title", "Description")
        );
        SimplePromptBuilder builder = new SimplePromptBuilder();
        String prompt = builder.buildPrompt("", results);
        assertThat(prompt).contains("Запрос: ");
        assertThat(prompt).contains("Результаты поиска:");
    }

}