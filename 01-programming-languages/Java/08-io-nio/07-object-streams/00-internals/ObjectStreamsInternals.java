package academy.javaengineering.io.internals;

import java.io.*;

public class ObjectStreamsInternals {

    public static void main(String[] args) {
        System.out.println("=== Object Streams Internals ===\n");

        // 1. ObjectOutputStream
        System.out.println("--- ObjectOutputStream ---");
        System.out.println("Writes entire objects to stream");
        System.out.println("Object must implement Serializable");
        System.out.println("writeObject() method");

        // 2. ObjectInputStream
        System.out.println("\n--- ObjectInputStream ---");
        System.out.println("Reads objects from stream");
        System.out.println("readObject() method");
        System.out.println("Returns Object, requires casting");

        // 3. Serialization
        System.out.println("\n--- Serialization ---");
        System.out.println("Converts object to byte stream");
        System.out.println("transient: exclude field from serialization");
        System.out.println("serialVersionUID: version control");
    }
}
