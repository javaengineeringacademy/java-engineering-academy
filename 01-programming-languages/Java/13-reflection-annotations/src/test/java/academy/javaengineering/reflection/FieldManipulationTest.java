package academy.javaengineering.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Field Manipulation Tests")
class FieldManipulationTest {

    @Test
    @DisplayName("Should read private field value")
    void testReadPrivateField() throws Exception {
        FieldManipulation obj = new FieldManipulation("pub", "priv", 42);
        Object value = FieldManipulation.readField(obj, "privateField");
        assertEquals("priv", value);
    }

    @Test
    @DisplayName("Should write private field value")
    void testWritePrivateField() throws Exception {
        FieldManipulation obj = new FieldManipulation();
        FieldManipulation.writeField(obj, "privateField", "newValue");
        Object value = FieldManipulation.readField(obj, "privateField");
        assertEquals("newValue", value);
    }

    @Test
    @DisplayName("Should read static field")
    void testReadStaticField() throws Exception {
        Field field = FieldManipulation.class.getDeclaredField("staticField");
        field.setAccessible(true);
        Object value = field.get(null);
        assertNotNull(value);
        assertTrue(value instanceof String);
    }

    @Test
    @DisplayName("Should convert object to map")
    void testObjectToMap() throws Exception {
        FieldManipulation obj = new FieldManipulation("pub", "priv", 99);
        Map<String, Object> map = FieldManipulation.objectToMap(obj);
        assertEquals("pub", map.get("publicField"));
        assertEquals("priv", map.get("privateField"));
        assertEquals(99, map.get("numberField"));
        assertTrue(map.containsKey("protectedField"));
        assertTrue(map.containsKey("packageField"));
    }

    @Test
    @DisplayName("Should populate object from map")
    void testMapToObject() throws Exception {
        FieldManipulation target = new FieldManipulation();
        Map<String, Object> data = Map.of(
                "publicField", "fromMap",
                "numberField", 777
        );
        FieldManipulation.mapToObject(data, target);
        assertEquals("fromMap", target.publicField);
        java.lang.reflect.Field nf = FieldManipulation.class.getDeclaredField("numberField");
        nf.setAccessible(true);
        assertEquals(777, nf.getInt(target));
    }

    @Test
    @DisplayName("Should copy fields between objects")
    void testCopyFields() throws Exception {
        FieldManipulation source = new FieldManipulation("src", "srcPriv", 111);
        FieldManipulation target = new FieldManipulation();
        FieldManipulation.copyAllFields(source, target);
        assertEquals("src", target.publicField);
    }

    @Test
    @DisplayName("Should read final field value")
    void testReadFinalField() throws Exception {
        FieldManipulation obj = new FieldManipulation();
        Field finalField = FieldManipulation.class.getDeclaredField("finalField");
        finalField.setAccessible(true);
        assertEquals("immutable", finalField.get(obj));
    }

    @Test
    @DisplayName("Should read integer field via setInt")
    void testReadIntField() throws Exception {
        FieldManipulation obj = new FieldManipulation("a", "b", 42);
        Field numberField = FieldManipulation.class.getDeclaredField("numberField");
        numberField.setAccessible(true);
        assertEquals(42, numberField.getInt(obj));
    }

    @Test
    @DisplayName("Should write integer field via setInt")
    void testWriteIntField() throws Exception {
        FieldManipulation obj = new FieldManipulation();
        Field numberField = FieldManipulation.class.getDeclaredField("numberField");
        numberField.setAccessible(true);
        numberField.setInt(obj, 200);
        assertEquals(200, numberField.getInt(obj));
    }

    @Test
    @DisplayName("Should copy fields returns changed fields map")
    void testCopyFieldsReturnsChanges() throws Exception {
        FieldManipulation source = new FieldManipulation("newPub", "newPriv", 500);
        FieldManipulation target = new FieldManipulation();
        Map<String, Object> changes = FieldManipulation.copyAllFields(source, target);
        assertNotNull(changes);
        assertTrue(changes.containsKey("publicField"));
        assertEquals("newPub", changes.get("publicField"));
    }
}
