import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeErasureTest {

    @Test
    void testTypeErasureSameRuntimeClass() {
        List<String> stringList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        assertEquals(stringList.getClass(), intList.getClass());
    }

    @Test
    void testRawTypeAccess() {
        List<String> typedList = new ArrayList<>();
        typedList.add("Hello");
        List rawList = typedList;
        assertSame(typedList, rawList);
    }

    @Test
    void testBridgeMethodWorks() {
        TypeErasureDemo.Container<String> container = new TypeErasureDemo.StringContainer();
        container.set("Test");
        assertEquals("Test", container.get());
    }

    @Test
    void testFindMaxInteger() {
        List<Integer> list = List.of(5, 2, 8, 1, 9);
        assertEquals(9, TypeErasureDemo.findMax(list));
    }

    @Test
    void testGenericClassSameRuntimeType() {
        Box<String> stringBox = new Box<>();
        Box<Integer> intBox = new Box<>();
        assertEquals(stringBox.getClass(), intBox.getClass());
    }

    @Test
    void testListTypeErasure() {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        assertEquals(ArrayList.class, strings.getClass());
        assertEquals(ArrayList.class, integers.getClass());
    }
}
