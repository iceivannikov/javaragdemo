package rag.input;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

class ConsoleQuerySourceTest {
    @Test
    void whenUserProvidesInputThenItIsReadCorrectly() {
        String simulatedInput = "привет\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        try {
            ConsoleQuerySource source = new ConsoleQuerySource();
            String result = source.getQuery();

            assertThat(result).isEqualTo("привет");
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void whenUserEntersEmptyLineThenValidLineThenItIsReadCorrectly() {
        String simulatedInput = "\njava rocks\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        try {
            ConsoleQuerySource source = new ConsoleQuerySource();
            String result = source.getQuery();

            assertThat(result).isEqualTo("java rocks");
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void whenUserInputsSpacesThenValidLineThenItIsReadCorrectly() {
        String simulatedInput = "   \nGPT-4\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        try {
            ConsoleQuerySource source = new ConsoleQuerySource();
            String result = source.getQuery();

            assertThat(result).isEqualTo("GPT-4");
        } finally {
            System.setIn(originalIn);
        }
    }
}