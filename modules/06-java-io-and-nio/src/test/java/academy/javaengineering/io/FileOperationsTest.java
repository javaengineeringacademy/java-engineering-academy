package academy.javaengineering.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("File Operations Tests")
class FileOperationsTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("File Creation Tests")
    class FileCreationTests {

        @Test
        @DisplayName("Should create file with NIO")
        void shouldCreateFileWithNio() throws IOException {
            Path filePath = tempDir.resolve("test.txt");
            Path created = FileOperations.createFileWithNio(filePath);
            assertTrue(Files.exists(created));
            assertTrue(Files.isRegularFile(created));
        }

        @Test
        @DisplayName("Should create nested directories")
        void shouldCreateNestedDirectories() throws IOException {
            Path nested = tempDir.resolve("a/b/c");
            Path created = FileOperations.createDirectories(nested);
            assertTrue(Files.exists(created));
            assertTrue(Files.isDirectory(created));
        }

        @Test
        @DisplayName("Should throw when creating duplicate file")
        void shouldThrowWhenCreatingDuplicateFile() throws IOException {
            Path filePath = tempDir.resolve("dup.txt");
            Files.createFile(filePath);
            assertThrows(IOException.class,
                () -> FileOperations.createFileWithNio(filePath));
        }
    }

    @Nested
    @DisplayName("Read Operations Tests")
    class ReadOperationsTests {

        @Test
        @DisplayName("Should read file as string")
        void shouldReadAsString() throws IOException {
            Path filePath = tempDir.resolve("read.txt");
            Files.writeString(filePath, "Hello World");
            String content = FileOperations.readAsString(filePath);
            assertEquals("Hello World", content);
        }

        @Test
        @DisplayName("Should read file as lines")
        void shouldReadAsLines() throws IOException {
            Path filePath = tempDir.resolve("lines.txt");
            Files.writeString(filePath, "Line1\nLine2\nLine3");
            List<String> lines = FileOperations.readAsLines(filePath);
            assertEquals(3, lines.size());
            assertEquals("Line1", lines.get(0));
            assertEquals("Line2", lines.get(1));
            assertEquals("Line3", lines.get(2));
        }

        @Test
        @DisplayName("Should read file as bytes")
        void shouldReadAsBytes() throws IOException {
            Path filePath = tempDir.resolve("bytes.txt");
            byte[] expected = "Binary Data".getBytes();
            Files.write(filePath, expected);
            byte[] actual = FileOperations.readAsBytes(filePath);
            assertEquals(expected.length, actual.length);
        }

        @Test
        @DisplayName("Should stream file lines")
        void shouldStreamLines() throws IOException {
            Path filePath = tempDir.resolve("stream.txt");
            Files.writeString(filePath, "A\nB\nC");
            try (Stream<String> lines = FileOperations.streamLines(filePath)) {
                long count = lines.count();
                assertEquals(3, count);
            }
        }

        @Test
        @DisplayName("Should read empty file")
        void shouldReadEmptyFile() throws IOException {
            Path filePath = tempDir.resolve("empty.txt");
            Files.writeString(filePath, "");
            String content = FileOperations.readAsString(filePath);
            assertEquals("", content);
        }
    }

    @Nested
    @DisplayName("Write Operations Tests")
    class WriteOperationsTests {

        @Test
        @DisplayName("Should write string to file")
        void shouldWriteString() throws IOException {
            Path filePath = tempDir.resolve("write.txt");
            FileOperations.writeString(filePath, "Test Content");
            assertEquals("Test Content", Files.readString(filePath));
        }

        @Test
        @DisplayName("Should write lines to file")
        void shouldWriteLines() throws IOException {
            Path filePath = tempDir.resolve("wlines.txt");
            List<String> lines = List.of("First", "Second", "Third");
            FileOperations.writeLines(filePath, lines);
            List<String> read = Files.readAllLines(filePath);
            assertEquals(3, read.size());
        }

        @Test
        @DisplayName("Should append to file")
        void shouldAppendToFile() throws IOException {
            Path filePath = tempDir.resolve("append.txt");
            FileOperations.writeString(filePath, "Start");
            FileOperations.appendToFile(filePath, " End");
            assertEquals("Start End", Files.readString(filePath));
        }

        @Test
        @DisplayName("Should overwrite existing file")
        void shouldOverwriteExistingFile() throws IOException {
            Path filePath = tempDir.resolve("overwrite.txt");
            FileOperations.writeString(filePath, "Old");
            FileOperations.writeString(filePath, "New");
            assertEquals("New", Files.readString(filePath));
        }
    }

    @Nested
    @DisplayName("File Copy and Move Tests")
    class CopyMoveTests {

        @Test
        @DisplayName("Should copy file")
        void shouldCopyFile() throws IOException {
            Path source = tempDir.resolve("source.txt");
            Path dest = tempDir.resolve("dest.txt");
            Files.writeString(source, "Copy Me");
            FileOperations.copyFile(source, dest);
            assertTrue(Files.exists(dest));
            assertEquals("Copy Me", Files.readString(dest));
        }

        @Test
        @DisplayName("Should move file")
        void shouldMoveFile() throws IOException {
            Path source = tempDir.resolve("moveme.txt");
            Path dest = tempDir.resolve("moved.txt");
            Files.writeString(source, "Move Me");
            FileOperations.moveFile(source, dest);
            assertFalse(Files.exists(source));
            assertTrue(Files.exists(dest));
            assertEquals("Move Me", Files.readString(dest));
        }

        @Test
        @DisplayName("Should copy and overwrite existing")
        void shouldCopyOverwriteExisting() throws IOException {
            Path source = tempDir.resolve("src.txt");
            Path dest = tempDir.resolve("dst.txt");
            Files.writeString(source, "New");
            Files.writeString(dest, "Old");
            FileOperations.copyFile(source, dest);
            assertEquals("New", Files.readString(dest));
        }
    }

    @Nested
    @DisplayName("Delete Operations Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete existing file")
        void shouldDeleteFile() throws IOException {
            Path filePath = tempDir.resolve("delete.txt");
            Files.writeString(filePath, "Gone");
            assertTrue(FileOperations.deleteFile(filePath));
            assertFalse(Files.exists(filePath));
        }

        @Test
        @DisplayName("Should return false when deleting non-existent file")
        void shouldReturnFalseForNonExistent() throws IOException {
            Path filePath = tempDir.resolve("nope.txt");
            assertFalse(FileOperations.deleteFile(filePath));
        }
    }

    @Nested
    @DisplayName("File Attributes Tests")
    class AttributesTests {

        @Test
        @DisplayName("Should get file attributes")
        void shouldGetFileAttributes() throws IOException {
            Path filePath = tempDir.resolve("attrs.txt");
            Files.writeString(filePath, "Attributes Test");
            Map<String, Object> attrs = FileOperations.getFileAttributes(filePath);
            assertNotNull(attrs);
            assertTrue((Boolean) attrs.get("isRegularFile"));
            assertFalse((Boolean) attrs.get("isDirectory"));
            assertEquals(16L, attrs.get("size"));
        }
    }

    @Nested
    @DisplayName("Directory Operations Tests")
    class DirectoryTests {

        @Test
        @DisplayName("Should list directory contents")
        void shouldListDirectory() throws IOException {
            Files.writeString(tempDir.resolve("a.txt"), "a");
            Files.writeString(tempDir.resolve("b.txt"), "b");
            List<Path> files = FileOperations.listDirectory(tempDir);
            assertEquals(2, files.size());
        }

        @Test
        @DisplayName("Should list directory with glob")
        void shouldListDirectoryWithGlob() throws IOException {
            Files.writeString(tempDir.resolve("data.txt"), "x");
            Files.writeString(tempDir.resolve("data.csv"), "y");
            List<Path> txtFiles = FileOperations.listDirectory(tempDir, "*.txt");
            assertEquals(1, txtFiles.size());
            assertTrue(txtFiles.get(0).toString().endsWith(".txt"));
        }

        @Test
        @DisplayName("Should calculate directory size")
        void shouldCalculateDirectorySize() throws IOException {
            Path sub = Files.createDirectories(tempDir.resolve("sub"));
            Files.writeString(sub.resolve("f1.txt"), "12345");
            Files.writeString(sub.resolve("f2.txt"), "67890");
            long size = FileOperations.calculateDirectorySize(tempDir);
            assertEquals(10L, size);
        }
    }

    @Nested
    @DisplayName("File Search Tests")
    class SearchTests {

        @Test
        @DisplayName("Should find files by extension")
        void shouldFindByExtension() throws IOException {
            Files.writeString(tempDir.resolve("a.java"), "code");
            Files.writeString(tempDir.resolve("b.java"), "code");
            Files.writeString(tempDir.resolve("c.txt"), "text");
            List<Path> javaFiles = FileOperations
                .findByExtension(tempDir, "java")
                .toList();
            assertEquals(2, javaFiles.size());
        }

        @Test
        @DisplayName("Should find files by name pattern")
        void shouldFindByName() throws IOException {
            Files.writeString(tempDir.resolve("report-2024.csv"), "r");
            Files.writeString(tempDir.resolve("report-2025.csv"), "r");
            Files.writeString(tempDir.resolve("summary.csv"), "s");
            List<Path> matches = FileOperations
                .findByName(tempDir, "report")
                .toList();
            assertEquals(2, matches.size());
        }
    }
}
