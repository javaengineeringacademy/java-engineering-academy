import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class WhileLoopTest {

    @Test
    void testBasicWhile() {
        int sum = 0;
        int i = 1;
        while (i <= 10) {
            sum += i;
            i++;
        }
        assertEquals(55, sum);
    }

    @Test
    void testDoWhileExecutesAtLeastOnce() {
        int count = 0;
        int value = 100;
        do {
            count++;
            value++;
        } while (value < 100);
        assertEquals(1, count);
    }

    @Test
    void testWhileMayNotExecute() {
        int count = 0;
        int value = 100;
        while (value < 100) {
            count++;
            value++;
        }
        assertEquals(0, count);
    }

    @Test
    void testSentinelValue() {
        List<Integer> numbers = List.of(1, 2, 3, -1, 4, 5);
        List<Integer> result = new ArrayList<>();
        int index = 0;

        while (index < numbers.size()) {
            int num = numbers.get(index);
            if (num < 0) break;
            result.add(num);
            index++;
        }

        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void testInfiniteLoopPrevention() {
        int sum = 0;
        int i = 0;
        while (i < 100) {
            sum += i;
            i += 2;  // Step by 2 to prevent infinite loop
        }
        assertEquals(2500, sum);
    }

    @Test
    void testWhileWithBreak() {
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        int found = -1;
        int i = 0;

        while (i < numbers.size()) {
            if (numbers.get(i) == 30) {
                found = i;
                break;
            }
            i++;
        }

        assertEquals(2, found);
    }

    @Test
    void testEmptyList() {
        List<String> empty = new ArrayList<>();
        int count = 0;
        java.util.Iterator<String> it = empty.iterator();
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(0, count);
    }
}
