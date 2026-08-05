package academy.javaengineering.oop.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for sorting mechanisms: Comparable, Comparator, and related utilities.
 */
class SortingTest {

    private List<Student> students;

    @BeforeEach
    void setUp() {
        students = new ArrayList<>(List.of(
                new Student("Charlie", 22, "A", 3.8),
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Diana", 23, "A", 3.7)
        ));
    }

    @Test
    void testComparableSorting() {
        // Test natural ordering (by name) using Comparable
        Collections.sort(students);

        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("Charlie", students.get(2).getName());
        assertEquals("Diana", students.get(3).getName());
    }

    @Test
    void testComparableSortingArray() {
        Student[] studentArray = students.toArray(new Student[0]);

        // Arrays.sort() uses natural ordering
        java.util.Arrays.sort(studentArray);

        assertEquals("Alice", studentArray[0].getName());
        assertEquals("Bob", studentArray[1].getName());
        assertEquals("Charlie", studentArray[2].getName());
        assertEquals("Diana", studentArray[3].getName());
    }

    @Test
    void testComparatorSorting() {
        // Test sorting with Comparator.comparingInt (by age)
        students.sort(Comparator.comparingInt(Student::getAge));

        assertEquals(20, students.get(0).getAge());
        assertEquals(21, students.get(1).getAge());
        assertEquals(22, students.get(2).getAge());
        assertEquals(23, students.get(3).getAge());
    }

    @Test
    void testComparatorSortingByName() {
        // Test Comparator.comparing (by name)
        students.sort(Comparator.comparing(Student::getName));

        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("Charlie", students.get(2).getName());
        assertEquals("Diana", students.get(3).getName());
    }

    @Test
    void testComparatorSortingByGPA() {
        // Test Comparator.comparingDouble (by GPA)
        students.sort(Comparator.comparingDouble(Student::getGpa));

        assertEquals(3.5, students.get(0).getGpa());
        assertEquals(3.7, students.get(1).getGpa());
        assertEquals(3.8, students.get(2).getGpa());
        assertEquals(3.9, students.get(3).getGpa());
    }

    @Test
    void testLambdaSorting() {
        // Test lambda comparator (by name length)
        students.sort((s1, s2) -> Integer.compare(s1.getName().length(), s2.getName().length()));

        // Bob (3), Eve (3), Alice (5), Diana (5)
        assertEquals(3, students.get(0).getName().length());
        assertEquals(3, students.get(1).getName().length());
    }

    @Test
    void testLambdaSortingGPA() {
        // Test lambda comparator (GPA descending)
        students.sort((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()));

        assertEquals(3.9, students.get(0).getGpa());
        assertEquals(3.8, students.get(1).getGpa());
        assertEquals(3.7, students.get(2).getGpa());
        assertEquals(3.5, students.get(3).getGpa());
    }

    @Test
    void testDescendingSort() {
        // Test Collections.reverseOrder()
        Collections.sort(students, Collections.reverseOrder());

        assertEquals("Diana", students.get(0).getName());
        assertEquals("Charlie", students.get(1).getName());
        assertEquals("Bob", students.get(2).getName());
        assertEquals("Alice", students.get(3).getName());
    }

    @Test
    void testDescendingSortWithReversed() {
        // Test Comparator.reversed()
        students.sort(Comparator.comparing(Student::getName).reversed());

        assertEquals("Diana", students.get(0).getName());
        assertEquals("Charlie", students.get(1).getName());
        assertEquals("Bob", students.get(2).getName());
        assertEquals("Alice", students.get(3).getName());
    }

    @Test
    void testDescendingGPASort() {
        // Test descending GPA sort
        students.sort(Comparator.comparingDouble(Student::getGpa).reversed());

        assertEquals(3.9, students.get(0).getGpa());
        assertEquals(3.8, students.get(1).getGpa());
        assertEquals(3.7, students.get(2).getGpa());
        assertEquals(3.5, students.get(3).getGpa());
    }

    @Test
    void testNullHandlingNullsFirst() {
        // Test Comparator.nullsFirst()
        students.add(null);
        students.add(null);

        students.sort(Comparator.nullsFirst(Comparator.comparing(Student::getName)));

        assertNull(students.get(0));
        assertNull(students.get(1));
        assertEquals("Alice", students.get(2).getName());
        assertEquals("Bob", students.get(3).getName());
    }

