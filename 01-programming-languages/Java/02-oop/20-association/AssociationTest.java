import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Association Tests")
class AssociationTest {

    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("Dr. Smith", "Math");
        student = new Student("Alice", "S001");
    }

    @Test
    @DisplayName("Teacher and student are separate objects")
    void separateObjects() {
        assertNotSame(teacher, student);
        assertEquals("Dr. Smith", teacher.getName());
        assertEquals("Alice", student.getName());
    }

    @Test
    @DisplayName("Bidirectional association via addStudent")
    void bidirectional() {
        teacher.addStudent(student);
        assertTrue(teacher.getStudents().contains(student));
        assertTrue(student.getTeachers().contains(teacher));
    }

    @Test
    @DisplayName("Multiple students per teacher")
    void multipleStudents() {
        Student s2 = new Student("Bob", "S002");
        teacher.addStudent(student);
        teacher.addStudent(s2);
        assertEquals(2, teacher.getStudents().size());
    }

    @Test
    @DisplayName("Multiple teachers per student")
    void multipleTeachers() {
        Teacher t2 = new Teacher("Dr. Jones", "Science");
        teacher.addStudent(student);
        t2.addStudent(student);
        assertEquals(2, student.getTeachers().size());
    }

    @Test
    @DisplayName("getRoster returns formatted string")
    void roster() {
        teacher.addStudent(student);
        String roster = teacher.getRoster();
        assertTrue(roster.contains("Alice"));
        assertTrue(roster.contains("Math"));
    }

    @Test
    @DisplayName("getSchedule returns formatted string")
    void schedule() {
        teacher.addStudent(student);
        String schedule = student.getSchedule();
        assertTrue(schedule.contains("Math"));
        assertTrue(schedule.contains("Dr. Smith"));
    }
}