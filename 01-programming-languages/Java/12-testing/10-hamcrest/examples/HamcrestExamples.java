package academy.javaengineering.testing.hamcrest.examples;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class HamcrestExamples {

    @Test
    void shouldUseBasicMatchers() {
        assertThat(2 + 3, is(5));
        assertThat("hello", is(not("goodbye")));
        assertThat(true, is(true));
    }

    @Test
    void shouldAssertCollections() {
        List<String> fruits = List.of("apple", "banana", "cherry");

        assertThat(fruits, hasSize(3));
        assertThat(fruits, hasItem("apple"));
        assertThat(fruits, not(hasItem("grape")));
    }

    @Test
    void shouldAssertStrings() {
        assertThat("Hello World", containsString("World"));
        assertThat("Hello World", startsWith("Hello"));
        assertThat("Hello World", endsWith("World"));
    }

    @Test
    void shouldAssertMaps() {
        Map<String, Integer> ages = Map.of("Alice", 25, "Bob", 30);

        assertThat(ages, hasKey("Alice"));
        assertThat(ages, hasEntry("Bob", 30));
        assertThat(ages, aMapWithSize(2));
    }

    @Test
    void shouldCombineMatchers() {
        assertThat(5, allOf(greaterThan(3), lessThan(10)));
        assertThat("test@test.com", anyOf(containsString("@"), containsString(".")));
    }
}
