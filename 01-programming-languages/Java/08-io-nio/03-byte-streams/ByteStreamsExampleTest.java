import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("Byte Streams Tests")
class ByteStreamsExampleTest {

    @TempDir
    Path tempDir;

    private String tempPath(String name) {
        return tempDir.resolve(name).toString();
    }

    @Nested
    @DisplayName("Basic Byte Operations Tests")
    class BasicByteOperationsTests {

        @Test
        @DisplayName("Should write and read bytes")
        void shouldWriteAndReadBytes() throws IOException {
            String path = tempPath("rw.bin");
            byte[] data = "Hello Byte Streams".getBytes();
            ByteStreamsExample.writeBytes(path, data);
            byte[] read = ByteStreamsExample.readBytes(path);
            assertArrayEquals(data, read);
        }

        @Test
        @DisplayName("Should handle empty byte array")
        void shouldHandleEmptyByteArray() throws IOException {
            String path = tempPath("empty.bin");
            byte[] data = new byte[0];
            ByteStreamsExample.writeBytes(path, data);
            byte[] read = ByteStreamsExample.readBytes(path);
            assertEquals(0, read.length);
        }

        @Test
        @DisplayName("Should handle binary data")
        void shouldHandleBinaryData() throws IOException {
            String path = tempPath("binary.bin");
            byte[] data = new byte[256];
            for (int i = 0; i < 256; i++) {
                data[i] = (byte) i;
            }
            ByteStreamsExample.writeBytes(path, data);
            byte[] read = ByteStreamsExample.readBytes(path);
            assertArrayEquals(data, read);
        }

        @Test
        @DisplayName("Should copy file with byte count")
        void shouldCopyFileAndReturnByteCount() throws IOException {
            String src = tempPath("copy-src.bin");
            String dst = tempPath("copy-dst.bin");
            byte[] data = "Copy This Content".getBytes();
            ByteStreamsExample.writeBytes(src, data);
            long copied = ByteStreamsExample.copyFile(src, dst);
            assertEquals(data.length, copied);
            byte[] read = ByteStreamsExample.readBytes(dst);
            assertArrayEquals(data, read);
        }
    }

    @Nested
    @DisplayName("Data Stream Operations Tests")
    class DataStreamTests {

        @Test
        @DisplayName("Should write and read primitives")
        void shouldWriteAndReadPrimitives() throws IOException {
            String path = tempPath("primitives.bin");
            ByteStreamsExample.writePrimitives(path, 42, 123456789L, 3.14159, "Hello");
            Object[] values = ByteStreamsExample.readPrimitives(path);
            assertEquals(42, values[0]);
            assertEquals(123456789L, values[1]);
            assertEquals(3.14159, (Double) values[2], 0.00001);
            assertEquals("Hello", values[3]);
        }

        @Test
        @DisplayName("Should handle negative values")
        void shouldHandleNegativeValues() throws IOException {
            String path = tempPath("negative.bin");
            ByteStreamsExample.writePrimitives(path, -1, -999L, -2.5, "");
            Object[] values = ByteStreamsExample.readPrimitives(path);
            assertEquals(-1, values[0]);
            assertEquals(-999L, values[1]);
            assertEquals(-2.5, (Double) values[2], 0.001);
            assertEquals("", values[3]);
        }
    }

    @Nested
    @DisplayName("Compression Tests")
    class CompressionTests {

        @Test
        @DisplayName("Should gzip compress and decompress")
        void shouldGzipCompressAndDecompress() throws IOException {
            String src = tempPath("compress.bin");
            String gz = tempPath("compress.gz");
            String dec = tempPath("decompressed.bin");

            byte[] original = "Repeating data. ".repeat(100).getBytes();
            ByteStreamsExample.writeBytes(src, original);

            double ratio = ByteStreamsExample.gzipCompress(src, gz);
            assertTrue(ratio > 0, "Compression ratio should be positive");

            ByteStreamsExample.gzipDecompress(gz, dec);
            byte[] decompressed = ByteStreamsExample.readBytes(dec);
            assertArrayEquals(original, decompressed);
        }
    }

    @Nested
    @DisplayName("Utility Methods Tests")
    class UtilityTests {

        @Test
        @DisplayName("Should calculate checksum")
        void shouldCalculateChecksum() throws IOException {
            String path = tempPath("checksum.bin");
            byte[] data = {1, 2, 3, 4, 5};
            ByteStreamsExample.writeBytes(path, data);
            int checksum = ByteStreamsExample.calculateChecksum(path);
            int expected = 1 ^ 2 ^ 3 ^ 4 ^ 5;
            assertEquals(expected, checksum);
        }

        @Test
        @DisplayName("Should find byte pattern")
        void shouldFindBytePattern() throws IOException {
            String path = tempPath("pattern.bin");
            byte[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            ByteStreamsExample.writeBytes(path, data);
            byte[] pattern = {3, 4, 5};
            long pos = ByteStreamsExample.findPattern(path, pattern);
            assertEquals(3, pos);
        }

        @Test
        @DisplayName("Should return -1 when pattern not found")
        void shouldReturnMinusOneWhenPatternNotFound() throws IOException {
            String path = tempPath("no-pattern.bin");
            byte[] data = {1, 2, 3};
            ByteStreamsExample.writeBytes(path, data);
            byte[] pattern = {9, 9, 9};
            long pos = ByteStreamsExample.findPattern(path, pattern);
            assertEquals(-1, pos);
        }
    }
}