    @Test
    void testNullHandlingNullsLast() {
        // Test Comparator.nullsLast()
        students.add(null);
        students.add(null);

        students.sort(Comparator.nullsLast(Comparator.comparing(Student::getName)));

        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertNull(students.get(4));
        assertNull(students.get(5));
    }

    @Test
    void testNullHandlingInMiddle() {
        // Test that nulls in the middle are handled correctly
        students.add(1, null);
        students.add(3, null);

        students.sort(Comparator.nullsLast(Comparator.comparing(Student::getName)));

        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("Charlie", students.get(2).getName());
        assertEquals("Diana", students.get(3).getName());
        assertNull(students.get(4));
        assertNull(students.get(5));
    }

    @Test
    void testChainedComparators() {
        // Test byNameThenAge comparator
        students.sort(StudentComparator.byNameThenAge());

        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("Charlie", students.get(2).getName());
        assertEquals("Diana", students.get(3).getName());
    }

    @Test
    void testChainedComparatorsWithSameName() {
        // Test chaining when names are similar
        List<Student> similarStudents = new ArrayList<>(List.of(
                new Student("Alice", 25, "A", 3.9),
                new Student("Alice", 20, "A+", 3.8),
                new Student("Alice", 22, "A", 3.7)
        ));

        similarStudents.sort(StudentComparator.byNameThenAge());

        assertEquals(20, similarStudents.get(0).getAge());
        assertEquals(22, similarStudents.get(1).getAge());
        assertEquals(25, similarStudents.get(2).getAge());
    }

    @Test
    void testGPAThenNameComparator() {
        // Test GPA then name comparator
        students.sort(StudentComparator.byGPAThenName());

        assertEquals(3.9, students.get(0).getGpa());
        assertEquals(3.8, students.get(1).getGpa());
        assertEquals(3.7, students.get(2).getGpa());
        assertEquals(3.5, students.get(3).getGpa());
    }

    @Test
    void testStudentComparatorByGPA() {
        // Test StudentComparator.byGPA() (highest first)
        students.sort(StudentComparator.byGPA());

        assertEquals(3.9, students.get(0).getGpa());
        assertEquals(3.8, students.get(1).getGpa());
        assertEquals(3.7, students.get(2).getGpa());
        assertEquals(3.5, students.get(3).getGpa());
    }

    @Test
    void testStudentComparatorByGrade() {
        // Test StudentComparator.byGrade()
        students.sort(StudentComparator.byGrade());

        assertEquals("A", students.get(0).getGrade());
        assertEquals("A", students.get(1).getGrade());
        assertEquals("A+", students.get(2).getGrade());
        assertEquals("B+", students.get(3).getGrade());
    }

    @Test
    void testEmptyList() {
        // Test sorting an empty list
        List<Student> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> Collections.sort(emptyList));
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testSingleElement() {
        // Test sorting a list with single element
        List<Student> singleList = new ArrayList<>(List.of(
                new Student("Alice", 20, "A", 3.9)
        ));
        Collections.sort(singleList);
        assertEquals(1, singleList.size());
        assertEquals("Alice", singleList.get(0).getName());
    }

    @Test
    void testAlreadySorted() {
        // Test sorting an already sorted list
        List<Student> sortedList = new ArrayList<>(List.of(
                new Student("Alice", 20, "A+", 3.9),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Charlie", 22, "A", 3.8),
                new Student("Diana", 23, "A", 3.7)
        ));

        sortedList.sort(StudentComparator.byName());

        assertEquals("Alice", sortedList.get(0).getName());
        assertEquals("Bob", sortedList.get(1).getName());
        assertEquals("Charlie", sortedList.get(2).getName());
        assertEquals("Diana", sortedList.get(3).getName());
    }

    @Test
    void testReverseSorted() {
        // Test sorting a reverse-sorted list
        List<Student> reverseSortedList = new ArrayList<>(List.of(
                new Student("Diana", 23, "A", 3.7),
                new Student("Charlie", 22, "A", 3.8),
                new Student("Bob", 21, "B+", 3.5),
                new Student("Alice", 20, "A+", 3.9)
        ));

        reverseSortedList.sort(StudentComparator.byName());

        assertEquals("Alice", reverseSortedList.get(0).getName());
        assertEquals("Bob", reverseSortedList.get(1).getName());
        assertEquals("Charlie", reverseSortedList.get(2).getName());
        assertEquals("Diana", reverseSortedList.get(3).getName());
    }
}
