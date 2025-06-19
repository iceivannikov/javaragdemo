package rag.input;

public interface QuerySource {
    /**
     * Получить исходный запрос пользователя для поиска.
     * @return строка с запросом
     */
    String getQuery();
}
