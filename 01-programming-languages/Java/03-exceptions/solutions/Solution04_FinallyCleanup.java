package academy.javaengineering.exceptions.solutions;

import java.io.StringWriter;

/**
 * Solution 4: Finally block for cleanup
 *
 * Use try-finally to guarantee resource cleanup.
 */
public class Solution04_FinallyCleanup {

    public static String writeSafely(String content) {
        StringWriter writer = new StringWriter();
        try {
            writer.write(content);
            return writer.toString();
        } finally {
            writer.close();
        }
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + writeSafely("Hello"));
        System.out.println("Test 2: " + writeSafely(null));
    }
}
