package academy.javaengineering.io;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Introduction to Java IO/NIO - Demonstrates BIO vs NIO concepts.
 *
 * <p>This class provides foundational examples of Java IO operations,
 * including byte streams, character streams, and basic NIO usage.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class IoIntroduction {

    private IoIntroduction() {
        // Utility class
    }

    /**
     * Demonstrates basic byte stream operations.
     *
     * @param source the source file path
     * @param destination the destination file path
     * @throws IOException if an I/O error occurs
     */
    public static void copyWithByteStreams(Path source, Path destination)
            throws IOException {
        try (InputStream is = Files.newInputStream(source);
             OutputStream os = Files.newOutputStream(destination)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Demonstrates character stream operations.
     *
     * @param filePath the file to read
     * @return the file contents as a string
     * @throws IOException if an I/O error occurs
     */
    public static String readWithCharacterStreams(Path filePath)
            throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    /**
     * Demonstrates buffered stream operations for better performance.
     *
     * @param source the source file
     * @param destination the destination file
     * @throws IOException if an I/O error occurs
     */
    public static void copyWithBufferedStreams(Path source, Path destination)
            throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(
                Files.newInputStream(source));
             BufferedOutputStream bos = new BufferedOutputStream(
                Files.newOutputStream(destination))) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Demonstrates NIO FileChannel-based file copy.
     *
     * @param source the source file
     * @param destination the destination file
     * @throws IOException if an I/O error occurs
     */
    public static void copyWithNioChannel(Path source, Path destination)
            throws IOException {
        try (FileChannel sourceChannel = FileChannel.open(source,
                StandardOpenOption.READ);
             FileChannel destChannel = FileChannel.open(destination,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
        }
    }

    /**
     * Counts words in a file using character streams.
     *
     * @param filePath the file to analyze
     * @return map of word to count
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, Integer> countWords(Path filePath)
            throws IOException {
        Map<String, Integer> wordCount = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
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

    /**
     * Calculates file statistics using IO streams.
     *
     * @param filePath the file to analyze
     * @return array containing [lines, words, characters, bytes]
     * @throws IOException if an I/O error occurs
     */
    public static long[] calculateFileStats(Path filePath)
            throws IOException {
        long lines = 0;
        long words = 0;
        long characters = 0;
        long bytes = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                words += line.split("\\s+").length;
                characters += line.length();
            }
        }
        bytes = Files.size(filePath);

        return new long[]{lines, words, characters, bytes};
    }

    /**
     * Demonstrates decorator pattern with streams.
     *
     * @param inputFile the input file
     * @param outputFile the output file
     * @throws IOException if an I/O error occurs
     */
    public static void demonstrateDecoratorPattern(Path inputFile,
            Path outputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new BufferedInputStream(
                        Files.newInputStream(inputFile)))));
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new BufferedOutputStream(
                        Files.newOutputStream(outputFile))))) {

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                writer.write(String.format("%4d: %s", lineNumber, line));
                writer.newLine();
                lineNumber++;
            }
        }
    }

    /**
     * Main method demonstrating IO/NIO concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Java IO/NIO Introduction ===");
        System.out.println();

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "io-intro-demo");

        try {
            Files.createDirectories(tempDir);

            // Create sample file
            Path inputFile = tempDir.resolve("sample.txt");
            Files.writeString(inputFile,
                "Hello, Java IO/NIO!\n"
                + "This is a demonstration of Java input/output.\n"
                + "Java provides both blocking and non-blocking IO.");

            // Demonstrate byte stream copy
            Path byteCopy = tempDir.resolve("byte-copy.txt");
            long start = System.nanoTime();
            copyWithByteStreams(inputFile, byteCopy);
            long byteStreamTime = System.nanoTime() - start;

            // Demonstrate buffered stream copy
            Path bufferedCopy = tempDir.resolve("buffered-copy.txt");
            start = System.nanoTime();
            copyWithBufferedStreams(inputFile, bufferedCopy);
            long bufferedTime = System.nanoTime() - start;

            // Demonstrate NIO channel copy
            Path nioCopy = tempDir.resolve("nio-copy.txt");
            start = System.nanoTime();
            copyWithNioChannel(inputFile, nioCopy);
            long nioTime = System.nanoTime() - start;

            // Read and display content
            System.out.println("File Content:");
            System.out.println(readWithCharacterStreams(inputFile));

            // Word count
            System.out.println("Word Count:");
            countWords(inputFile).entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                    .reversed())
                .limit(5)
                .forEach(e ->
                    System.out.printf("  %-15s %d%n", e.getKey(),
                        e.getValue()));

            // File stats
            long[] stats = calculateFileStats(inputFile);
            System.out.printf("%nFile Statistics:%n");
            System.out.printf("  Lines:      %d%n", stats[0]);
            System.out.printf("  Words:      %d%n", stats[1]);
            System.out.printf("  Characters: %d%n", stats[2]);
            System.out.printf("  Bytes:      %d%n", stats[3]);

            // Performance comparison
            System.out.printf("%nPerformance Comparison (ns):%n");
            System.out.printf("  Byte Streams:   %,d%n", byteStreamTime);
            System.out.printf("  Buffered:       %,d%n", bufferedTime);
            System.out.printf("  NIO Channel:    %,d%n", nioTime);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            try {
                Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); }
                        catch (IOException ignored) { }
                    });
            } catch (IOException ignored) { }
        }
    }
}
