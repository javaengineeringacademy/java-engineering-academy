package academy.javaengineering.jvm.security;

import java.io.*;

/**
 * Solution 2: Deserialization Filtering
 */
public class Solution2 implements Serializable {

    private static final long serialVersionUID = 1L;
    private String data;

    public Solution2(String data) {
        this.data = data;
    }

    public static void main(String[] args) {
        System.out.println("=== Deserialization Filtering ===\n");

        try {
            // Serialize
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new Solution2("test data"));
            oos.close();
            System.out.println("Serialized " + baos.size() + " bytes");

            // Deserialize with filter
            System.out.println("\nDeserialization filter test:");
            System.out.println("Run with: java -Djdk.serialFilter='!academy.javaengineering.jvm.security.*' Solution2");

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Solution2 obj = (Solution2) ois.readObject();
            System.out.println("Deserialized: " + obj.data);

        } catch (Exception e) {
            System.out.println("Deserialization blocked: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // Validate deserialized data
        if (data == null || data.isEmpty()) {
            throw new InvalidObjectException("Invalid data: must not be null or empty");
        }
        System.out.println("Custom validation passed");
    }
}
