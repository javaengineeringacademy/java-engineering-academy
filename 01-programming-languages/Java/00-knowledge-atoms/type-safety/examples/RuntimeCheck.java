public class RuntimeCheck {
    public static void main(String[] args) {
        // 1. Safe cast with instanceof check
        Object obj1 = "Hello";
        if (obj1 instanceof String) {
            String s = (String) obj1;
            System.out.println("Safe cast: " + s.toUpperCase());
        }

        // 2. Unsafe cast that would fail
        Object obj2 = "Hello";
        try {
            Integer num = (Integer) obj2; // ClassCastException
        } catch (ClassCastException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
        }

        // 3. Runtime type checking in method
        processObject("Hello");
        processObject(42);
        processObject(3.14);

        // 4. Checking array types at runtime
        Object[] array = new String[]{"Hello", "World"};
        try {
            array[0] = 42; // ArrayStoreException
        } catch (ArrayStoreException e) {
            System.out.println("Array store check: " + e.getMessage());
        }
    }

    static void processObject(Object obj) {
        if (obj instanceof String s) {
            System.out.println("String of length " + s.length() + ": " + s);
        } else if (obj instanceof Integer i) {
            System.out.println("Integer doubled: " + (i * 2));
        } else if (obj instanceof Double d) {
            System.out.println("Double as int: " + d.intValue());
        } else {
            System.out.println("Unknown type: " + obj.getClass().getSimpleName());
        }
    }
}
