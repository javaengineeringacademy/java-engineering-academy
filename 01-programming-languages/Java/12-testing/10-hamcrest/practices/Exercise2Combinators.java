package academy.javaengineering.testing.hamcrest.practices;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Exercise 2: Matcher Combinators
 *
 * Tasks:
 * 1. Use allOf() for AND conditions
 * 2. Use anyOf() for OR conditions
 * 3. Use not() for negation
 * 4. Use both() and either()
 */
class Exercise2Combinators {

    @Test
    void shouldUseLogicalCombinators() {
        int age = 25;
        // TODO: Assert age is between 18 and 65 using allOf
        // TODO: Assert string starts with "A" or "B" using anyOf
        // TODO: Assert value is not null using not()
    }

    @Test
    void shouldUseBothAndEither() {
        String name = "Alice";
        // TODO: Assert name contains "li" and has length > 3
        // TODO: Assert name starts with "A" or "B"
    }
}
