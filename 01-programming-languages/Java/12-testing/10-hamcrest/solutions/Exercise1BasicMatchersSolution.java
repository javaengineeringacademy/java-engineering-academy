package academy.javaengineering.testing.hamcrest.solutions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class Exercise1BasicMatchersSolution {

    @Test
    void shouldAssertEquality() {
        assertThat(5, is(5));
        assertThat("hello", equalTo("hello"));
    }

    @Test
    void shouldAssertCollections() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        assertThat(numbers, hasSize(5));
        assertThat(numbers, hasItem(3));
        assertThat(numbers, not(hasItem(6)));
    }

    @Test
    void shouldAssertComparisons() {
        assertThat(10, greaterThan(5));
        assertThat(3, lessThan(7));
    }
}
