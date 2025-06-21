package rag.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RapidApiSearchResultParserTest {
    @Test
    void whenValidJsonThenReturnsListOfSearchResults() throws Exception {
        String rawJson = """
        {
            "results": [
                {
                    "url": "https://test.com",
                    "title": "Test Title",
                    "description": "Test Description"
                },
                {
                    "url": "https://another.com",
                    "title": "Another Title",
                    "description": "Another Description"
                }
            ]
        }
        """;
        RapidApiSearchResultParser parser = new RapidApiSearchResultParser();
        List<SearchResult> results = parser.parse(rawJson);
        assertThat(results).hasSize(2);
        assertThat(results.getFirst().url()).isEqualTo("https://test.com");
        assertThat(results.getFirst().title()).isEqualTo("Test Title");
        assertThat(results.getFirst().description()).isEqualTo("Test Description");
        assertThat(results.get(1).url()).isEqualTo("https://another.com");
        assertThat(results.get(1).title()).isEqualTo("Another Title");
        assertThat(results.get(1).description()).isEqualTo("Another Description");
    }
    @Test
    void whenJsonWithEmptyResultsThenReturnsEmptyList() throws Exception {
        String rawJson = """
        {
            "results": []
        }
        """;
        RapidApiSearchResultParser parser = new RapidApiSearchResultParser();
        List<SearchResult> results = parser.parse(rawJson);
        assertThat(results).isEmpty();
    }

    @Test
    void whenInvalidJsonThenThrowsException() {
        String invalidJson = "{ not_a_json }";
        RapidApiSearchResultParser parser = new RapidApiSearchResultParser();
        assertThatThrownBy(() -> parser.parse(invalidJson))
                .isInstanceOf(JsonProcessingException.class);
    }
    @Test
    void whenJsonWithMissingFieldsThenUsesDefaults() throws Exception {
        String rawJson = """
        {
            "results": [
                {
                    "url": "https://test.com"
                }
            ]
        }
        """;
        RapidApiSearchResultParser parser = new RapidApiSearchResultParser();
        List<SearchResult> results = parser.parse(rawJson);
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().url()).isEqualTo("https://test.com");
        assertThat(results.getFirst().title()).isEqualTo("");
        assertThat(results.getFirst().description()).isEqualTo("");
    }
}