import java.io.*;
import java.util.*;

/**
 * Buffered Streams in Java IO - Demonstrates performance optimization.
 *
 * <p>This class provides comprehensive examples of buffered stream operations
 * including BufferedInputStream, BufferedOutputStream, BufferedReader,
 * and BufferedWriter with performance comparisons.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class BufferedStreamsExample {

    private BufferedStreamsExample() {
        // Utility class
    }

    // ==================== Basic Operations ====================

    /**
     * Copies a file using buffered byte streams.
     *
     * @param source source file path
     * @param destination destination file path
     * @return number of bytes copied
     * @throws IOException if copy fails
     */
    public static long copyWithBufferedStreams(String source,
            String destination) throws IOException {
        long totalBytes = 0;

        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(source));
             BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destination))) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
        }

        return totalBytes;
    }

    /**
     * Copies a file with custom buffer size.
     *
     * @param source source file path
     * @param destination destination file path
     * @param bufferSize the buffer size to use
     * @return number of bytes copied
     * @throws IOException if copy fails
     */
    public static long copyWithCustomBuffer(String source,
            String destination, int bufferSize) throws IOException {
        long totalBytes = 0;

        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(source), bufferSize);
             BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destination), bufferSize)) {

            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
        }

        return totalBytes;
    }

    /**
     * Copies a file without buffering for comparison.
     *
     * @param source source file path
     * @param destination destination file path
     * @return number of bytes copied
     * @throws IOException if copy fails
     */
    public static long copyWithoutBuffer(String source,
            String destination) throws IOException {
        long totalBytes = 0;

        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024]; // Small buffer
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
        }

        return totalBytes;
    }

    // ==================== Character Operations ====================

    /**
     * Reads a file line by line using BufferedReader.
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
     * Writes lines to a file using BufferedWriter.
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
     * Copies text file with line transformation.
     *
     * @param source source file path
     * @param destination destination file path
     * @param transformer line transformation function
     * @return number of lines processed
     * @throws IOException if operation fails
     */
    public static int transformLines(String source, String destination,
            java.util.function.UnaryOperator<String> transformer)
            throws IOException {
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(source));
             BufferedWriter writer = new BufferedWriter(
                new FileWriter(destination))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(transformer.apply(line));
                writer.newLine();
                lineCount++;
            }
        }

        return lineCount;
    }

    // ==================== Performance ====================

    /**
     * Benchmarks different buffer sizes.
     *
     * @param filePath the file to benchmark
     * @param bufferSizes array of buffer sizes to test
     * @return map of buffer size to time in milliseconds
     * @throws IOException if operation fails
     */
    public static Map<Integer, Long> benchmarkBufferSizes(String filePath,
            int[] bufferSizes) throws IOException {

        Map<Integer, Long> results = new LinkedHashMap<>();
        long fileSize = new File(filePath).length();

        for (int bufferSize : bufferSizes) {
            String tempFile = filePath + ".bench." + bufferSize;
            long startTime = System.nanoTime();

            if (bufferSize == 0) {
                copyWithoutBuffer(filePath, tempFile);
            } else {
                copyWithCustomBuffer(filePath, tempFile, bufferSize);
            }

            long elapsed = (System.nanoTime() - startTime) / 1_000_000;
            results.put(bufferSize, elapsed);

            new File(tempFile).delete();
        }

        return results;
    }

    // ==================== Main Method ====================

    /**
     * Demonstrates buffered stream operations.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Buffered Streams Demo ===");

        String tempDir = System.getProperty("java.io.tmpdir");
        String testFile = tempDir + File.separator + "buffer-test.txt";
        String copyFile = tempDir + File.separator + "buffer-copy.txt";
        String lineFile = tempDir + File.separator + "lines.txt";
        String transformedFile = tempDir + File.separator + "transformed.txt";

        try {
            // Create test file
            List<String> testData = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                testData.add("Line " + i + ": " +
                    "Test data for buffered streams. ".repeat(5));
            }
            writeLines(testFile, testData);
            System.out.println("Created test file with " +
                testData.size() + " lines");

            // Read lines
            long start = System.nanoTime();
            List<String> lines = readLines(testFile);
            long readTime = System.nanoTime() - start;
            System.out.printf("Read %d lines in %,d ms%n",
                lines.size(), readTime / 1_000_000);

            // Transform lines
            start = System.nanoTime();
            int transformed = transformLines(testFile, transformedFile,
                line -> line.toUpperCase());
            long transformTime = System.nanoTime() - start;
            System.out.printf("Transformed %d lines in %,d ms%n",
                transformed, transformTime / 1_000_000);

            // Write lines
            start = System.nanoTime();
            writeLines(copyFile, lines);
            long writeTime = System.nanoTime() - start;
            System.out.printf("Wrote %d lines in %,d ms%n",
                lines.size(), writeTime / 1_000_000);

            // Performance benchmark
            System.out.println("\nBuffer Size Benchmark:");
            System.out.printf("%-12s %-15s %-15s%n",
                "Buffer", "Time (ms)", "Throughput");

            Map<Integer, Long> benchmark = benchmarkBufferSizes(
                testFile, new int[]{0, 1024, 8192, 65536, 262144});

            long fileSize = new File(testFile).length();
            benchmark.forEach((bufferSize, time) -> {
                double throughput = (fileSize / 1024.0 / 1024.0) /
                    (time / 1000.0);
                String label = bufferSize == 0 ? "None" :
                    bufferSize + " bytes";
                System.out.printf("%-12s %-15d %-15.2f MB/s%n",
                    label, time, throughput);
            });

            // Cleanup
            new File(testFile).delete();
            new File(copyFile).delete();
            new File(lineFile).delete();
            new File(transformedFile).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
