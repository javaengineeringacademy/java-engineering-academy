package academy.javaengineering.oop.comparison;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@DisplayName("Comparable vs Comparator Tests")
class ComparisonTest {

    @Test
    @DisplayName("Comparable should sort by natural ordering")
    void testComparable() {
        List<ComparableVsComparator.Employee> employees = new ArrayList<>(Arrays.asList(
            new ComparableVsComparator.Employee("Charlie", "Engineering", 105000),
            new ComparableVsComparator.Employee("Alice", "Marketing", 75000),
            new ComparableVsComparator.Employee("Bob", "Engineering", 95000)
        ));

        Collections.sort(employees);
        assertEquals("Alice", employees.get(0).getName());
        assertEquals("Bob", employees.get(1).getName());
        assertEquals("Charlie", employees.get(2).getName());
    }

    @Test
    @DisplayName("Comparator should sort by custom criteria")
    void testComparator() {
        List<ComparableVsComparator.Employee> employees = new ArrayList<>(Arrays.asList(
            new ComparableVsComparator.Employee("Charlie", "Engineering", 105000),
            new ComparableVsComparator.Employee("Alice", "Marketing", 75000),
            new ComparableVsComparator.Employee("Bob", "Engineering", 95000)
        ));

        employees.sort(ComparableVsComparator.EmployeeComparators.BY_SALARY_DESC);
        assertEquals(105000, employees.get(0).getSalary());
        assertEquals(95000, employees.get(1).getSalary());
        assertEquals(75000, employees.get(2).getSalary());
    }

    @Test
    @DisplayName("Comparator should handle multiple criteria")
    void testMultiCriteria() {
        List<ComparableVsComparator.Employee> employees = new ArrayList<>(Arrays.asList(
            new ComparableVsComparator.Employee("Charlie", "Engineering", 105000),
            new ComparableVsComparator.Employee("Alice", "Engineering", 95000),
            new ComparableVsComparator.Employee("Bob", "Marketing", 85000)
        ));

        employees.sort(ComparableVsComparator.EmployeeComparators.BY_DEPT_AND_NAME);
        assertEquals("Alice", employees.get(0).getName());
        assertEquals("Charlie", employees.get(1).getName());
        assertEquals("Bob", employees.get(2).getName());
    }
}
