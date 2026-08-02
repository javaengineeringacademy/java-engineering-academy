package academy.javaengineering.iotutorial;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("NIO Buffer Tests")
class NioBufferTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("Buffer Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create heap buffer")
        void shouldCreateHeapBuffer() {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            assertNotNull(buffer);
            assertEquals(1024, buffer.capacity());
            assertEquals(0, buffer.position());
            assertEquals(1024, buffer.limit());
            assertTrue(buffer.hasArray());
        }

        @Test
        @DisplayName("Should create direct buffer")
        void shouldCreateDirectBuffer() {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            assertNotNull(buffer);
            assertEquals(1024, buffer.capacity());
            assertFalse(buffer.hasArray());
        }

        @Test
        @DisplayName("Should wrap array")
        void shouldWrapArray() {
            byte[] array = {1, 2, 3, 4, 5};
            ByteBuffer buffer = ByteBuffer.wrap(array);
            assertEquals(5, buffer.capacity());
            assertEquals(5, buffer.limit());
            assertEquals(1, buffer.get(0));
        }

        @Test
        @DisplayName("Should wrap with offset")
        void shouldWrapWithOffset() {
            byte[] array = {0, 0, 10, 20, 30};
            ByteBuffer buffer = ByteBuffer.wrap(array, 2, 3);
            assertEquals(5, buffer.capacity());
            assertEquals(2, buffer.position());
            assertEquals(5, buffer.limit());
        }
    }

    @Nested
    @DisplayName("Buffer Operations Tests")
    class OperationTests {

        @Test
        @DisplayName("Should put and get byte")
        void shouldPutAndGet() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 42);
            buffer.flip();
            assertEquals(42, buffer.get());
        }

        @Test
        @DisplayName("Should flip correctly")
        void shouldFlip() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 1);
            buffer.put((byte) 2);
            buffer.flip();
            assertEquals(0, buffer.position());
            assertEquals(2, buffer.limit());
        }

        @Test
        @DisplayName("Should clear correctly")
        void shouldClear() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 1);
            buffer.clear();
            assertEquals(0, buffer.position());
            assertEquals(10, buffer.limit());
        }

        @Test
        @DisplayName("Should compact correctly")
        void shouldCompact() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 1);
            buffer.put((byte) 2);
            buffer.put((byte) 3);
            buffer.flip();
            buffer.get(); // Read first byte
            buffer.compact();
            assertEquals(2, buffer.position());
            assertEquals(10, buffer.limit());
        }

        @Test
        @DisplayName("Should mark and reset")
        void shouldMarkAndReset() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 1);
            buffer.put((byte) 2);
            buffer.mark();
            buffer.put((byte) 3);
            assertEquals(3, buffer.position());
            buffer.reset();
            assertEquals(2, buffer.position());
        }

        @Test
        @DisplayName("Should rewind")
        void shouldRewind() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 1);
            buffer.put((byte) 2);
            buffer.rewind();
            assertEquals(0, buffer.position());
            assertEquals(10, buffer.limit());
        }
    }

    @Nested
    @DisplayName("Byte Order Tests")
    class ByteOrderTests {

        @Test
        @DisplayName("Should use big-endian by default")
        void shouldUseBigEndian() {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            assertEquals(ByteOrder.BIG_ENDIAN, buffer.order());
        }

        @Test
        @DisplayName("Should write and read int with big-endian")
        void shouldWriteReadBigEndian() {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putInt(0x12345678);
            buffer.flip();
            assertEquals(0x12345678, buffer.getInt());
        }

        @Test
        @DisplayName("Should write and read int with little-endian")
        void shouldWriteReadLittleEndian() {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(0x12345678);
            buffer.flip();
            assertEquals(0x12345678, buffer.getInt());
        }

        @Test
        @DisplayName("Should represent different byte orders")
        void shouldRepresentDifferentOrders() {
            int value = 0x12345678;

            ByteBuffer bigEndian = ByteBuffer.allocate(4);
            bigEndian.order(ByteOrder.BIG_ENDIAN);
            bigEndian.putInt(value);
            bigEndian.flip();

            ByteBuffer littleEndian = ByteBuffer.allocate(4);
            littleEndian.order(ByteOrder.LITTLE_ENDIAN);
            littleEndian.putInt(value);
            littleEndian.flip();

            // Big-endian: [12, 34, 56, 78]
            assertEquals(0x12, bigEndian.get() & 0xFF);
            // Little-endian: [78, 56, 34, 12]
            assertEquals(0x78, littleEndian.get() & 0xFF);
        }
    }

    @Nested
    @DisplayName("Buffer Slicing Tests")
    class SlicingTests {

        @Test
        @DisplayName("Should create slice")
        void shouldCreateSlice() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            for (int i = 0; i < 10; i++) {
                buffer.put((byte) (i * 10));
            }
            buffer.flip();
            buffer.position(2);
            buffer.limit(5);

            ByteBuffer slice = buffer.slice();
            assertEquals(3, slice.capacity());
            assertEquals(0, slice.position());
            assertEquals(3, slice.limit());
        }

        @Test
        @DisplayName("Should share data with slice")
        void shouldShareDataWithSlice() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put(0, (byte) 100);

            buffer.position(0);
            buffer.limit(5);
            ByteBuffer slice = buffer.slice();

            assertEquals(100, slice.get(0));
        }

        @Test
        @DisplayName("Should modify slice affects original")
        void shouldModifyAffectsOriginal() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put(0, (byte) 10);

            buffer.position(0);
            buffer.limit(5);
            ByteBuffer slice = buffer.slice();
            slice.put(0, (byte) 99);

            assertEquals(99, buffer.get(0));
        }
    }

    @Nested
    @DisplayName("Buffer View Tests")
    class ViewTests {

        @Test
        @DisplayName("Should create int buffer view")
        void shouldCreateIntView() {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putInt(12345);
            buffer.flip();

            IntBuffer intView = buffer.asIntBuffer();
            assertEquals(12345, intView.get(0));
        }

        @Test
        @DisplayName("Should create double buffer view")
        void shouldCreateDoubleView() {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            buffer.putDouble(3.14159);
            buffer.flip();

            DoubleBuffer doubleView = buffer.asDoubleBuffer();
            assertEquals(3.14159, doubleView.get(0), 0.0001);
        }

        @Test
        @DisplayName("Should duplicate buffer")
        void shouldDuplicate() {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put((byte) 42);
            buffer.flip();

            ByteBuffer dup = buffer.duplicate();
            assertEquals(42, dup.get(0));
            assertEquals(buffer.capacity(), dup.capacity());
        }
    }

    @Nested
    @DisplayName("File IO Tests")
    class FileIOTests {

        @Test
        @DisplayName("Should write and read file with buffer")
        void shouldWriteReadWithBuffer() throws Exception {
            Path file = tempDir.resolve("buffer-io.txt");

            // Write
            ByteBuffer writeBuf = ByteBuffer.wrap("Hello NIO".getBytes());
            try (var channel = java.nio.channels.FileChannel.open(file,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.WRITE)) {
                while (writeBuf.hasRemaining()) {
                    channel.write(writeBuf);
                }
            }

            // Read
            ByteBuffer readBuf = ByteBuffer.allocate(1024);
            try (var channel = java.nio.channels.FileChannel.open(file,
                    java.nio.file.StandardOpenOption.READ)) {
                channel.read(readBuf);
            }
            readBuf.flip();

            String content = StandardCharsets.UTF_8.decode(readBuf).toString();
            assertEquals("Hello NIO", content);
        }

        @Test
        @DisplayName("Should handle buffer overflow")
        void shouldHandleOverflow() {
            ByteBuffer buffer = ByteBuffer.allocate(5);
            buffer.put((byte) 1);
            buffer.put((byte) 2);
            buffer.put((byte) 3);
            buffer.put((byte) 4);
            buffer.put((byte) 5);
            assertThrows(BufferOverflowException.class,
                () -> buffer.put((byte) 6));
        }

        @Test
        @DisplayName("Should handle buffer underflow")
        void shouldHandleUnderflow() {
            ByteBuffer buffer = ByteBuffer.allocate(5);
            buffer.put((byte) 1);
            buffer.flip();
            buffer.get(); // Read the one byte
            assertThrows(BufferUnderflowException.class,
                () -> buffer.get());
        }
    }
}
