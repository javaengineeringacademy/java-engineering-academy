package com.javaacademy.sprint1.basics;

/**
 * HelloWorld - The classic first Java program.
 *
 * <p>This demonstrates the basic structure of a Java program:
 * <ul>
 *   <li>Package declaration</li>
 *   <li>Class declaration</li>
 *   <li>Main method - entry point of execution</li>
 *   <li>Statement with output</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b> Like the "Hello, World!" sign
 * you put on a new house - it's the first sign of life in a new program.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class HelloWorld {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private HelloWorld() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Main method - entry point of the Java application.
     *
     * <p><b>Signature breakdown:</b>
     * <ul>
     *   <li>{@code public} - accessible from anywhere (JVM calls this)</li>
     *   <li>{@code static} - belongs to class, not instance (JVM calls without object)</li>
     *   <li>{@code void} - returns nothing</li>
     *   <li>{@code main} - special name JVM looks for</li>
     *   <li>{@code String[] args} - command-line arguments</li>
     * </ul>
     *
     * @param args command-line arguments (not used in this example)
     */
    public static void main(String[] args) {
        // Print to standard output (console)
        System.out.println("Hello, World!");
        System.out.println("Welcome to Java Engineering Academy!");

        // Expected output:
        // Hello, World!
        // Welcome to Java Engineering Academy!
    }
}