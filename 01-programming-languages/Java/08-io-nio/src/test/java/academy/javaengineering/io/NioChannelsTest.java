package academy.javaengineering.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("NIO Channel Operations Tests")
class NioChannelsTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("NIO Channel Copy Tests")
    class ChannelCopyTests {

        @Test
        @DisplayName("Should copy file with NIO channel")
        void shouldCopyWithNioChannel() throws IOException {
            Path src = tempDir.resolve("channel-src.txt");
            Path dst = tempDir.resolve("channel-dst.txt");
            Files.writeString(src, "NIO Channel Copy");
            IoIntroduction.copyWithNioChannel(src, dst);
            assertTrue(Files.exists(dst));
            assertEquals("NIO Channel Copy", Files.readString(dst));
        }

        @Test
        @DisplayName("Should copy large file with NIO channel")
        void shouldCopyLargeFileWithNioChannel() throws IOException {
            Path src = tempDir.resolve("large-src.bin");
            Path dst = tempDir.resolve("large-dst.bin");
            byte[] data = new byte[100_000];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (i % 256);
            }
            Files.write(src, data);
            IoIntroduction.copyWithNioChannel(src, dst);
            assertEquals(data.length, Files.size(dst));
        }

        @Test
        @DisplayName("Should overwrite destination with NIO channel")
        void shouldOverwriteWithNioChannel() throws IOException {
            Path src = tempDir.resolve("ow-src.txt");
            Path dst = tempDir.resolve("ow-dst.txt");
            Files.writeString(src, "New Content");
            Files.writeString(dst, "Old Content");
            IoIntroduction.copyWithNioChannel(src, dst);
            assertEquals("New Content", Files.readString(dst));
        }
    }

    @Nested
    @DisplayName("Byte Stream Copy Tests")
    class ByteStreamCopyTests {

        @Test
        @DisplayName("Should copy file with byte streams")
        void shouldCopyWithByteStreams() throws IOException {
            Path src = tempDir.resolve("byte-src.txt");
            Path dst = tempDir.resolve("byte-dst.txt");
            Files.writeString(src, "Byte Stream Copy");
            IoIntroduction.copyWithByteStreams(src, dst);
            assertEquals("Byte Stream Copy", Files.readString(dst));
        }

        @Test
        @DisplayName("Should copy empty file with byte streams")
        void shouldCopyEmptyFileWithByteStreams() throws IOException {
            Path src = tempDir.resolve("empty-byte-src.txt");
            Path dst = tempDir.resolve("empty-byte-dst.txt");
            Files.writeString(src, "");
            IoIntroduction.copyWithByteStreams(src, dst);
            assertEquals(0, Files.size(dst));
        }
    }

    @Nested
    @DisplayName("Buffered Stream Copy Tests")
    class BufferedStreamCopyTests {

        @Test
        @DisplayName("Should copy with buffered streams via IoIntroduction")
        void shouldCopyWithBufferedStreams() throws IOException {
            Path src = tempDir.resolve("buf-src.txt");
            Path dst = tempDir.resolve("buf-dst.txt");
            Files.writeString(src, "Buffered Copy");
            IoIntroduction.copyWithBufferedStreams(src, dst);
            assertEquals("Buffered Copy", Files.readString(dst));
        }
    }

    @Nested
    @DisplayName("Character Stream Read Tests")
    class CharacterStreamReadTests {

        @Test
        @DisplayName("Should read with character streams")
        void shouldReadWithCharacterStreams() throws IOException {
            Path src = tempDir.resolve("char-read.txt");
            Files.writeString(src, "Hello NIO\nSecond Line");
            String content = IoIntroduction.readWithCharacterStreams(src);
            assertTrue(content.contains("Hello NIO"));
            assertTrue(content.contains("Second Line"));
        }
    }

    @Nested
    @DisplayName("File Statistics Tests")
    class FileStatsTests {

        @Test
        @DisplayName("Should calculate file statistics")
        void shouldCalculateFileStats() throws IOException {
            Path src = tempDir.resolve("stats.txt");
            Files.writeString(src, "One Two\nThree Four Five");
            long[] stats = IoIntroduction.calculateFileStats(src);
            assertEquals(2, stats[0]);   // lines
            assertEquals(5, stats[1]);   // words
            assertEquals(22, stats[2]);  // characters (excl newlines)
            assertTrue(stats[3] > 0);    // bytes
        }

        @Test
        @DisplayName("Should count words")
        void shouldCountWords() throws IOException {
            Path src = tempDir.resolve("words.txt");
            Files.writeString(src, "Java Java Java Python Python");
            Map<String, Integer> counts = IoIntroduction.countWords(src);
            assertEquals(3, counts.get("java"));
            assertEquals(2, counts.get("python"));
        }
    }

    @Nested
    @DisplayName("Decorator Pattern Tests")
    class DecoratorPatternTests {

        @Test
        @DisplayName("Should add line numbers with decorator pattern")
        void shouldAddLineNumbers() throws IOException {
            Path src = tempDir.resolve("decorator-src.txt");
            Path dst = tempDir.resolve("decorator-dst.txt");
            Files.writeString(src, "Line A\nLine B\nLine C");
            IoIntroduction.demonstrateDecoratorPattern(src, dst);
            String result = Files.readString(dst);
            assertTrue(result.contains("1: Line A"));
            assertTrue(result.contains("2: Line B"));
            assertTrue(result.contains("3: Line C"));
        }

        @Test
        @DisplayName("Should handle single line with decorator")
        void shouldHandleSingleLine() throws IOException {
            Path src = tempDir.resolve("single-src.txt");
            Path dst = tempDir.resolve("single-dst.txt");
            Files.writeString(src, "Only");
            IoIntroduction.demonstrateDecoratorPattern(src, dst);
            String result = Files.readString(dst);
            assertTrue(result.contains("1: Only"));
        }
    }
}
