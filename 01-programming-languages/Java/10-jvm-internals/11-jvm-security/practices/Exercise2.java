package academy.javaengineering.jvm.security;

import java.io.*;

/**
 * Exercise 2: Deserialization Filtering
 *
 * Task: Implement deserialization filters to prevent malicious objects.
 * Run with: java -Djdk.serialFilter='!academy.javaengineering.jvm.security.*' Exercise2
 */
public class Exercise2 implements Serializable {

    private static final long serialVersionUID = 1L;
    private String data;

    public static void main(String[] args) {
        System.out.println("=== Deserialization Filtering ===\n");

        // TODO: Create a serialized object
        // TODO: Attempt to deserialize it
        // TODO: Configure filter to reject specific classes
        // TODO: Verify filter blocks malicious deserialization

        System.out.println("Run with -Djdk.serialFilter to test filtering.");
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // TODO: Implement custom validation in readObject
    }
}
