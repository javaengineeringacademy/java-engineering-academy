package academy.javaengineering.collections.iteration.spliterator;

import java.util.*;
import java.util.stream.*;

public class SpliteratorExample {
    public static void main(String[] args) {
        List<Integer> list = IntStream.rangeClosed(1, 10).boxed().toList();
        Spliterator<Integer> spl = list.spliterator();
        System.out.println("Exact size: " + spl.getExactSizeIfKnown());
        spl.forEachRemaining(System.out::println);
    }
}
