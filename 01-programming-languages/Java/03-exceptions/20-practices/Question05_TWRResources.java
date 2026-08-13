package academy.javaengineering.exceptions.questions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Question 5: Try-with-resources
 *
 * Task: Complete the method using try-with-resources to read a file.
 * Wrap IOException in a RuntimeException and throw it.
 */
public class Question05_TWRResources {

    public static List<String> readLines(String path) {
        // TODO: Use try-with-resources to open BufferedReader
        // Read all lines into a List<String>
        // If IOException occurs, wrap in RuntimeException and throw
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        try {
            List<String> lines = readLines("test.txt");
            System.out.println("Read " + lines.size() + " lines");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
