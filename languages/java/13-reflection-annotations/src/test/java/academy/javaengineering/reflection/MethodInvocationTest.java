package academy.javaengineering.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Method Invocation Tests")
class MethodInvocationTest {

    @Test
    @DisplayName("Should invoke public method via reflection")
    void testInvokePublicMethod() throws Exception {
        MethodInvocation obj = new MethodInvocation();
        Method greet = obj.getClass().getMethod("greet", String.class);
        String result = (String) greet.invoke(obj, "JUnit");
        assertEquals("Hello, JUnit!", result);
    }

    @Test
    @DisplayName("Should invoke private method via reflection")
    void testInvokePrivateMethod() throws Exception {
        MethodInvocation obj = new MethodInvocation();
        Method privateMethod = obj.getClass().getDeclaredMethod("internalProcess", String.class);
        privateMethod.setAccessible(true);
        String result = (String) privateMethod.invoke(obj, "hello");
        assertEquals("Processed: HELLO", result);
    }

    @Test
    @DisplayName("Should invoke static method")
    void testInvokeStaticMethod() throws Exception {
        Method staticMethod = MethodInvocation.class.getMethod("staticMethod", int.class);
        Object result = staticMethod.invoke(null, 5);
        assertEquals(25, result);
    }

    @Test
    @DisplayName("Should invoke overloaded methods")
    void testInvokeOverloadedMethods() throws Exception {
        MethodInvocation obj = new MethodInvocation();

        Method addInt = obj.getClass().getMethod("add", int.class, int.class);
        assertEquals(7, addInt.invoke(obj, 3, 4));

        Method addDouble = obj.getClass().getMethod("add", double.class, double.class);
        assertEquals(6.0, addDouble.invoke(obj, 2.5, 3.5));
    }

    @Test
    @DisplayName("Should find methods by name")
    void testFindMethodsByName() {
        List<Method> processMethods = Arrays.stream(MethodInvocation.class.getDeclaredMethods())
                .filter(m -> m.getName().equals("process"))
                .toList();
        assertEquals(3, processMethods.size());
    }

    @Test
    @DisplayName("Should use MethodHandle for invocation")
    void testMethodHandleInvocation() throws Throwable {
        MethodInvocation obj = new MethodInvocation();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(String.class, String.class);
        MethodHandle handle = lookup.findVirtual(MethodInvocation.class, "greet", type);
        String result = (String) handle.invoke(obj, "Handle");
        assertEquals("Hello, Handle!", result);
    }

    @Test
    @DisplayName("Should invoke method with varargs")
    void testInvokeVarargsMethod() throws Exception {
        MethodInvocation obj = new MethodInvocation();
        Method sumMethod = obj.getClass().getMethod("sum", int[].class);
        int result = (int) sumMethod.invoke(obj, (Object) new int[]{1, 2, 3, 4, 5});
        assertEquals(15, result);
    }

    @Test
    @DisplayName("Should use MethodType to describe signatures")
    void testMethodTypeCreation() {
        MethodType voidType = MethodType.methodType(void.class);
        MethodType stringType = MethodType.methodType(String.class);
        MethodType addType = MethodType.methodType(int.class, int.class, int.class);

        assertEquals(void.class, voidType.returnType());
        assertEquals(String.class, stringType.returnType());
        assertEquals(int.class, addType.returnType());
        assertEquals(2, addType.parameterCount());
        assertEquals(List.of(int.class, int.class), addType.parameterList());
    }

    @Test
    @DisplayName("Should find methods by return type")
    void testFindMethodsByReturnType() {
        List<Method> stringMethods = Arrays.stream(MethodInvocation.class.getDeclaredMethods())
                .filter(m -> m.getReturnType() == String.class)
                .toList();
        assertFalse(stringMethods.isEmpty());
        assertTrue(stringMethods.stream().allMatch(m -> m.getReturnType() == String.class));
    }
}
