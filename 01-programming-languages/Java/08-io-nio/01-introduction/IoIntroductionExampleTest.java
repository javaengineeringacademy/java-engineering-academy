import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("IO Introduction Tests")
class IoIntroductionExampleTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("Byte Stream Copy Tests")
    class ByteStreamCopyTests {

        @Test
        @DisplayName("Should copy file with byte streams")
        void shouldCopyWithByteStreams() throws Exception {
            Path src = tempDir.resolve("src.txt");
            Path dst = tempDir.resolve("dst.txt");
            Files.writeString(src, "Hello Byte Streams");
            IoIntroductionExample.copyWithByteStreams(src, dst);
            assertTrue(Files.exists(dst));
            assertEquals("Hello Byte Streams", Files.readString(dst));
        }

        @Test
        @DisplayName("Should copy empty file with byte streams")
        void shouldCopyEmptyFile() throws Exception {
            Path src = tempDir.resolve("empty-src.txt");
            Path dst = tempDir.resolve("empty-dst.txt");
            Files.writeString(src, "");
            IoIntroductionExample.copyWithByteStreams(src, dst);
            assertEquals(0, Files.size(dst));
        }
    }

    @Nested
    @DisplayName("Character Stream Read Tests")
    class CharacterStreamReadTests {

        @Test
        @DisplayName("Should read with character streams")
        void shouldReadWithCharacterStreams() throws Exception {
            Path src = tempDir.resolve("char-read.txt");
            Files.writeString(src, "Hello NIO\nSecond Line");
            String content = IoIntroductionExample.readWithCharacterStreams(src);
            assertTrue(content.contains("Hello NIO"));
            assertTrue(content.contains("Second Line"));
        }
    }

    @Nested
    @DisplayName("Buffered Stream Copy Tests")
    class BufferedStreamCopyTests {

        @Test
        @DisplayName("Should copy with buffered streams")
        void shouldCopyWithBufferedStreams() throws Exception {
            Path src = tempDir.resolve("buf-src.txt");
            Path dst = tempDir.resolve("buf-dst.txt");
            Files.writeString(src, "Buffered Copy");
            IoIntroductionExample.copyWithBufferedStreams(src, dst);
            assertEquals("Buffered Copy", Files.readString(dst));
        }
    }

    @Nested
    @DisplayName("NIO Channel Copy Tests")
    class NioChannelCopyTests {

        @Test
        @DisplayName("Should copy with NIO channel")
        void shouldCopyWithNioChannel() throws Exception {
            Path src = tempDir.resolve("nio-src.txt");
            Path dst = tempDir.resolve("nio-dst.txt");
            Files.writeString(src, "NIO Channel Copy");
            IoIntroductionExample.copyWithNioChannel(src, dst);
            assertTrue(Files.exists(dst));
            assertEquals("NIO Channel Copy", Files.readString(dst));
        }
    }

    @Nested
    @DisplayName("File Statistics Tests")
    class FileStatsTests {

        @Test
        @DisplayName("Should calculate file statistics")
        void shouldCalculateFileStats() throws Exception {
            Path src = tempDir.resolve("stats.txt");
            Files.writeString(src, "One Two\nThree Four Five");
            long[] stats = IoIntroductionExample.calculateFileStats(src);
            assertEquals(2, stats[0]);   // lines
            assertEquals(5, stats[1]);   // words
            assertTrue(stats[3] > 0);    // bytes
        }

        @Test
        @DisplayName("Should count words")
        void shouldCountWords() throws Exception {
            Path src = tempDir.resolve("words.txt");
            Files.writeString(src, "Java Java Java Python Python");
            Map<String, Integer> counts = IoIntroductionExample.countWords(src);
            assertEquals(3, counts.get("java"));
            assertEquals(2, counts.get("python"));
        }
    }

    @Nested
    @DisplayName("Decorator Pattern Tests")
    class DecoratorPatternTests {

        @Test
        @DisplayName("Should add line numbers with decorator pattern")
        void shouldAddLineNumbers() throws Exception {
            Path src = tempDir.resolve("decorator-src.txt");
            Path dst = tempDir.resolve("decorator-dst.txt");
            Files.writeString(src, "Line A\nLine B\nLine C");
            IoIntroductionExample.demonstrateDecoratorPattern(src, dst);
            String result = Files.readString(dst);
            assertTrue(result.contains("1: Line A"));
            assertTrue(result.contains("2: Line B"));
            assertTrue(result.contains("3: Line C"));
        }
    }
}
