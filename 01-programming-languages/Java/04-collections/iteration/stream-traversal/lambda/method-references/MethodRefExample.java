import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class MethodRefExample {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("alice", "bob", "charlie", "david", "eve");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Static method reference
        System.out.println("=== Static Method Reference ===");
        Function<String, Integer> parseInt = Integer::parseInt;
        List<Integer> parsed = Arrays.asList("1", "2", "3", "4", "5").stream()
            .map(parseInt)
            .collect(Collectors.toList());
        System.out.println("Parsed integers: " + parsed);

        // Unbound instance method reference
        System.out.println("\n=== Unbound Instance Method Reference ===");
        Function<String, Integer> toLength = String::length;
        List<Integer> lengths = names.stream()
            .map(toLength)
            .collect(Collectors.toList());
        System.out.println("Name lengths: " + lengths);

        Function<String, String> toUpperCase = String::toUpperCase;
        List<String> upperNames = names.stream()
            .map(toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Uppercase names: " + upperNames);

        // Bound instance method reference
        System.out.println("\n=== Bound Instance Method Reference ===");
        String prefix = "Name: ";
        Function<String, String> addPrefix = prefix::concat;
        List<String> prefixed = names.stream()
            .map(addPrefix)
            .collect(Collectors.toList());
        System.out.println("With prefix: " + prefixed);

        // Constructor reference
        System.out.println("\n=== Constructor Reference ===");
        Supplier<ArrayList<String>> listFactory = ArrayList::new;
        ArrayList<String> newList = listFactory.get();
        names.forEach(newList::add);
        System.out.println("Created list: " + newList);

        // Constructor reference with function
        Function<Integer, ArrayList> sizedList = ArrayList::new;
        ArrayList<Integer> sized = sizedList.apply(10);
        System.out.println("Sized list capacity: " + sized.size());

        // Method references in stream operations
        System.out.println("\n=== Method References in Streams ===");

        // forEach with method reference
        System.out.print("Names: ");
        names.forEach(System.out::print);
        System.out.println();

        // filter with method reference
        List<String> nonEmpty = names.stream()
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
        System.out.println("Non-empty: " + nonEmpty);

        // sorted with method reference
        List<String> sorted = names.stream()
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());
        System.out.println("Sorted: " + sorted);

        // reduce with method reference
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // Method references with custom classes
        System.out.println("\n=== Custom Class Method References ===");
        List<Person> people = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        );

        // Method reference to getter
        Function<Person, String> getName = Person::getName;
        List<String> personNames = people.stream()
            .map(getName)
            .collect(Collectors.toList());
        System.out.println("Person names: " + personNames);

        // Method reference to instance method
        Function<Person, Integer> getAge = Person::getAge;
        List<Integer> ages = people.stream()
            .map(getAge)
            .collect(Collectors.toList());
        System.out.println("Ages: " + ages);

        // Comparing lambda vs method reference
        System.out.println("\n=== Lambda vs Method Reference ===");

        // Lambda
        List<String> upperLambda = names.stream()
            .map(s -> s.toUpperCase())
            .collect(Collectors.toList());

        // Method reference
        List<String> upperMethodRef = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());

        System.out.println("Lambda result equals method ref: "
            + upperLambda.equals(upperMethodRef));

        // Method reference chains
        System.out.println("\n=== Method Reference Chains ===");
        String result = names.stream()
            .filter(s -> s.length() > 3)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.joining(", "));
        System.out.println("Filtered, uppered, sorted, joined: " + result);
    }

    static class Person {
        private String name;
        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
}
