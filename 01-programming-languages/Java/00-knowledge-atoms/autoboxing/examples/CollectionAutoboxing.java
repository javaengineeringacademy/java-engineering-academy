import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionAutoboxing {
    public static void main(String[] args) {
        System.out.println("=== Collections with Autoboxing ===\n");

        // ArrayList with autoboxing
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);    // Autoboxing: int → Integer
        numbers.add(20);    // Autoboxing: int → Integer
        numbers.add(30);    // Autoboxing: int → Integer

        System.out.println("ArrayList<Integer>:");
        for (int num : numbers) {  // Unboxing: Integer → int
            System.out.println("  " + num);
        }

        // Get with unboxing
        int first = numbers.get(0);  // Unboxing: Integer → int
        System.out.println("First element: " + first);

        // HashMap with autoboxing
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);    // Autoboxing: int → Integer
        scores.put("Bob", 87);      // Autoboxing: int → Integer
        scores.put("Charlie", 92);  // Autoboxing: int → Integer

        System.out.println("\nHashMap<String, Integer>:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            int score = entry.getValue();  // Unboxing: Integer → int
            System.out.println("  " + entry.getKey() + ": " + score);
        }

        // Autoboxing in expressions
        System.out.println("\n=== Autoboxing in Expressions ===");
        Integer a = 10;
        Integer b = 20;
        Integer sum = a + b;  // Unbox, add, autobox
        System.out.println("a + b = " + sum);

        // Comparison with unboxing
        Integer x = 100;
        if (x > 50) {  // Unboxing x
            System.out.println("x is greater than 50");
        }

        // Enhanced for loop with autoboxing
        System.out.println("\n=== Enhanced For Loop ===");
        List<Double> prices = new ArrayList<>();
        prices.add(9.99);
        prices.add(19.99);
        prices.add(29.99);

        double total = 0;
        for (double price : prices) {  // Unboxing: Double → double
            total += price;
        }
        System.out.println("Total: $" + total);
    }
}
