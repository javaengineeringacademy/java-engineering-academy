package academy.javaengineering.oop.internals;

public class AccessModifiersInternals {

    class PublicClass {
        public int publicField = 1;
        protected int protectedField = 2;
        int defaultField = 3;
        private int privateField = 4;

        public void publicMethod() {}
        protected void protectedMethod() {}
        void defaultMethod() {}
        private void privateMethod() {}
    }

    public static void main(String[] args) {
        System.out.println("=== Access Modifiers Internals ===\n");

        // 1. Access Levels
        System.out.println("--- Access Levels ---");
        System.out.println("public: everywhere");
        System.out.println("protected: package + subclasses");
        System.out.println("default: package only");
        System.out.println("private: class only");

        // 2. Visibility Table
        System.out.println("\n--- Visibility Table ---");
        System.out.println("Modifier    | Class | Package | Subclass | World");
        System.out.println("------------|-------|---------|----------|------");
        System.out.println("public      |   Y   |    Y    |    Y     |   Y");
        System.out.println("protected   |   Y   |    Y    |    Y     |   N");
        System.out.println("default     |   Y   |    Y    |    N     |   N");
        System.out.println("private     |   Y   |    N    |    N     |   N");

        // 3. Best Practices
        System.out.println("\n--- Best Practices ---");
        System.out.println("1. Start with private");
        System.out.println("2. Open up as needed");
        System.out.println("3. Prefer getter/setter");
        System.out.println("4. Use final for immutable");
    }
}
