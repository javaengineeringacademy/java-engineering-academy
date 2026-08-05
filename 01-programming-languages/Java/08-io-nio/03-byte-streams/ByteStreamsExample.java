import java.io.*;
import java.util.zip.*;

/**
 * Byte Streams in Java IO - Demonstrates binary data operations.
 *
 * <p>This class provides comprehensive examples of byte stream operations
 * including FileInputStream, FileOutputStream, BufferedInputStream,
 * BufferedOutputStream, and DataInputStream/DataOutputStream.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class ByteStreamsExample {

    private static final int BUFFER_SIZE = 8192;

    private ByteStreamsExample() {
        // Utility class
    }

    // ==================== Basic Operations ====================

    /**
     * Writes bytes to a file.
     *
     * @param path the file path
     * @param data the byte array to write
     * @throws IOException if write fails
     */
    public static void writeBytes(String path, byte[] data)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(data);
        }
    }

    /**
     * Reads all bytes from a file.
     *
     * @param path the file path
     * @return byte array content
     * @throws IOException if read fails
     */
    public static byte[] readBytes(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    /**
     * Copies a file using buffered byte streams.
     *
     * @param source source file path
     * @param destination destination file path
     * @return number of bytes copied
     * @throws IOException if copy fails
     */
    public static long copyFile(String source, String destination)
            throws IOException {
        long totalBytes = 0;
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(source), BUFFER_SIZE);
             BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destination), BUFFER_SIZE)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
        }
        return totalBytes;
    }

    // ==================== Data Stream Operations ====================

    /**
     * Writes primitive data types to a file.
     *
     * @param path the file path
     * @param intValue integer value
     * @param longValue long value
     * @param doubleValue double value
     * @param stringValue string value
     * @throws IOException if write fails
     */
    public static void writePrimitives(String path, int intValue,
            long longValue, double doubleValue, String stringValue)
            throws IOException {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream(path), BUFFER_SIZE))) {
            dos.writeInt(intValue);
            dos.writeLong(longValue);
            dos.writeDouble(doubleValue);
            dos.writeUTF(stringValue);
        }
    }

    /**
     * Reads primitive data types from a file.
     *
     * @param path the file path
     * @return array of read values [int, long, double, String]
     * @throws IOException if read fails
     */
    public static Object[] readPrimitives(String path) throws IOException {
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                    new FileInputStream(path), BUFFER_SIZE))) {
            int intValue = dis.readInt();
            long longValue = dis.readLong();
            double doubleValue = dis.readDouble();
            String stringValue = dis.readUTF();
            return new Object[]{intValue, longValue, doubleValue, stringValue};
        }
    }

    // ==================== Compression ====================

    /**
     * Compresses a file using GZIP.
     *
     * @param source source file path
     * @param destination compressed file path
     * @return compression ratio
     * @throws IOException if compression fails
     */
    public static double gzipCompress(String source, String destination)
            throws IOException {
        long originalSize = new File(source).length();

        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination);
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, bytesRead);
            }
        }

        long compressedSize = new File(destination).length();
        return (1.0 - (double) compressedSize / originalSize) * 100;
    }

    /**
     * Decompresses a GZIP file.
     *
     * @param source compressed file path
     * @param destination decompressed file path
     * @throws IOException if decompression fails
     */
    public static void gzipDecompress(String source, String destination)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             FileOutputStream fos = new FileOutputStream(destination)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gzis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    // ==================== Utility Methods ====================

    /**
     * Calculates XOR checksum of a file.
     *
     * @param path the file path
     * @return XOR checksum
     * @throws IOException if read fails
     */
    public static int calculateChecksum(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            int checksum = 0;
            int b;
            while ((b = fis.read()) != -1) {
                checksum ^= b;
            }
            return checksum;
        }
    }

    /**
     * Displays hex dump of file contents.
     *
     * @param path the file path
     * @param maxLines maximum lines to display
     * @throws IOException if read fails
     */
    public static void hexDump(String path, int maxLines)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] buffer = new byte[16];
            int bytesRead;
            int lineCount = 0;

            while ((bytesRead = fis.read(buffer)) != -1 &&
                    lineCount < maxLines) {
                // Offset
                System.out.printf("%08X: ", lineCount * 16);

                // Hex bytes
                for (int i = 0; i < 16; i++) {
                    if (i < bytesRead) {
                        System.out.printf("%02X ", buffer[i]);
                    } else {
                        System.out.print("   ");
                    }
                    if (i == 7) System.out.print(" ");
                }

                // ASCII representation
                System.out.print(" |");
                for (int i = 0; i < bytesRead; i++) {
                    char c = (char) (buffer[i] & 0xFF);
                    System.out.print(
                        (c >= 32 && c < 127) ? c : '.');
                }
                System.out.println("|");

                lineCount++;
            }
        }
    }

    /**
     * Searches for a byte pattern in a file.
     *
     * @param path the file path
     * @param pattern the byte pattern to find
     * @return position of first occurrence, or -1 if not found
     * @throws IOException if read fails
     */
    public static long findPattern(String path, byte[] pattern)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            long position = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                for (int i = 0; i <= bytesRead - pattern.length; i++) {
                    boolean found = true;
                    for (int j = 0; j < pattern.length; j++) {
                        if (buffer[i + j] != pattern[j]) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        return position + i;
                    }
                }
                position += bytesRead;
            }
            return -1;
        }
    }

    // ==================== Main Method ====================

    /**
     * Demonstrates byte stream operations.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Byte Streams Demo ===");

        String tempDir = System.getProperty("java.io.tmpdir");
        String testFile = tempDir + File.separator + "byte-test.bin";
        String copyFile = tempDir + File.separator + "byte-copy.bin";
        String dataFile = tempDir + File.separator + "data.bin";
        String gzFile = tempDir + File.separator + "test.gz";
        String decompressedFile = tempDir + File.separator + "decompressed.bin";

        try {
            // Basic byte operations
            byte[] testData = "Hello, Byte Streams!".getBytes();
            writeBytes(testFile, testData);
            byte[] readData = readBytes(testFile);
            System.out.println("Read: " + new String(readData));

            // File copy
            long copied = copyFile(testFile, copyFile);
            System.out.printf("Copied %,d bytes%n", copied);

            // Data streams
            writePrimitives(dataFile, 42, 123456789L, 3.14159, "Hello Data");
            Object[] values = readPrimitives(dataFile);
            System.out.printf("Read: int=%d, long=%d, double=%.5f, string=%s%n",
                values[0], values[1], values[2], values[3]);

            // Hex dump
            System.out.println("\nHex dump of test file:");
            hexDump(testFile, 4);

            // Checksum
            System.out.printf("%nChecksum: 0x%04X%n",
                calculateChecksum(testFile));

            // Compression
            double ratio = gzipCompress(testFile, gzFile);
            System.out.printf("%nCompression ratio: %.1f%%%n", ratio);

            // Decompression
            gzipDecompress(gzFile, decompressedFile);
            byte[] decompressed = readBytes(decompressedFile);
            System.out.println("Decompressed: " + new String(decompressed));

            // Cleanup
            new File(testFile).delete();
            new File(copyFile).delete();
            new File(dataFile).delete();
            new File(gzFile).delete();
            new File(decompressedFile).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
