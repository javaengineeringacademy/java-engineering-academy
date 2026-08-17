# Control Flow - Solutions

## Solution 1: FizzBuzz

```java
public class FizzBuzz {
    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            if (i % 15 == 0) System.out.println("FizzBuzz");
            else if (i % 3 == 0) System.out.println("Fizz");
            else if (i % 5 == 0) System.out.println("Buzz");
            else System.out.println(i);
        }
    }
}
```

## Solution 2: Star Triangle

```java
public class StarTriangle {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
```

## Solution 3: Switch Calculator

```java
public class SwitchCalculator {
    public static void main(String[] args) {
        double a = 10, b = 3;
        String operator = "+";

        double result = switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> throw new IllegalArgumentException("Unknown: " + operator);
        };
        System.out.printf("%s %s %s = %s%n", a, operator, b, result);
    }
}
```

## Solution 4: Labeled Break

```java
public class LabeledBreak {
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int target = 5;
        outer:
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    System.out.printf("Found %d at [%d][%d]%n", target, i, j);
                    break outer;
                }
            }
        }
    }
}
```
