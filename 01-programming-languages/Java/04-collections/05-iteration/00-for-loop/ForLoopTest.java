package academy.javaengineering.collections.iteration.forloop;

import java.util.*;

public class ForLoopTest {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        int sum = 0;
        for (int i = 0; i < list.size(); i++) sum += list.get(i);
        assert sum == 15 : "Sum should be 15";
        System.out.println("ForLoopTest passed");
    }
}
