import java.util.Objects;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class BrokenImplementation {
    public static void main(String[] args) {
        System.out.println("=== Broken equals() and hashCode() Implementation ===\n");
        
        // Example 1: Only equals() overridden (hashCode broken)
        System.out.println("1. Only equals() Overridden (hashCode Broken):");
        BrokenPerson bp1 = new BrokenPerson("Alice", 30);
        BrokenPerson bp2 = new BrokenPerson("Alice", 30);
        
        System.out.println("   bp1.equals(bp2) = " + bp1.equals(bp2));
        System.out.println("   bp1.hashCode() = " + bp1.hashCode());
        System.out.println("   bp2.hashCode() = " + bp2.hashCode());
        System.out.println("   bp1.hashCode() == bp2.hashCode() = " + (bp1.hashCode() == bp2.hashCode()));
        System.out.println("   PROBLEM: Objects are equal but have different hashCodes!");
        System.out.println();
        
        // Example 2: HashSet with broken implementation
        System.out.println("2. HashSet with Broken Implementation:");
        Set<BrokenPerson> brokenSet = new HashSet<>();
        brokenSet.add(bp1);
        brokenSet.add(bp2);
        
        System.out.println("   Set size: " + brokenSet.size());
        System.out.println("   Expected: 1 (because bp1.equals(bp2) is true)");
        System.out.println("   Actual: " + brokenSet.size() + " (WRONG!)");
        System.out.println("   PROBLEM: HashSet thinks these are different objects!");
        System.out.println();
        
        // Example 3: HashMap with broken implementation
        System.out.println("3. HashMap with Broken Implementation:");
        Map<BrokenPerson, String> brokenMap = new HashMap<>();
        brokenMap.put(bp1, "Engineer");
        
        System.out.println("   Map size: " + brokenMap.size());
        System.out.println("   brokenMap.get(bp1) = " + brokenMap.get(bp1));
        System.out.println("   brokenMap.get(bp2) = " + brokenMap.get(bp2));
        System.out.println("   Expected: \"Engineer\" (because bp1.equals(bp2) is true)");
        System.out.println("   Actual: " + brokenMap.get(bp2) + " (WRONG!)");
        System.out.println("   PROBLEM: HashMap can't find bp2 even though it equals bp1!");
        System.out.println();
        
        // Example 4: Compare with correct implementation
        System.out.println("4. Comparison with Correct Implementation:");
        CorrectPerson cp1 = new CorrectPerson("Alice", 30);
        CorrectPerson cp2 = new CorrectPerson("Alice", 30);
        
        System.out.println("   cp1.equals(cp2) = " + cp1.equals(cp2));
        System.out.println("   cp1.hashCode() = " + cp1.hashCode());
        System.out.println("   cp2.hashCode() = " + cp2.hashCode());
        System.out.println("   cp1.hashCode() == cp2.hashCode() = " + (cp1.hashCode() == cp2.hashCode()));
        
        Set<CorrectPerson> correctSet = new HashSet<>();
        correctSet.add(cp1);
        correctSet.add(cp2);
        System.out.println("   Correct Set size: " + correctSet.size());
        
        Map<CorrectPerson, String> correctMap = new HashMap<>();
        correctMap.put(cp1, "Engineer");
        System.out.println("   Correct Map.get(cp2) = " + correctMap.get(cp2));
        System.out.println();
        
        // Example 5: The root cause
        System.out.println("5. Root Cause Analysis:");
        System.out.println("   The HashMap uses two steps to find objects:");
        System.out.println("     1. hashCode() to find the bucket");
        System.out.println("     2. equals() to find the exact object in the bucket");
        System.out.println();
        System.out.println("   With broken implementation:");
        System.out.println("     - bp1.hashCode() = " + bp1.hashCode() + " -> goes to bucket X");
        System.out.println("     - bp2.hashCode() = " + bp2.hashCode() + " -> goes to bucket Y");
        System.out.println("     - They go to DIFFERENT buckets, so equals() is never called!");
        System.out.println();
        System.out.println("   With correct implementation:");
        System.out.println("     - cp1.hashCode() = " + cp1.hashCode() + " -> goes to bucket Z");
        System.out.println("     - cp2.hashCode() = " + cp2.hashCode() + " -> goes to bucket Z");
        System.out.println("     - They go to the SAME bucket, so equals() finds the match!");
    }
    
    // Broken implementation: only equals() overridden
    static class BrokenPerson {
        private String name;
        private int age;
        
        public BrokenPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BrokenPerson person = (BrokenPerson) o;
            return age == person.age && Objects.equals(name, person.name);
        }
        // NO hashCode() override! Uses default Object.hashCode()
    }
    
    // Correct implementation: both equals() and hashCode() overridden
    static class CorrectPerson {
        private String name;
        private int age;
        
        public CorrectPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CorrectPerson person = (CorrectPerson) o;
            return age == person.age && Objects.equals(name, person.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}
