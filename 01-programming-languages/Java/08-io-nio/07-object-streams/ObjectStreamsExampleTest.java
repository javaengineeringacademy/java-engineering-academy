import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("Object Streams Tests")
class ObjectStreamsExampleTest {

    @TempDir
    Path tempDir;

    private String tempPath(String name) {
        return tempDir.resolve(name).toString();
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Should serialize and deserialize object")
        void shouldSerializeAndDeserialize() throws Exception {
            String path = tempPath("object.ser");
            ObjectStreamsExample.User user = new ObjectStreamsExample.User("Alice", 25, "secret");
            ObjectStreamsExample.serialize(path, user);
            ObjectStreamsExample.User deserialized = ObjectStreamsExample.deserialize(path);
            assertEquals("Alice", deserialized.getName());
            assertEquals(25, deserialized.getAge());
            assertNull(deserialized.getPassword()); // transient
        }

        @Test
        @DisplayName("Should create deep copy")
        void shouldCreateDeepCopy() throws Exception {
            ObjectStreamsExample.User original = new ObjectStreamsExample.User("Bob", 30);
            ObjectStreamsExample.User copy = ObjectStreamsExample.deepCopy(original);
            assertEquals("Bob", copy.getName());
            assertEquals(30, copy.getAge());
            assertNotSame(original, copy);
        }
    }

    @Nested
    @DisplayName("Collection Serialization Tests")
    class CollectionTests {

        @Test
        @DisplayName("Should serialize and deserialize collection")
        void shouldSerializeAndDeserializeCollection() throws Exception {
            String path = tempPath("collection.ser");
            java.util.List<ObjectStreamsExample.User> users = java.util.List.of(
                new ObjectStreamsExample.User("Alice", 25),
                new ObjectStreamsExample.User("Bob", 30)
            );
            ObjectStreamsExample.serializeCollection(path, users);
            java.util.List<ObjectStreamsExample.User> deserialized =
                ObjectStreamsExample.deserializeCollection(path);
            assertEquals(2, deserialized.size());
            assertEquals("Alice", deserialized.get(0).getName());
            assertEquals("Bob", deserialized.get(1).getName());
        }
    }
}
