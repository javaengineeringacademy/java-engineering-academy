package academy.javaengineering.exercises;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Exercises: Sorting (Comparator, Comparable)
 *
 * Complete the TODO sections below.
 */
public class SortingExercises {

    // TODO 1: Create a Student class that implements Comparable
    // Fields: name (String), grade (double), enrollmentYear (int)
    // Natural ordering: by grade descending (highest first), then by name alphabetically
    public static class Student implements Comparable<Student> {
        private final String name;
        private final double grade;
        private final int enrollmentYear;

        public Student(String name, double grade, int enrollmentYear) {
            this.name = name;
            this.grade = grade;
            this.enrollmentYear = enrollmentYear;
        }

        public String getName() { return name; }
        public double getGrade() { return grade; }
        public int getEnrollmentYear() { return enrollmentYear; }

        @Override
        public int compareTo(Student other) {
            // TODO: implement this - by grade descending, then by name
            return 0;
        }

        @Override
        public String toString() {
            return name + "(" + grade + ")";
        }
    }

    // TODO 2: Create comparators for different sorting criteria
    // Sort by name alphabetically (case-insensitive)
    public Comparator<Student> byName() {
        // TODO: implement this
        return null;
    }

    // Sort by enrollment year ascending
    public Comparator<Student> byEnrollmentYear() {
        // TODO: implement this
        return null;
    }

    // Sort by grade range (letter grades): A (90+) > B (80+) > C (70+) > D (60+) > F (<60)
    // Within same letter grade, sort by grade descending
    public Comparator<Student> byLetterGrade() {
        // TODO: implement this
        return null;
    }

    // TODO 3: Sort a list of strings by length, then alphabetically
    public List<String> sortByLengthThenAlpha(List<String> strings) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 4: Sort a list of integers such that even numbers come first (ascending),
    // then odd numbers (ascending)
    public List<Integer> evenFirst(List<Integer> numbers) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 5: Sort a list of words by the number of vowels they contain (descending)
    // Words with same vowel count should be sorted alphabetically
    public List<String> sortByVowelCount(List<String> words) {
        // TODO: implement this
        return new ArrayList<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        SortingExercises exercises = new SortingExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== SortingExercises Tests ===\n");

        // Test 1: Comparable
        total++;
        List<Student> students = new ArrayList<>(List.of(
            new Student("Alice", 85.0, 2022),
            new Student("Bob", 92.0, 2021),
            new Student("Charlie", 85.0, 2023),
            new Student("Diana", 92.0, 2022)
        ));
        Collections.sort(students);
        if (students.get(0).getGrade() == 92.0
            && students.get(1).getGrade() == 92.0
            && "Alice".equals(students.get(2).getName())
            && students.get(2).getGrade() == 85.0) {
            System.out.println("Test 1 PASSED: Comparable sorting");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: Comparable sorting - got " + students);
        }

        // Test 2: Comparator by name
        total++;
        List<Student> byName = new ArrayList<>(List.of(
            new Student("Charlie", 85.0, 2022),
            new Student("Alice", 92.0, 2021),
            new Student("Bob", 88.0, 2023)
        ));
        byName.sort(exercises.byName());
        if ("Alice".equals(byName.get(0).getName())
            && "Bob".equals(byName.get(1).getName())
            && "Charlie".equals(byName.get(2).getName())) {
            System.out.println("Test 2 PASSED: Comparator byName");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: Comparator byName - got " + byName);
        }

        // Test 3: Sort by length then alpha
        total++;
        List<String> words = List.of("banana", "pie", "apple", "fig", "cherry");
        List<String> sorted = exercises.sortByLengthThenAlpha(new ArrayList<>(words));
        if ("pie".equals(sorted.get(0))
            && "fig".equals(sorted.get(1))
            && "apple".equals(sorted.get(2))) {
            System.out.println("Test 3 PASSED: sortByLengthThenAlpha");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: sortByLengthThenAlpha - got " + sorted);
        }

        // Test 4: Even first sort
        total++;
        List<Integer> nums = List.of(5, 3, 8, 1, 4, 7, 2);
        List<Integer> evenFirst = exercises.evenFirst(new ArrayList<>(nums));
        if (evenFirst.get(0) == 2 && evenFirst.get(1) == 4 && evenFirst.get(2) == 8
            && evenFirst.get(3) == 1) {
            System.out.println("Test 4 PASSED: evenFirst");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: evenFirst - got " + evenFirst);
        }

        // Test 5: Sort by vowel count
        total++;
        List<String> vowelWords = List.of("sky", "education", "a", "beautiful");
        List<String> byVowels = exercises.sortByVowelCount(new ArrayList<>(vowelWords));
        if ("beautiful".equals(byVowels.get(0)) && "education".equals(byVowels.get(1))) {
            System.out.println("Test 5 PASSED: sortByVowelCount");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: sortByVowelCount - got " + byVowels);
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
