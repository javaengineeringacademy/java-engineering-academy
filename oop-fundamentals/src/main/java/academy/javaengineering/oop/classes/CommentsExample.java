package academy.javaengineering.oop.classes;

/**
 * Demonstrates comments in Java.
 */
public final class CommentsExample {

    /**
     * Main method - program entry point.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Single-line comment: print greeting
        System.out.println("Hello, World!");

        /*
         * Multi-line comment:
         * This demonstrates block comment syntax
         * which can span multiple lines
         */

        /**
         * Javadoc comment for the greeting variable
         * Describes the purpose of this variable
         */
        String greeting = "Welcome to Java Engineering Academy!";

        System.out.println(greeting); // Print greeting
    }
}