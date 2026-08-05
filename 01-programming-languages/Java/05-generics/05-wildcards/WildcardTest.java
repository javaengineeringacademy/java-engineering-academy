import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WildcardTest {

    @Test
    void testSumOfListIntegers() {
        List<Integer> integers = List.of(1, 2, 3, 4, 5);
        assertEquals(15.0, WildcardDemo.sumOfList(integers));
    }

    @Test
    void testAddNumbers() {
        List<Number> numberList = new ArrayList<>();
        WildcardDemo.addNumbers(numberList);
        assertEquals(5, numberList.size());
    }

    @Test
    void testFindMaxInteger() {
        List<Integer> list = List.of(5, 2, 8, 1, 9);
        assertEquals(9, WildcardDemo.findMax(list));
    }

    @Test
    void testFindMaxString() {
        List<String> list = List.of("Apple", "Banana", "Cherry");
        assertEquals("Cherry", WildcardDemo.findMax(list));
    }

    @Test
    void testCopy() {
        List<Number> dest = new ArrayList<>();
        List<Integer> src = List.of(1, 2, 3);
        WildcardDemo.copy(dest, src);
        assertEquals(3, dest.size());
    }

    @Test
    void testFindMaxEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> {
            WildcardDemo.findMax(List.of());
        });
    }
}
