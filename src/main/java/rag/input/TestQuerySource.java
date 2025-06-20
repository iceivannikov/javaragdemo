package rag.input;

import rag.config.ApiConfig;
import rag.config.ConfigLoader;

public class TestQuerySource implements QuerySource {
    ApiConfig apiConfig = ConfigLoader.loadApiConfig();

    @Override
    public String getQuery() {
        return "World Cup";
    }
}
