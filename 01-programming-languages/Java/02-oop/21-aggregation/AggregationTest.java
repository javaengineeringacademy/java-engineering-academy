import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Aggregation Tests")
class AggregationTest {

    private University university;

    @BeforeEach
    void setUp() {
        university = new University("MIT");
        Department cs = new Department("Computer Science", List.of("Java", "Python", "Algorithms"));
        Department math = new Department("Mathematics", List.of("Calculus", "Linear Algebra"));
        university.addDepartment(cs);
        university.addDepartment(math);
    }

    @Test
    @DisplayName("University contains departments")
    void hasDepartments() {
        assertEquals(2, university.getDepartments().size());
    }

    @Test
    @DisplayName("Departments exist independently")
    independentDepartments() {
        Department dept = new Department("Physics", List.of("Mechanics"));
        dept.addCourse("Thermodynamics");
        assertEquals(2, dept.getCourseCount());
    }

    @Test
    @DisplayName("getTotalCourses sums all courses")
    void totalCourses() {
        assertEquals(5, university.getTotalCourses());
    }

    @Test
    @DisplayName("getCatalog returns formatted string")
    void catalog() {
        String catalog = university.getCatalog();
        assertTrue(catalog.contains("MIT"));
        assertTrue(catalog.contains("Computer Science"));
        assertTrue(catalog.contains("Mathematics"));
    }

    @Test
    @DisplayName("getName returns university name")
    void getName() {
        assertEquals("MIT", university.getName());
    }

    @Test
    @DisplayName("getDepartments returns defensive copy")
    void defensiveCopy() {
        List<Department> depts = university.getDepartments();
        depts.clear();
        assertEquals(2, university.getDepartments().size());
    }
}