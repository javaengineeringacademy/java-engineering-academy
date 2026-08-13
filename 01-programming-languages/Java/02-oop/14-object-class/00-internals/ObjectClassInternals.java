package academy.javaengineering.oop.internals;

public class ObjectClassInternals {

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Person person = (Person) obj;
            return age == person.age && name.equals(person.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Object Class Internals ===\n");

        // 1. toString()
        System.out.println("--- toString() ---");
        Person p = new Person("Alice", 25);
        System.out.println(p); // calls toString()
        System.out.println("Returns: " + p.toString());

        // 2. equals() and hashCode()
        System.out.println("\n--- equals() and hashCode() ---");
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1 == p2: " + (p1 == p2));
        System.out.println("Same hashCode: " + (p1.hashCode() == p2.hashCode()));

        // 3. getClass()
        System.out.println("\n--- getClass() ---");
        System.out.println("Class: " + p.getClass().getName());
        System.out.println("Final: cannot override getClass()");

        // 4. clone() and finalize()
        System.out.println("\n--- clone() and finalize() ---");
        System.out.println("clone(): creates copy (use Cloneable)");
        System.out.println("finalize(): deprecated, use try-with-resources");
    }
}
