package academy.javaengineering.testing.junit5.advanced.solutions;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class Exercise2DynamicTestsSolution {

    int multiply(int a, int b) { return a * b; }
    String capitalize(String s) { return s == null ? null : s.substring(0, 1).toUpperCase() + s.substring(1); }

    @TestFactory
    Collection<DynamicTest> multiplicationTable() {
        List<int[]> data = List.of(
            new int[]{2, 3, 6},
            new int[]{4, 5, 20},
            new int[]{0, 10, 0},
            new int[]{7, 7, 49}
        );
        return data.stream()
            .map(d -> dynamicTest(
                String.format("%d x %d = %d", d[0], d[1], d[2]),
                () -> assertEquals(d[2], multiply(d[0], d[1]))
            ))
            .collect(Collectors.toList());
    }

    @TestFactory
    Collection<DynamicTest> capitalizeTests() {
        return IntStream.rangeClosed(1, 3)
            .mapToObj(i -> dynamicTest(
                "Capitalize string " + i,
                () -> assertNotNull(capitalize("test" + i))
            ))
            .collect(Collectors.toList());
    }
}
