package academy.javaengineering.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("Buffered Streams Tests")
class BufferedStreamsTest {

    @TempDir
    Path tempDir;

    private String path(String name) {
        return tempDir.resolve(name).toString();
    }

    @Nested
    @DisplayName("Buffered Byte Copy Tests")
    class BufferedByteCopyTests {

        @Test
        @DisplayName("Should copy file with buffered streams")
        void shouldCopyWithBufferedStreams() throws IOException {
            String src = path("src.bin");
            String dst = path("dst.bin");
            byte[] data = "Buffered Copy Test".repeat(50).getBytes();
            Files.write(Path.of(src), data);
            long copied = BufferedStreams.copyWithBufferedStreams(src, dst);
            assertEquals(data.length, copied);
            byte[] read = Files.readAllBytes(Path.of(dst));
            assertEquals(data.length, read.length);
        }

        @Test
        @DisplayName("Should copy with custom buffer size")
        void shouldCopyWithCustomBuffer() throws IOException {
            String src = path("custom-src.bin");
            String dst = path("custom-dst.bin");
            byte[] data = "Custom Buffer Test".repeat(100).getBytes();
            Files.write(Path.of(src), data);
            long copied = BufferedStreams.copyWithCustomBuffer(src, dst, 512);
            assertEquals(data.length, copied);
            assertTrue(Files.exists(Path.of(dst)));
        }

        @Test
        @DisplayName("Should copy without buffer")
        void shouldCopyWithoutBuffer() throws IOException {
            String src = path("unbuffered-src.bin");
            String dst = path("unbuffered-dst.bin");
            byte[] data = "No Buffer Test".repeat(20).getBytes();
            Files.write(Path.of(src), data);
            long copied = BufferedStreams.copyWithoutBuffer(src, dst);
            assertEquals(data.length, copied);
        }

        @Test
        @DisplayName("Should copy empty file")
        void shouldCopyEmptyFile() throws IOException {
            String src = path("empty-src.bin");
            String dst = path("empty-dst.bin");
            Files.write(Path.of(src), new byte[0]);
            long copied = BufferedStreams.copyWithBufferedStreams(src, dst);
            assertEquals(0, copied);
        }
    }

    @Nested
    @DisplayName("Buffered Character Operations Tests")
    class BufferedCharacterTests {

        @Test
        @DisplayName("Should read lines with BufferedReader")
        void shouldReadLines() throws IOException {
            String p = path("readlines.txt");
            Files.writeString(Path.of(p), "Alpha\nBeta\nGamma");
            List<String> lines = BufferedStreams.readLines(p);
            assertEquals(3, lines.size());
            assertEquals("Alpha", lines.get(0));
            assertEquals("Beta", lines.get(1));
            assertEquals("Gamma", lines.get(2));
        }

        @Test
        @DisplayName("Should write lines with BufferedWriter")
        void shouldWriteLines() throws IOException {
            String p = path("writelines.txt");
            List<String> lines = List.of("One", "Two", "Three");
            BufferedStreams.writeLines(p, lines);
            List<String> read = BufferedStreams.readLines(p);
            assertEquals(lines, read);
        }

        @Test
        @DisplayName("Should transform lines")
        void shouldTransformLines() throws IOException {
            String src = path("transform-src.txt");
            String dst = path("transform-dst.txt");
            Files.writeString(Path.of(src), "hello\nworld");
            int count = BufferedStreams.transformLines(src, dst,
                String::toUpperCase);
            assertEquals(2, count);
            List<String> result = BufferedStreams.readLines(dst);
            assertEquals("HELLO", result.get(0));
            assertEquals("WORLD", result.get(1));
        }

        @Test
        @DisplayName("Should transform with empty file")
        void shouldTransformEmptyFile() throws IOException {
            String src = path("empty-src.txt");
            String dst = path("empty-dst.txt");
            Files.writeString(Path.of(src), "");
            int count = BufferedStreams.transformLines(src, dst,
                String::toUpperCase);
            assertEquals(0, count);
        }

        @Test
        @DisplayName("Should transform lines preserving order")
        void shouldTransformPreservingOrder() throws IOException {
            String src = path("order-src.txt");
            String dst = path("order-dst.txt");
            Files.writeString(Path.of(src), "c\nb\na");
            BufferedStreams.transformLines(src, dst, s -> "[" + s + "]");
            List<String> result = BufferedStreams.readLines(dst);
            assertEquals("[c]", result.get(0));
            assertEquals("[b]", result.get(1));
            assertEquals("[a]", result.get(2));
        }
    }

    @Nested
    @DisplayName("Performance Benchmark Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should benchmark buffer sizes")
        void shouldBenchmarkBufferSizes() throws IOException {
            String p = path("benchmark.txt");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                sb.append("Line ").append(i).append(": benchmark data\n");
            }
            Files.writeString(Path.of(p), sb.toString());

            Map<Integer, Long> results = BufferedStreams.benchmarkBufferSizes(
                p, new int[]{1024, 8192});

            assertEquals(2, results.size());
            assertTrue(results.containsKey(1024));
            assertTrue(results.containsKey(8192));
            results.values().forEach(time -> assertTrue(time >= 0));
        }
    }
}
