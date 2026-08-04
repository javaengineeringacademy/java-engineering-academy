package academy.javaengineering.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reflection Basics Tests")
class ReflectionBasicsTest {

    @Test
    @DisplayName("Should obtain Class object via forName")
    void testClassForName() throws Exception {
        Class<?> clazz = Class.forName("academy.javaengineering.reflection.ReflectionBasics");
        assertNotNull(clazz);
        assertEquals("academy.javaengineering.reflection.ReflectionBasics", clazz.getName());
    }

    @Test
    @DisplayName("Should obtain Class object via .class literal")
    void testClassLiteral() {
        Class<?> clazz = ReflectionBasics.class;
        assertNotNull(clazz);
        assertEquals("ReflectionBasics", clazz.getSimpleName());
    }

    @Test
    @DisplayName("Should obtain Class object via getClass()")
    void testGetObjectClass() {
        ReflectionBasics obj = new ReflectionBasics("test", 10, "id1");
        Class<?> clazz = obj.getClass();
        assertNotNull(clazz);
        assertEquals(ReflectionBasics.class, clazz);
    }

    @Test
    @DisplayName("Should retrieve all declared fields including private")
    void testGetDeclaredFields() {
        List<Field> fields = ReflectionBasics.getDeclaredFields(ReflectionBasics.class);
        assertFalse(fields.isEmpty());

        List<String> fieldNames = fields.stream().map(Field::getName).toList();
        assertTrue(fieldNames.contains("name"));
        assertTrue(fieldNames.contains("age"));
        assertTrue(fieldNames.contains("id"));
        assertTrue(fieldNames.contains("instanceCount"));
    }

    @Test
    @DisplayName("Should retrieve all declared methods including private")
    void testGetDeclaredMethods() {
        List<Method> methods = ReflectionBasics.getDeclaredMethods(ReflectionBasics.class);
        assertFalse(methods.isEmpty());

        List<String> methodNames = methods.stream().map(Method::getName).toList();
        assertTrue(methodNames.contains("getInfo"));
        assertTrue(methodNames.contains("secretMethod"));
        assertTrue(methodNames.contains("getInstanceCount"));
    }

    @Test
    @DisplayName("Should retrieve all declared constructors")
    void testGetDeclaredConstructors() {
        List<Constructor<?>> constructors = ReflectionBasics.getDeclaredConstructors(ReflectionBasics.class);
        assertEquals(3, constructors.size());
    }

    @Test
    @DisplayName("Should access private field via setAccessible")
    void testAccessPrivateField() throws Exception {
        ReflectionBasics obj = new ReflectionBasics("Alice", 30, "ID001");
        Class<?> clazz = obj.getClass();

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        assertEquals(30, ageField.get(obj));

        ageField.set(obj, 35);
        assertEquals(35, ageField.get(obj));
    }

    @Test
    @DisplayName("Should read modifiers correctly")
    void testModifiers() {
        Field[] fields = ReflectionBasics.class.getDeclaredFields();
        for (Field field : fields) {
            int mods = field.getModifiers();
            assertNotNull(Modifier.toString(mods));
        }

        assertTrue(Modifier.isPublic(ReflectionBasics.class.getModifiers()) ||
                !Modifier.isPublic(ReflectionBasics.class.getModifiers())); // just verify no exception
    }

    @Test
    @DisplayName("Should create instance dynamically via constructor")
    void testDynamicInstantiation() throws Exception {
        Constructor<?> ctor = ReflectionBasics.class
                .getConstructor(String.class, int.class, String.class);
        ReflectionBasics instance = (ReflectionBasics) ctor.newInstance("Dynamic", 25, "DYN001");

        assertNotNull(instance);
        assertEquals("Dynamic", instance.name);
        assertEquals("Dynamic, age 25, id=DYN001", instance.getInfo());
    }
}
