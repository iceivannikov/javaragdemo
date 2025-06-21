package rag.llm;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface LLMService {
    /**
     * Отправляет prompt к LLM и получает сгенерированный ответ.
     * @param prompt строка с prompt’ом
     * @return сгенерированный ответ LLM
     */
    String generate(String prompt) throws JsonProcessingException;
}
