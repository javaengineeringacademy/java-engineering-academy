# Control Flow - Examples

## If-Else and Switch

```java
public class ControlFlowBasics {
    public static void main(String[] args) {
        // If-else
        int temperature = 35;
        if (temperature > 30) {
            System.out.println("Hot");
        } else if (temperature > 20) {
            System.out.println("Warm");
        } else {
            System.out.println("Cool");
        }

        // Switch expression (Java 14+)
        String day = "WEDNESDAY";
        String type = switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY",
                 "THURSDAY", "FRIDAY" -> "Weekday";
            case "SATURDAY", "SUNDAY" -> "Weekend";
            default -> "Unknown";
        };
        System.out.println(day + " is a " + type);
    }
}
```

## For Loops

```java
public class ForLoops {
    public static void main(String[] args) {
        // Traditional for loop
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Enhanced for loop
        String[] fruits = {"Apple", "Banana", "Cherry"};
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // Nested loop - multiplication table
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                System.out.printf("%d x %d = %d  ", i, j, i * j);
            }
            System.out.println();
        }
    }
}
```

## While and Do-While

```java
public class WhileLoops {
    public static void main(String[] args) {
        // While loop
        int count = 0;
        while (count < 5) {
            System.out.print(count + " ");
            count++;
        }
        System.out.println();

        // Do-while (executes at least once)
        int num = 10;
        do {
            System.out.println("num = " + num);
            num--;
        } while (num > 0);

        // Break and continue
        for (int i = 0; i < 10; i++) {
            if (i == 3) continue;   // skip 3
            if (i == 7) break;      // stop at 7
            System.out.print(i + " ");
        }
    }
}
```

## Pattern Matching Switch (Java 21)

```java
public class PatternMatching {
    static String describe(Object obj) {
        return switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s  -> "String: " + s.toUpperCase();
            case int[] arr -> "Array of " + arr.length + " ints";
            case null      -> "null";
            default        -> "Other: " + obj.getClass();
        };
    }

    public static void main(String[] args) {
        System.out.println(describe(42));
        System.out.println(describe("hello"));
        System.out.println(describe(new int[]{1, 2, 3}));
        System.out.println(describe(null));
    }
}
```
