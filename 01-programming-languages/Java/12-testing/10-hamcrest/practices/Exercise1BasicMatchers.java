package academy.javaengineering.testing.hamcrest.practices;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Exercise 1: Basic Hamcrest Matchers
 *
 * Tasks:
 * 1. Use is() and equalTo() for equality
 * 2. Use hasItem() and hasSize() for collections
 * 3. Use containsString() for strings
 * 4. Use greaterThan() for numbers
 */
class Exercise1BasicMatchers {

    @Test
    void shouldAssertEquality() {
        // TODO: Assert 5 is equal to 5
        // TODO: Assert "hello" is equal to "hello"
    }

    @Test
    void shouldAssertCollections() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        // TODO: Assert has size 5
        // TODO: Assert contains 3
        // TODO: Assert does not contain 6
    }

    @Test
    void shouldAssertComparisons() {
        // TODO: Assert 10 is greater than 5
        // TODO: Assert 3 is less than 7
    }
}
