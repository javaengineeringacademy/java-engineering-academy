public class NullUnboxing {
    public static void main(String[] args) {
        System.out.println("=== NullPointerException with Unboxing ===\n");

        Integer num = null;

        System.out.println("Integer num is null.");
        System.out.println("Attempting to unbox null Integer...\n");

        try {
            // This will throw NullPointerException
            int value = num;
            System.out.println("Value: " + value);
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException!");
            System.out.println("Message: " + e.getMessage());
        }

        System.out.println("\n=== Safe Null Handling ===");

        // Safe ways to handle potential null values
        Integer safeNum = null;

        // Method 1: Check for null
        if (safeNum != null) {
            int safeValue = safeNum;
            System.out.println("Value: " + safeValue);
        } else {
            System.out.println("Value is null (Method 1: null check)");
        }

        // Method 2: Use Integer's methods
        int intValue = safeNum != null ? safeNum : 0;
        System.out.println("Value with default: " + intValue);

        // Method 3: Use Integer's static methods
        Integer anotherNum = null;
        int result = Integer.valueOf(String.valueOf(anotherNum));
        System.out.println("Parsed value: " + result);

        // Demonstrate autoboxing null
        System.out.println("\n=== Autoboxing Null ===");
        Integer nullInt = null;
        System.out.println("nullInt: " + nullInt);
        System.out.println("nullInt == null: " + (nullInt == null));
    }
}
