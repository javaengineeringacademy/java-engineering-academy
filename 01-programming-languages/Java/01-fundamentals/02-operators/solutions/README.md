# Operators - Solutions

## Solution 1: Bitmask Flags

```java
public class BitmaskFlags {
    static final int READ = 1;     // 001
    static final int WRITE = 2;    // 010
    static final int EXECUTE = 4;  // 100

    public static void main(String[] args) {
        int permissions = 0;
        permissions |= READ;       // Add READ
        permissions |= WRITE;      // Add WRITE
        System.out.println("Has READ: " + ((permissions & READ) != 0));
        System.out.println("Has EXECUTE: " + ((permissions & EXECUTE) != 0));

        permissions &= ~WRITE;     // Remove WRITE
        System.out.println("After removing WRITE: " + Integer.toBinaryString(permissions));

        permissions ^= EXECUTE;    // Toggle EXECUTE
        System.out.println("After toggle EXECUTE: " + Integer.toBinaryString(permissions));
    }
}
```

## Solution 2: Swap Without Temp

```java
public class SwapDemo {
    public static void main(String[] args) {
        int a = 5, b = 9;
        System.out.printf("Before: a=%d, b=%d%n", a, b);

        // XOR swap
        a ^= b;
        b ^= a;
        a ^= b;
        System.out.printf("After:  a=%d, b=%d%n", a, b);

        // Arithmetic swap
        a = a + b;   // a = 14
        b = a - b;   // b = 5
        a = a - b;   // a = 9
        System.out.printf("Arithmetic: a=%d, b=%d%n", a, b);
    }
}
```

## Solution 3: Short-Circuit Demo

```java
public class ShortCircuitDemo {
    static boolean sideEffect() {
        System.out.println("  Side effect executed!");
        return true;
    }

    public static void main(String[] args) {
        System.out.println("AND with false first:");
        boolean r1 = false && sideEffect();  // sideEffect NOT called
        System.out.println("Result: " + r1);

        System.out.println("\nOR with true first:");
        boolean r2 = true || sideEffect();   // sideEffect NOT called
        System.out.println("Result: " + r2);

        System.out.println("\nAND with true first:");
        boolean r3 = true && sideEffect();   // sideEffect IS called
        System.out.println("Result: " + r3);
    }
}
```

## Solution 4: Bitwise Division

```java
public class BitwiseDivision {
    public static void main(String[] args) {
        int value = 64;
        for (int i = 0; i < 7; i++) {
            System.out.printf("%d >> %d = %d%n", value, i, value >> i);
        }
    }
}
```
