package academy.javaengineering.knowledgeatoms.passbyvalue;

public class PassByValueInternals {

    public static void main(String[] args) {
        System.out.println("=== Pass by Value Internals ===\n");

        // 1. Primitives: copy of value
        System.out.println("--- Primitives: Copy of Value ---");
        int x = 10;
        System.out.println("Before modifyPrimitive: x = " + x);
        modifyPrimitive(x);
        System.out.println("After modifyPrimitive: x = " + x);

        // 2. Objects: copy of reference
        System.out.println("\n--- Objects: Copy of Reference ---");
        StringBuilder sb = new StringBuilder("Hello");
        System.out.println("Before modifyObject: sb = " + sb);
        modifyObject(sb);
        System.out.println("After modifyObject: sb = " + sb);

        // 3. Reassignment doesn't work
        System.out.println("\n--- Reassignment Doesn't Work ---");
        StringBuilder original = new StringBuilder("Hello");
        System.out.println("Before reassignObject: " + original);
        reassignObject(original);
        System.out.println("After reassignObject: " + original);

        // 4. Array behavior
        System.out.println("\n--- Array Behavior ---");
        int[] arr = {1, 2, 3};
        System.out.println("Before modifyArray: arr[0] = " + arr[0]);
        modifyArray(arr);
        System.out.println("After modifyArray: arr[0] = " + arr[0]);

        // 5. Swap doesn't work
        System.out.println("\n--- Swap Doesn't Work ---");
        StringBuilder a = new StringBuilder("Hello");
        StringBuilder b = new StringBuilder("World");
        System.out.println("Before swap: a = " + a + ", b = " + b);
        swap(a, b);
        System.out.println("After swap: a = " + a + ", b = " + b);

        // 6. JVM perspective
        System.out.println("\n--- JVM Perspective ---");
        System.out.println("Method invocation pushes a new stack frame:");
        System.out.println("  1. Caller's stack frame has original variables");
        System.out.println("  2. Callee's stack frame receives COPIES of arguments");
        System.out.println("  3. Modifying copies does not affect caller's variables");
        System.out.println("  4. For objects: reference is copied, object is shared");
        System.out.println("  5. Modifying object state through reference IS visible to caller");
    }

    private static void modifyPrimitive(int value) {
        value = 20;
        System.out.println("  Inside method: value = " + value);
    }

    private static void modifyObject(StringBuilder builder) {
        builder.append(" World");  // modifies the shared object
        System.out.println("  Inside method: builder = " + builder);
    }

    private static void reassignObject(StringBuilder builder) {
        builder = new StringBuilder("Goodbye");  // reassigns local copy only
        System.out.println("  Inside method: builder = " + builder);
    }

    private static void modifyArray(int[] arr) {
        arr[0] = 100;  // modifies shared array contents
        arr = new int[]{4, 5, 6};  // reassigns local reference only
        System.out.println("  Inside method: arr[0] = " + arr[0]);
    }

    private static void swap(StringBuilder x, StringBuilder y) {
        StringBuilder temp = x;
        x = y;
        y = temp;
        System.out.println("  Inside method: x = " + x + ", y = " + y);
    }
}
