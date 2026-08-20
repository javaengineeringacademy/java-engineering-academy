package academy.javaengineering.testing.assertj.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Exercise1BasicAssertionsSolution {

    @Test
    void shouldAssertIntegers() {
        assertThat(2 + 2).isEqualTo(4);
        assertThat(10).isGreaterThan(5);
        assertThat(50).isBetween(1, 100);
    }

    @Test
    void shouldAssertStrings() {
        String greeting = "Hello, World!";
        assertThat(greeting).contains("World");
        assertThat(greeting).startsWith("Hello");
        assertThat(greeting).hasSize(13);
    }

    @Test
    void shouldAssertCollections() {
        List<String> colors = List.of("red", "green", "blue");
        assertThat(colors).hasSize(3);
        assertThat(colors).contains("red");
        assertThat(colors).doesNotContain("yellow");
    }
}
