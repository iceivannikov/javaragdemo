package rag.search;

public interface SearchProvider {
    /**
     * Выполняет поиск по запросу и возвращает "сырые" результаты (например, JSON-строку или структуру).
     * @param query поисковый запрос
     * @return результат поиска (строка или список объектов)
     */
    String search(String query);
}
