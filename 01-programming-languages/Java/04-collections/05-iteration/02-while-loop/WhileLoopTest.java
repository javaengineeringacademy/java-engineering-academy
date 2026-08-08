package academy.javaengineering.collections.iteration.whileloop;

import java.util.*;

public class WhileLoopTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30));
        int sum = 0;
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) sum += it.next();
        assert sum == 60 : "Sum should be 60";
        System.out.println("WhileLoopTest passed");
    }
}
