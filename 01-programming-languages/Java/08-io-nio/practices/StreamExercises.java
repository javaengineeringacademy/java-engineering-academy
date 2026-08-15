package academy.javaengineering.exercises;

import java.io.*;
import java.util.*;

/**
 * Exercises: Byte and Character Streams
 *
 * Complete the TODO sections below.
 */
public class StreamExercises {

    // TODO 1: Read all bytes from an InputStream and return as byte array
    public byte[] readAllBytes(InputStream is) throws IOException {
        // TODO: implement using ByteArrayOutputStream
        return new byte[0];
    }

    // TODO 2: Copy contents from InputStream to OutputStream
    // Return number of bytes copied
    public long copyStream(InputStream is, OutputStream os) throws IOException {
        // TODO: implement
        return 0;
    }

    // TODO 3: Read a text file and return its contents as a String
    // Use BufferedReader
    public String readWithBufferedReader(String filePath) throws IOException {
        // TODO: implement
        return "";
    }

    // TODO 4: Write lines to a file using BufferedWriter
    public void writeLines(String filePath, List<String> lines) throws IOException {
        // TODO: implement
    }

    // TODO 5: Implement a method that reads a CSV file
    // Each line is comma-separated, return list of string arrays
    public List<String[]> readCsv(String filePath) throws IOException {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 6: Implement a counting OutputStream that counts bytes written
    public static class CountingOutputStream extends OutputStream {
        private final OutputStream wrapped;
        private long count = 0;

        public CountingOutputStream(OutputStream wrapped) {
            this.wrapped = wrapped;
        }

        public long getCount() {
            return count;
        }

        @Override
        public void write(int b) throws IOException {
            // TODO: implement - write byte and increment count
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        StreamExercises exercises = new StreamExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== StreamExercises Tests ===\n");

        // Test 1
        total++;
        try {
            byte[] data = "Hello World".getBytes();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            byte[] result = exercises.readAllBytes(bis);
            if (Arrays.equals(data, result)) {
                System.out.println("Test 1 PASSED: readAllBytes");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: readAllBytes");
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            byte[] data = "Copy this".getBytes();
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            long copied = exercises.copyStream(is, os);
            if (copied == data.length && Arrays.equals(data, os.toByteArray())) {
                System.out.println("Test 2 PASSED: copyStream");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: copyStream");
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_reader_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "BufferedReader test\nLine 2");
            String content = exercises.readWithBufferedReader(tempFile);
            if (content.contains("BufferedReader test") && content.contains("Line 2")) {
                System.out.println("Test 3 PASSED: readWithBufferedReader");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: readWithBufferedReader");
            }
            java.nio.file.Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_writer_" + System.nanoTime() + ".txt";
            exercises.writeLines(tempFile, List.of("line1", "line2", "line3"));
            String content = Files.readString(Path.of(tempFile));
            if (content.contains("line1") && content.contains("line2") && content.contains("line3")) {
                System.out.println("Test 4 PASSED: writeLines");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: writeLines");
            }
            java.nio.file.Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_csv_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "name,age,city\nAlice,30,NYC\nBob,25,LA");
            List<String[]> csv = exercises.readCsv(tempFile);
            if (csv.size() == 3
                && "name".equals(csv.get(0)[0])
                && "Alice".equals(csv.get(1)[0])
                && "30".equals(csv.get(1)[1])) {
                System.out.println("Test 5 PASSED: readCsv");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: readCsv - " + csv.size() + " rows");
            }
            java.nio.file.Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            StreamExercises.CountingOutputStream cos = new StreamExercises.CountingOutputStream(os);
            cos.write("test".getBytes());
            cos.flush();
            if (cos.getCount() == 4 && "test".equals(os.toString())) {
                System.out.println("Test 6 PASSED: CountingOutputStream");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: CountingOutputStream - count=" + cos.getCount());
            }
        } catch (Exception e) {
            System.out.println("Test 6 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
