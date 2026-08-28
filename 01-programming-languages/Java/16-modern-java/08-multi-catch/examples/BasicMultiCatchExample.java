package academy.javaengineering.modern.multicatch;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Basic multi-catch examples.
 */
public class BasicMultiCatchExample {

    public static void main(String[] args) {
        // Basic multi-catch
        System.out.println("=== Basic Multi-catch ===");
        try {
            riskyOperation();
        } catch (IOException | SQLException | ParseException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }

        // Multi-catch with different exception types
        System.out.println("\n=== Different Exception Types ===");
        testMultiCatch(IOException.class);
        testMultiCatch(SQLException.class);
        testMultiCatch(ParseException.class);

        // Multi-catch in loop
        System.out.println("\n=== Multi-catch in Loop ===");
        String[] inputs = {"123", "abc", "456", "def"};
        for (String input : inputs) {
            try {
                int value = Integer.parseInt(input);
                System.out.println("Parsed: " + value);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error with '" + input + "': " + e.getMessage());
            }
        }

        // Multi-catch with finally
        System.out.println("\n=== Multi-catch with Finally ===");
        try {
            riskyOperation();
        } catch (IOException | SQLException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("Finally block executed");
        }
    }

    static void riskyOperation() throws IOException, SQLException, ParseException {
        double random = Math.random();
        if (random < 0.33) {
            throw new IOException("IO error occurred");
        } else if (random < 0.66) {
            throw new SQLException("SQL error occurred");
        } else {
            throw new ParseException("Parse error occurred", 0);
        }
    }

    static void testMultiCatch(Class<? extends Exception> exceptionType) {
        try {
            if (exceptionType == IOException.class) {
                throw new IOException("IO exception");
            } else if (exceptionType == SQLException.class) {
                throw new SQLException("SQL exception");
            } else if (exceptionType == ParseException.class) {
                throw new ParseException("Parse exception", 0);
            }
        } catch (IOException | SQLException | ParseException e) {
            System.out.println("Caught " + exceptionType.getSimpleName() + ": " + e.getMessage());
        }
    }
}
