package rag.parser;

public class SearchResult {
    String url;
    String title;
    String description;

    public SearchResult( String url, String title, String description) {
        this.url = url;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
