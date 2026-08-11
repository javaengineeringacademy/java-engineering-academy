package academy.javaengineering.exceptions.solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Solution 5: Try-with-resources
 *
 * Use try-with-resources to read a file and wrap IOException in RuntimeException.
 */
public class Solution05_TWRResources {

    public static List<String> readLines(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
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
