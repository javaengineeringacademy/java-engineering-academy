import java.io.*;
import java.util.*;

/**
 * Object Streams in Java IO - Demonstrates serialization operations.
 *
 * <p>This class provides comprehensive examples of ObjectOutputStream
 * and ObjectInputStream for serializing and deserializing Java objects.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class ObjectStreamsExample {

    private ObjectStreamsExample() {
        // Utility class
    }

    // ==================== Basic Serialization ====================

    /**
     * Serializes an object to a file.
     *
     * @param path the file path
     * @param object the object to serialize
     * @throws IOException if serialization fails
     */
    public static void serialize(String path, Serializable object)
            throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path))) {
            oos.writeObject(object);
        }
    }

    /**
     * Deserializes an object from a file.
     *
     * @param path the file path
     * @return the deserialized object
     * @throws IOException if deserialization fails
     * @throws ClassNotFoundException if class not found
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String path)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(path))) {
            return (T) ois.readObject();
        }
    }

    // ==================== Deep Copy ====================

    /**
     * Creates a deep copy of a serializable object.
     *
     * @param object the object to copy
     * @return deep copy of the object
     * @throws IOException if copy fails
     * @throws ClassNotFoundException if class not found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepCopy(T object)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {
            return (T) ois.readObject();
        }
    }

    // ==================== Collection Operations ====================

    /**
     * Serializes a collection to a file.
     *
     * @param path the file path
     * @param collection the collection to serialize
     * @throws IOException if serialization fails
     */
    public static <T extends Serializable> void serializeCollection(
            String path, Collection<T> collection) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path))) {
            oos.writeObject(new ArrayList<>(collection));
        }
    }

    /**
     * Deserializes a collection from a file.
     *
     * @param path the file path
     * @return the deserialized collection
     * @throws IOException if deserialization fails
     * @throws ClassNotFoundException if class not found
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> deserializeCollection(String path)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(path))) {
            return (List<T>) ois.readObject();
        }
    }

    // ==================== Main Method ====================

    /**
     * Demonstrates object stream operations.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Object Streams Demo ===");

        String tempDir = System.getProperty("java.io.tmpdir");
        String objectFile = tempDir +
            File.separator + "object.ser";
        String collectionFile = tempDir +
            File.separator + "collection.ser";

        try {
            // Basic serialization
            System.out.println("Basic serialization:");
            User user = new User("Alice", 25, "secret123");
            serialize(objectFile, user);
            User deserialized = deserialize(objectFile);
            System.out.println("  Original: " + user);
            System.out.println("  Deserialized: " + deserialized);
            System.out.println("  Password (transient): " +
                deserialized.getPassword());

            // Deep copy
            System.out.println("\nDeep copy:");
            User copy = deepCopy(user);
            System.out.println("  Original: " + user);
            System.out.println("  Copy: " + copy);
            System.out.println("  Same object? " + (user == copy));

            // Collection serialization
            System.out.println("\nCollection serialization:");
            List<User> users = List.of(
                new User("Alice", 25),
                new User("Bob", 30),
                new User("Charlie", 35)
            );
            serializeCollection(collectionFile, users);
            List<User> deserializedUsers =
                deserializeCollection(collectionFile);
            System.out.println("  Original: " + users.size() + " users");
            System.out.println("  Deserialized: " +
                deserializedUsers.size() + " users");
            deserializedUsers.forEach(u ->
                System.out.println("    " + u));

            // Cleanup
            new File(objectFile).delete();
            new File(collectionFile).delete();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // ==================== User Class ====================

    /**
     * User class for demonstration.
     */
    static class User implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int age;
        private transient String password;

        User(String name, int age) {
            this(name, age, null);
        }

        User(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        String getName() { return name; }
        String getPassword() { return password; }

        @Override
        public String toString() {
            return String.format("User{name='%s', age=%d}", name, age);
        }
    }
}
