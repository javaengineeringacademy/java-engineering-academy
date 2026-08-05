import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ObjectDemo Tests")
class ObjectDemoTest {

    @Test
    @DisplayName("Object creation and field access")
    void objectCreation() {
        ObjectDemo obj = new ObjectDemo("test");
        assertEquals("test", obj.getData());
    }

    @Test
    @DisplayName("Reference assignment shares same object")
    void sharedReference() {
        ObjectDemo original = new ObjectDemo("original");
        ObjectDemo reference = original;
        reference.setData("changed");

        assertEquals("changed", original.getData(),
                "Both references point to same object");
    }

    @Test
    @DisplayName("Independent objects are not same reference")
    void independentObjects() {
        ObjectDemo a = new ObjectDemo("a");
        ObjectDemo b = new ObjectDemo("b");

        assertNotSame(a, b);
        assertNotEquals(a.getData(), b.getData());
    }

    @Test
    @DisplayName("Null reference throws NullPointerException")
    void nullReference() {
        ObjectDemo nullObj = null;
        assertThrows(NullPointerException.class, () -> nullObj.getData());
    }

    @Test
    @DisplayName("Method can modify object state via reference")
    void modifyObjectState() {
        ObjectDemo obj = new ObjectDemo("before");
        ObjectDemo.modifyObject(obj);
        assertEquals("modified by method", obj.getData());
    }

    @Test
    @DisplayName("Shared reference returned from method")
    void sharedReferenceFromMethod() {
        ObjectDemo result = ObjectDemo.createSharedReference();
        assertEquals("modified", result.getData());
    }
}