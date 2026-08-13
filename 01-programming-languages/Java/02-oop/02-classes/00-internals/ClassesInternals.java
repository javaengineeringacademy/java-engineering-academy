package academy.javaengineering.oop.internals;

public class ClassesInternals {

    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    public static void main(String[] args) {
        System.out.println("=== Classes Internals ===\n");

        // 1. Class Structure
        System.out.println("--- Class Structure ---");
        System.out.println("Fields: state (variables)");
        System.out.println("Methods: behavior (functions)");
        System.out.println("Constructors: initialization");

        // 2. Access Modifiers
        System.out.println("\n--- Access Modifiers ---");
        System.out.println("public: accessible everywhere");
        System.out.println("protected: accessible in package + subclasses");
        System.out.println("default: accessible in package only");
        System.out.println("private: accessible in class only");

        // 3. Static vs Instance
        System.out.println("\n--- Static vs Instance ---");
        System.out.println("Static: belongs to class");
        System.out.println("Instance: belongs to object");
        System.out.println("Static: one copy shared");
        System.out.println("Instance: separate copy per object");

        // 4. Final Keyword
        System.out.println("\n--- Final Keyword ---");
        System.out.println("final variable: cannot change value");
        System.out.println("final method: cannot override");
        System.out.println("final class: cannot extend");
    }
}
