package academy.javaengineering.knowledgeatoms.equalshashcode;

import java.util.*;

public class EqualsHashCodeInternals {

    public static void main(String[] args) {
        System.out.println("=== Equals & HashCode Internals ===\n");

        // 1. How HashMap uses hashCode
        System.out.println("--- HashMap Bucket Mechanics ---");
        System.out.println("HashMap stores entries in buckets based on hashCode()");
        System.out.println("Bucket index = hashCode() & (capacity - 1)");
        System.out.println("Multiple keys can map to same bucket (collision)");
        System.out.println("equals() resolves which entry matches within a bucket");

        // 2. Hash collision demo
        System.out.println("\n--- Hash Collision Demo ---");
        String a = "Aa";
        String b = "BB";
        System.out.println("\"Aa\".hashCode() = " + a.hashCode());
        System.out.println("\"BB\".hashCode() = " + b.hashCode());
        System.out.println("Same hash code, different objects: " + a.equals(b));

        // 3. Contract violation demo
        System.out.println("\n--- Contract Violation Demo ---");
        Set<BrokenPerson> brokenSet = new HashSet<>();
        BrokenPerson bp1 = new BrokenPerson("Alice", 30);
        BrokenPerson bp2 = new BrokenPerson("Alice", 30);
        brokenSet.add(bp1);
        brokenSet.add(bp2);
        System.out.println("BrokenPerson set size (should be 1): " + brokenSet.size());
        System.out.println("Contains bp1: " + brokenSet.contains(bp1));
        System.out.println("Contains bp2: " + brokenSet.contains(bp2));

        Set<CorrectPerson> correctSet = new HashSet<>();
        CorrectPerson cp1 = new CorrectPerson("Alice", 30);
        CorrectPerson cp2 = new CorrectPerson("Alice", 30);
        correctSet.add(cp1);
        correctSet.add(cp2);
        System.out.println("\nCorrectPerson set size (should be 1): " + correctSet.size());
        System.out.println("Contains cp1: " + correctSet.contains(cp1));
        System.out.println("Contains cp2: " + correctSet.contains(cp2));

        // 4. hashCode quality impact
        System.out.println("\n--- hashCode Quality Impact ---");
        System.out.println("Good hashCode: distributes objects across many buckets");
        System.out.println("Poor hashCode: all objects in one bucket -> O(n) lookup");
        System.out.println("Worst case: HashMap becomes linked list");

        // 5. equals() symmetry requirement
        System.out.println("\n--- equals() Symmetry Requirement ---");
        System.out.println("If x.equals(y) then y.equals(x) must be true");
        System.out.println("Violating symmetry breaks collection behavior");
    }

    // Broken: overrides equals() but not hashCode()
    static class BrokenPerson {
        String name;
        int age;

        BrokenPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BrokenPerson p = (BrokenPerson) o;
            return age == p.age && Objects.equals(name, p.name);
        }
        // hashCode() not overridden! Uses default identity-based hashCode
    }

    // Correct: overrides both equals() and hashCode()
    static class CorrectPerson {
        String name;
        int age;

        CorrectPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CorrectPerson p = (CorrectPerson) o;
            return age == p.age && Objects.equals(name, p.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}
