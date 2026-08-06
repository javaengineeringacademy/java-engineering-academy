import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Character Streams in Java IO - Demonstrates text data operations.
 *
 * <p>This class provides comprehensive examples of character stream operations
 * including FileReader, FileWriter, BufferedReader, BufferedWriter,
 * InputStreamReader, and OutputStreamWriter.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class CharacterStreamsExample {

    private CharacterStreamsExample() {
        // Utility class
    }

    // ==================== Basic Operations ====================

    /**
     * Writes text to a file using character streams.
     *
     * @param path the file path
     * @param content the text content
     * @throws IOException if write fails
     */
    public static void writeText(String path, String content)
            throws IOException {
        try (Writer writer = new FileWriter(path)) {
            writer.write(content);
        }
    }

    /**
     * Reads entire text file content.
     *
     * @param path the file path
     * @return file content as string
     * @throws IOException if read fails
     */
    public static String readText(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    // ==================== Encoding Operations ====================

    /**
     * Writes text to a file with specific encoding.
     *
     * @param path the file path
     * @param content the text content
     * @param charset the character set to use
     * @throws IOException if write fails
     */
    public static void writeWithEncoding(String path, String content,
            Charset charset) throws IOException {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(path), charset)) {
            writer.write(content);
        }
    }

    /**
     * Reads text file with specific encoding.
     *
     * @param path the file path
     * @param charset the character set to use
     * @return file content as string
     * @throws IOException if read fails
     */
    public static String readWithEncoding(String path, Charset charset)
            throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(path), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    // ==================== Line Operations ====================

    /**
     * Reads all lines from a file.
     *
     * @param path the file path
     * @return list of lines
     * @throws IOException if read fails
     */
    public static List<String> readLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Writes lines to a file.
     *
     * @param path the file path
     * @param lines the lines to write
     * @throws IOException if write fails
     */
    public static void writeLines(String path, List<String> lines)
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(path))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Appends a line to a file.
     *
     * @param path the file path
     * @param line the line to append
     * @throws IOException if write fails
     */
    public static void appendLine(String path, String line)
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(path, true))) {
            writer.write(line);
            writer.newLine();
        }
    }

    // ==================== Statistics ====================

    /**
     * Calculates text statistics.
     *
     * @param path the file path
     * @return map of statistics
     * @throws IOException if read fails
     */
    public static Map<String, Long> calculateStats(String path)
            throws IOException {
        Map<String, Long> stats = new LinkedHashMap<>();
        long lines = 0, words = 0, characters = 0;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                words += line.split("\\s+").length;
                characters += line.length();
            }
        }

        stats.put("lines", lines);
        stats.put("words", words);
        stats.put("characters", characters);
        return stats;
    }

    /**
     * Counts word frequencies.
     *
     * @param path the file path
     * @return map of word to count
     * @throws IOException if read fails
     */
    public static Map<String, Integer> countWords(String path)
            throws IOException {
        Map<String, Integer> wordCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCount.merge(word, 1, Integer::sum);
                    }
                }
            }
        }

        return wordCount;
    }

    // ==================== Search Operations ====================

    /**
     * Finds lines matching a pattern.
     *
     * @param path the file path
     * @param pattern the pattern to search for
     * @return list of matching lines with line numbers
     * @throws IOException if read fails
     */
    public static List<Map.Entry<Integer, String>> searchLines(
            String path, String pattern) throws IOException {

        List<Map.Entry<Integer, String>> matches = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.contains(pattern)) {
                    matches.add(Map.entry(lineNumber, line));
                }
                lineNumber++;
            }
        }

        return matches;
    }

    // ==================== Main Method ====================

    /**
     * Demonstrates character stream operations.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Character Streams Demo ===");

        String tempDir = System.getProperty("java.io.tmpdir");
        String testFile = tempDir + File.separator + "char-test.txt";
        String copyFile = tempDir + File.separator + "char-copy.txt";
        String encodedFile = tempDir + File.separator + "encoded.txt";

        try {
            // Basic write/read
            writeText(testFile, "Hello, Character Streams!\n"
                + "Line 2: Testing text operations.\n"
                + "Line 3: Special chars: \u00e9, \u00f1, \u00fc, \u4e2d\u6587");
            System.out.println("File content:");
            System.out.println(readText(testFile));

            // Lines operations
            List<String> lines = List.of("First line", "Second line",
                "Third line");
            writeLines(copyFile, lines);
            System.out.println("\nWritten lines:");
            readLines(copyFile)
                .forEach(line -> System.out.println("  " + line));

            // Append
            appendLine(copyFile, "Fourth line (appended)");
            System.out.println("\nAfter append:");
            readLines(copyFile)
                .forEach(line -> System.out.println("  " + line));

            // Statistics
            Map<String, Long> stats = calculateStats(testFile);
            System.out.println("\nFile statistics:");
            stats.forEach((k, v) ->
                System.out.printf("  %-12s %d%n", k, v));

            // Word count
            System.out.println("\nWord frequencies:");
            countWords(testFile).entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                    .reversed())
                .limit(5)
                .forEach(e ->
                    System.out.printf("  %-15s %d%n", e.getKey(),
                        e.getValue()));

            // Search
            System.out.println("\nLines containing 'Stream':");
            searchLines(testFile, "Stream")
                .forEach(e ->
                    System.out.printf("  Line %d: %s%n",
                        e.getKey(), e.getValue()));

            // Encoding operations
            writeWithEncoding(encodedFile,
                "UTF-8 encoded text: \u00e9, \u00f1, \u00fc, \u4e2d\u6587",
                StandardCharsets.UTF_8);
            System.out.println("\nUTF-8 content:");
            System.out.println(readWithEncoding(encodedFile,
                StandardCharsets.UTF_8));

            // Cleanup
            new File(testFile).delete();
            new File(copyFile).delete();
            new File(encodedFile).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
