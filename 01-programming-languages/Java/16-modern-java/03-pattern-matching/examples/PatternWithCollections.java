package academy.javaengineering.modern.pattern;

import java.util.List;

/**
 * Pattern matching with collections and complex conditions.
 */
public class PatternWithCollections {

    public record Person(String name, int age, List<String> hobbies) {}
    public record Company(String name, List<Person> employees) {}

    public static void main(String[] args) {
        // Create test data
        Person alice = new Person("Alice", 30, List.of("Reading", "Hiking"));
        Person bob = new Person("Bob", 25, List.of("Gaming", "Cooking"));
        Person charlie = new Person("Charlie", 35, List.of("Photography"));
        Company company = new Company("TechCorp", List.of(alice, bob, charlie));

        // Pattern matching with records
        System.out.println("=== Person Classification ===");
        for (Person person : List.of(alice, bob, charlie)) {
            System.out.println(person.name() + ": " + classifyPerson(person));
        }

        // Pattern matching in method calls
        System.out.println("\n=== Hobby Analysis ===");
        for (Person person : List.of(alice, bob, charlie)) {
            System.out.println(person.name() + ": " + analyzeHobbies(person.hobbies()));
        }

        // Pattern matching with company
        System.out.println("\n=== Company Analysis ===");
        System.out.println("Company: " + company.name());
        System.out.println("Total employees: " + company.employees().size());
        System.out.println("Average age: " + calculateAverageAge(company));
        System.out.println("Department: " + getDepartment(company));

        // Pattern matching with nested structures
        System.out.println("\n=== Nested Pattern Matching ===");
        Object nested = List.of(
            List.of(1, 2, 3),
            List.of("a", "b"),
            List.of()
        );
        System.out.println("Nested structure: " + analyzeNested(nested));
    }

    static String classifyPerson(Person person) {
        return switch (person) {
            case Person p && p.age() < 18 -> "Minor";
            case Person p && p.age() >= 18 && p.age() < 65 -> "Adult";
            case Person p && p.age() >= 65 -> "Senior";
            default -> "Unknown";
        };
    }

    static String analyzeHobbies(List<String> hobbies) {
        return switch (hobbies) {
            case List<String> h && h.isEmpty() -> "No hobbies";
            case List<String> h && h.size() == 1 -> "Single hobby: " + h.get(0);
            case List<String> h && h.size() <= 3 -> "Few hobbies: " + String.join(", ", h);
            case List<String> h -> "Many hobbies: " + h.size() + " total";
        };
    }

    static double calculateAverageAge(Company company) {
        return company.employees().stream()
            .mapToInt(Person::age)
            .average()
            .orElse(0);
    }

    static String getDepartment(Company company) {
        return switch (company.employees().size()) {
            case int size && size > 100 -> "Large Enterprise";
            case int size && size > 50 -> "Medium Business";
            case int size && size > 10 -> "Small Business";
            default -> "Startup";
        };
    }

    static String analyzeNested(Object obj) {
        return switch (obj) {
            case List<?> list && list.isEmpty() -> "Empty list";
            case List<?> list && list.get(0) instanceof List<?> inner -> 
                "Nested list with " + list.size() + " sublists";
            case List<?> list -> "Flat list with " + list.size() + " elements";
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            default -> "Other: " + obj.getClass().getSimpleName();
        };
    }
}
