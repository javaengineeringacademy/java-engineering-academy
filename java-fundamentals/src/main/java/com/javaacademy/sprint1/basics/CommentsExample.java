package com.javaacademy.sprint1.basics;

/**
 * CommentsExample - Demonstrates Java comment types.
 * 
 * <p>Java supports three types of comments:
 * <ul>
 *   <li><b>Single-line:</b> {@code //} - for brief explanations</li>
 *   <li><b>Multi-line:</b> {@code /* ... */} - for longer descriptions</li>
 *   <li><b>Javadoc:</b> {@code /** ... */} - for API documentation (generates HTML)</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like sticky notes on code - 
 * single-line = quick note, multi-line = detailed explanation, 
 * Javadoc = official manual for other developers.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class CommentsExample {

    /** 
     * Javadoc comment for the class - describes purpose and usage.
     * This appears in generated API documentation.
     */

    // Single-line comment: explains the next line
    private static final String GREETING = "Hello";

    /**
     * Javadoc comment for a field - explains what the constant represents.
     * Use {@code @since} to indicate version when added.
     */
    private static final int VERSION = 1;

    /**
     * Private constructor - utility class pattern.
     */
    private CommentsExample() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Demonstrates different comment styles.
     * 
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Single-line comment: print greeting
        System.out.println(GREETING); // Inline comment
        
        /*
         * Multi-line comment:
         * This block explains a complex operation.
         * Useful for algorithms or business logic explanations.
         * 
         * Note: Nested multi-line comments are NOT allowed in Java!
         */
        System.out.println("Version: " + VERSION);

        /**
         * Javadoc comment inside method - generally not used here.
         * Javadoc is meant for class/interface/method/field declarations.
         */
        
        // Expected output:
        // Hello
        // Version: 1
    }
}