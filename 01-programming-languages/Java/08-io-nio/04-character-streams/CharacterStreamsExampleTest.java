import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("Character Streams Tests")
class CharacterStreamsExampleTest {

    @TempDir
    Path tempDir;

    private String path(String name) {
        return tempDir.resolve(name).toString();
    }

    @Nested
    @DisplayName("Basic Text Operations Tests")
    class BasicTextTests {

        @Test
        @DisplayName("Should write and read text")
        void shouldWriteAndReadText() throws IOException {
            String p = path("text.txt");
            CharacterStreamsExample.writeText(p, "Hello World");
            String content = CharacterStreamsExample.readText(p);
            assertTrue(content.contains("Hello World"));
        }

        @Test
        @DisplayName("Should handle multiline text")
        void shouldHandleMultilineText() throws IOException {
            String p = path("multi.txt");
            String text = "Line 1\nLine 2\nLine 3";
            CharacterStreamsExample.writeText(p, text);
            String content = CharacterStreamsExample.readText(p);
            assertTrue(content.contains("Line 1"));
            assertTrue(content.contains("Line 2"));
            assertTrue(content.contains("Line 3"));
        }

        @Test
        @DisplayName("Should handle empty file")
        void shouldHandleEmptyFile() throws IOException {
            String p = path("empty.txt");
            CharacterStreamsExample.writeText(p, "");
            String content = CharacterStreamsExample.readText(p);
            assertEquals("", content.trim());
        }
    }

    @Nested
    @DisplayName("Encoding Operations Tests")
    class EncodingTests {

        @Test
        @DisplayName("Should write and read with UTF-8 encoding")
        void shouldWriteAndReadUtf8() throws IOException {
            String p = path("utf8.txt");
            String text = "UTF-8: \u00e9\u00f1\u00fc\u4e2d\u6587";
            CharacterStreamsExample.writeWithEncoding(p, text, StandardCharsets.UTF_8);
            String content = CharacterStreamsExample.readWithEncoding(p, StandardCharsets.UTF_8);
            assertTrue(content.contains(text));
        }

        @Test
        @DisplayName("Should write and read with US-ASCII encoding")
        void shouldWriteAndReadAscii() throws IOException {
            String p = path("ascii.txt");
            String text = "ASCII Only Text";
            CharacterStreamsExample.writeWithEncoding(p, text, StandardCharsets.US_ASCII);
            String content = CharacterStreamsExample.readWithEncoding(p, StandardCharsets.US_ASCII);
            assertTrue(content.contains(text));
        }
    }

    @Nested
    @DisplayName("Line Operations Tests")
    class LineOperationsTests {

        @Test
        @DisplayName("Should read all lines")
        void shouldReadAllLines() throws IOException {
            String p = path("lines.txt");
            Files.writeString(Path.of(p), "A\nB\nC\nD");
            List<String> lines = CharacterStreamsExample.readLines(p);
            assertEquals(4, lines.size());
            assertEquals("A", lines.get(0));
            assertEquals("D", lines.get(3));
        }

        @Test
        @DisplayName("Should write and read lines")
        void shouldWriteAndReadLines() throws IOException {
            String p = path("wlines.txt");
            List<String> lines = List.of("Alpha", "Beta", "Gamma");
            CharacterStreamsExample.writeLines(p, lines);
            List<String> read = CharacterStreamsExample.readLines(p);
            assertEquals(3, read.size());
            assertEquals(lines, read);
        }

        @Test
        @DisplayName("Should append line to file")
        void shouldAppendLine() throws IOException {
            String p = path("append.txt");
            Files.writeString(Path.of(p), "First\n");
            CharacterStreamsExample.appendLine(p, "Second");
            List<String> lines = CharacterStreamsExample.readLines(p);
            assertEquals(2, lines.size());
            assertEquals("First", lines.get(0));
            assertEquals("Second", lines.get(1));
        }
    }

    @Nested
    @DisplayName("Statistics Tests")
    class StatisticsTests {

        @Test
        @DisplayName("Should calculate text statistics")
        void shouldCalculateStats() throws IOException {
            String p = path("stats.txt");
            Files.writeString(Path.of(p), "Hello World\nFoo Bar");
            Map<String, Long> stats = CharacterStreamsExample.calculateStats(p);
            assertEquals(2L, stats.get("lines"));
            assertEquals(4L, stats.get("words"));
            assertEquals(18L, stats.get("characters"));
        }

        @Test
        @DisplayName("Should count word frequencies")
        void shouldCountWords() throws IOException {
            String p = path("words.txt");
            Files.writeString(Path.of(p), "cat dog cat bird cat");
            Map<String, Integer> counts = CharacterStreamsExample.countWords(p);
            assertEquals(3, counts.get("cat"));
            assertEquals(1, counts.get("dog"));
            assertEquals(1, counts.get("bird"));
        }
    }

    @Nested
    @DisplayName("Search Operations Tests")
    class SearchTests {

        @Test
        @DisplayName("Should search lines by pattern")
        void shouldSearchLines() throws IOException {
            String p = path("search.txt");
            Files.writeString(Path.of(p),
                "Java is great\nPython is nice\nJava is powerful");
            List<Map.Entry<Integer, String>> matches =
                CharacterStreamsExample.searchLines(p, "Java");
            assertEquals(2, matches.size());
            assertEquals(1, matches.get(0).getKey());
            assertEquals(3, matches.get(1).getKey());
        }

        @Test
        @DisplayName("Should return empty list when no match")
        void shouldReturnEmptyOnNoMatch() throws IOException {
            String p = path("nomatch.txt");
            Files.writeString(Path.of(p), "Hello World");
            List<Map.Entry<Integer, String>> matches =
                CharacterStreamsExample.searchLines(p, "XYZ");
            assertTrue(matches.isEmpty());
        }
    }
}
