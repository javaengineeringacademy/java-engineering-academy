import static org.junit.jupiter.api.Assertions.*;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.charset.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("NIO Channels Tests")
class NioChannelsExampleExampleTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("FileChannel Tests")
    class FileChannelTests {

        @Test
        @DisplayName("Should write and read with FileChannel")
        void shouldWriteReadWithChannel() throws Exception {
            Path file = tempDir.resolve("channel.txt");

            // Write
            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {
                ByteBuffer buffer = ByteBuffer.wrap("Hello".getBytes());
                channel.write(buffer);
            }

            // Read
            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.READ)) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                channel.read(buffer);
                buffer.flip();
                assertEquals("Hello",
                    StandardCharsets.UTF_8.decode(buffer).toString());
            }
        }

        @Test
        @DisplayName("Should get file size and position")
        void shouldGetSizeAndPosition() throws Exception {
            Path file = tempDir.resolve("size.txt");
            Files.writeString(file, "Test content");

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.READ)) {
                assertEquals(12, channel.size());
                assertEquals(0, channel.position());
            }
        }

        @Test
        @DisplayName("Should truncate file")
        void shouldTruncate() throws Exception {
            Path file = tempDir.resolve("truncate.txt");
            Files.writeString(file, "Hello World");

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.WRITE)) {
                channel.truncate(5);
            }

            assertEquals("Hello", Files.readString(file));
        }
    }

    @Nested
    @DisplayName("Zero-Copy Transfer Tests")
    class ZeroCopyTests {

        @Test
        @DisplayName("Should transfer file with zero-copy")
        void shouldTransferZeroCopy() throws Exception {
            Path source = tempDir.resolve("source.txt");
            Path target = tempDir.resolve("target.txt");
            Files.writeString(source, "Zero copy content");

            try (FileChannel srcChannel = FileChannel.open(source,
                    StandardOpenOption.READ);
                 FileChannel tgtChannel = FileChannel.open(target,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {

                long transferred = srcChannel.transferTo(
                    0, srcChannel.size(), tgtChannel);
                assertEquals(17, transferred);
            }

            assertEquals("Zero copy content", Files.readString(target));
        }
    }

    @Nested
    @DisplayName("Scatter/Gather Tests")
    class ScatterGatherTests {

        @Test
        @DisplayName("Should scatter read into multiple buffers")
        void shouldScatterRead() throws Exception {
            Path file = tempDir.resolve("scatter.txt");
            Files.writeString(file, "HEADERBody content");

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.READ)) {

                ByteBuffer header = ByteBuffer.allocate(6);
                ByteBuffer body = ByteBuffer.allocate(100);
                ByteBuffer[] buffers = {header, body};

                channel.read(buffers);

                header.flip();
                body.flip();

                assertEquals("HEADER",
                    StandardCharsets.UTF_8.decode(header).toString());
                assertEquals("Body content",
                    StandardCharsets.UTF_8.decode(body).toString().trim());
            }
        }

        @Test
        @DisplayName("Should gather write from multiple buffers")
        void shouldGatherWrite() throws Exception {
            Path file = tempDir.resolve("gather.txt");

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {

                ByteBuffer header = ByteBuffer.wrap("H".getBytes());
                ByteBuffer body = ByteBuffer.wrap("B".getBytes());
                channel.write(new ByteBuffer[]{header, body});
            }

            assertEquals("HB", Files.readString(file));
        }
    }

    @Nested
    @DisplayName("Memory-Mapped Tests")
    class MemoryMappedTests {

        @Test
        @DisplayName("Should read memory-mapped file")
        void shouldReadMemoryMapped() throws Exception {
            Path file = tempDir.resolve("mapped.txt");
            Files.writeString(file, "Mapped content");

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.READ)) {

                MappedByteBuffer mapped = channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    0,
                    channel.size()
                );

                byte[] data = new byte[(int) channel.size()];
                mapped.get(data);
                assertEquals("Mapped content", new String(data));
            }
        }

        @Test
        @DisplayName("Should write memory-mapped file")
        void shouldWriteMemoryMapped() throws Exception {
            Path file = tempDir.resolve("mapped-write.txt");

            try (FileChannel channel = FileChannel.open(file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.READ,
                    StandardOpenOption.WRITE)) {

                MappedByteBuffer mapped = channel.map(
                    FileChannel.MapMode.READ_WRITE,
                    0,
                    100
                );

                mapped.put("Written via mmap".getBytes());
                mapped.force();
            }

            assertTrue(Files.readString(file).startsWith("Written via mmap"));
        }
    }

    @Nested
    @DisplayName("Channel Lifecycle Tests")
    class LifecycleTests {

        @Test
        @DisplayName("Should close channel properly")
        void shouldCloseChannel() throws Exception {
            Path file = tempDir.resolve("lifecycle.txt");
            FileChannel channel = FileChannel.open(file,
                StandardOpenOption.CREATE,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE);

            assertTrue(channel.isOpen());
            channel.close();
            assertFalse(channel.isOpen());
        }

        @Test
        @DisplayName("Should throw on closed channel")
        void shouldThrowOnClosedChannel() throws Exception {
            Path file = tempDir.resolve("closed.txt");
            Files.writeString(file, "test data");
            FileChannel channel = FileChannel.open(file,
                StandardOpenOption.READ);
            channel.close();

            ByteBuffer buffer = ByteBuffer.allocate(10);
            assertThrows(ClosedChannelException.class,
                () -> channel.read(buffer));
        }
    }
}
