package academy.javaengineering.oop.objectclass;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Object class method demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class ObjectClassTest {

    @Test
    void shouldReturnStringRepresentation() {
        Student student = new Student("Alice", 95);
        String str = student.toString();
        
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("95"));
    }

    @Test
    void shouldCompareByContent() {
        Student s1 = new Student("Alice", 95);
        Student s2 = new Student("Alice", 95);
        Student s3 = new Student("Bob", 88);
        
        assertTrue(s1.equals(s2));
        assertFalse(s1.equals(s3));
        assertFalse(s1.equals(null));
        assertFalse(s1.equals("not a student"));
    }

    @Test
    void shouldHaveConsistentHashCode() {
        Student s1 = new Student("Alice", 95);
        Student s2 = new Student("Alice", 95);
        
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void shouldReturnCorrectClass() {
        Student student = new Student("Alice", 95);
        
        assertEquals(Student.class, student.getClass());
        assertEquals("Student", student.getClass().getSimpleName());
    }

    @Test
    void shouldCloneObject() throws CloneNotSupportedException {
        Student original = new Student("Alice", 95);
        Student cloned = (Student) original.clone();
        
        assertEquals(original, cloned);
        assertNotSame(original, cloned); // Different objects
    }

    @Test
    void shouldSupportInstanceof() {
        Student student = new Student("Alice", 95);
        
        assertTrue(student instanceof Student);
        assertTrue(student instanceof Object);
        assertFalse(student instanceof String);
    }
}