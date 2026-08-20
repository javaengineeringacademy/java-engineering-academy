package academy.javaengineering.testing.hamcrest.solutions;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class Exercise2CombinatorsSolution {

    @Test
    void shouldUseLogicalCombinators() {
        int age = 25;
        assertThat(age, allOf(greaterThanOrEqualTo(18), lessThanOrEqualTo(65)));

        String str = "Apple";
        assertThat(str, anyOf(startsWith("A"), startsWith("B")));

        Object value = "test";
        assertThat(value, is(notNullValue()));
    }

    @Test
    void shouldUseBothAndEither() {
        String name = "Alice";
        assertThat(name, both(containsString("li")).and(hasLength(greaterThan(3))));
        assertThat(name, either(startsWith("A")).or(startsWith("B")));
    }
}
