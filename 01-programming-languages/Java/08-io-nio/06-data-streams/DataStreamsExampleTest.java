import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("Data Streams Tests")
class DataStreamsExampleTest {

    @TempDir
    Path tempDir;

    private String tempPath(String name) {
        return tempDir.resolve(name).toString();
    }

    @Nested
    @DisplayName("Primitive Data Stream Tests")
    class PrimitiveTests {

        @Test
        @DisplayName("Should write and read primitives")
        void shouldWriteAndReadPrimitives() throws IOException {
            String path = tempPath("primitives.bin");
            DataStreamsExample.writePrimitives(path);
            // Just verify no exception thrown
            DataStreamsExample.readPrimitives(path);
        }
    }

    @Nested
    @DisplayName("RandomAccessFile Tests")
    class RandomAccessTests {

        @Test
        @DisplayName("Should perform random access operations")
        void shouldPerformRandomAccess() throws IOException {
            String path = tempPath("random.dat");
            DataStreamsExample.randomAccessDemo(path);
            // Verify file was created
            assertTrue(Files.exists(Path.of(path)));
        }
    }

    @Nested
    @DisplayName("Student Record Tests")
    class StudentRecordTests {

        @Test
        @DisplayName("Should write and read student records")
        void shouldWriteAndReadStudentRecords() throws IOException {
            String path = tempPath("students.dat");
            try (var dos = new java.io.DataOutputStream(
                    new java.io.BufferedOutputStream(
                        new java.io.FileOutputStream(path)))) {
                DataStreamsExample.writeStudentRecord(dos, 1, "Alice", 3.8);
                DataStreamsExample.writeStudentRecord(dos, 2, "Bob", 2.5);
            }

            try (var dis = new java.io.DataInputStream(
                    new java.io.BufferedInputStream(
                        new java.io.FileInputStream(path)))) {
                Object[] record1 = DataStreamsExample.readStudentRecord(dis);
                assertEquals(1, record1[0]);
                assertEquals("Alice", record1[1]);
                assertEquals(3.8, (Double) record1[2], 0.001);
                assertEquals(true, record1[3]);

                Object[] record2 = DataStreamsExample.readStudentRecord(dis);
                assertEquals(2, record2[0]);
                assertEquals("Bob", record2[1]);
                assertEquals(2.5, (Double) record2[2], 0.001);
                assertEquals(false, record2[3]);
            }
        }
    }
}
