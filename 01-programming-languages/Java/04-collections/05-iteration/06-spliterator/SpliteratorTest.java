package academy.javaengineering.collections.iteration.spliterator;

import java.util.*;
import java.util.stream.*;

public class SpliteratorTest {
    public static void main(String[] args) {
        List<Integer> list = IntStream.rangeClosed(1, 100).boxed().toList();
        long count = list.stream().filter(n -> n % 2 == 0).count();
        assert count == 50 : "Should be 50 evens";
        System.out.println("SpliteratorTest passed");
    }
}
