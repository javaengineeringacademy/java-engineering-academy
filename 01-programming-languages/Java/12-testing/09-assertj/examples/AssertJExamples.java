package academy.javaengineering.testing.assertj.examples;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AssertJExamples {

    @Test
    void shouldAssertBasicTypes() {
        assertThat(2 + 3).isEqualTo(5);
        assertThat("hello").isNotEmpty().contains("ell");
        assertThat(true).isTrue();
        assertThat(Math.PI).isGreaterThan(3.0).isLessThan(4.0);
    }

    @Test
    void shouldAssertCollections() {
        List<String> fruits = List.of("apple", "banana", "cherry");

        assertThat(fruits)
            .hasSize(3)
            .contains("apple", "banana")
            .doesNotContain("grape")
            .startsWith("apple");
    }

    @Test
    void shouldAssertMaps() {
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30);

        assertThat(ages)
            .hasSize(2)
            .containsKey("Alice")
            .containsEntry("Bob", 30);
    }

    @Test
    void shouldAssertObjects() {
        User user = new User("Alice", 25);

        assertThat(user)
            .extracting("name", "age")
            .containsExactly("Alice", 25);
    }

    @Test
    void shouldAssertExceptions() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("Bad input");
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Bad input");
    }

    static class User {
        String name;
        int age;
        User(String name, int age) { this.name = name; this.age = age; }
    }
}
