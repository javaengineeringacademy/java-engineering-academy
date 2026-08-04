package academy.javaengineering.iotutorial;

import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.channels.*;

/**
 * NIO Buffer Operations - Comprehensive examples of buffer manipulation.
 *
 * <p>This class demonstrates buffer creation, operations, byte order,
 * slicing, views, and practical IO operations using buffers.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class NioBufferExample {

    private NioBufferExample() {}

    public static void main(String[] args) throws Exception {
        System.out.println("=== NIO Buffer Demo ===\n");

        demonstrateBasicOps();
        demonstrateBufferStates();
        demonstrateByteOrder();
        demonstrateSlicing();
        demonstrateViews();
        demonstrateFileIO();
    }

    private static void demonstrateBasicOps() {
        System.out.println("--- Basic Buffer Operations ---");

        ByteBuffer buffer = ByteBuffer.allocate(20);

        System.out.println("Initial: pos=" + buffer.position()
            + ", lim=" + buffer.limit()
            + ", cap=" + buffer.capacity());

        // Put data
        buffer.put((byte) 10);
        buffer.put((byte) 20);
        buffer.put((byte) 30);
        System.out.println("After put: pos=" + buffer.position());

        // Flip
        buffer.flip();
        System.out.println("After flip: pos=" + buffer.position()
            + ", lim=" + buffer.limit());

        // Read data
        System.out.println("Read: " + buffer.get()
            + ", " + buffer.get()
            + ", " + buffer.get());
        System.out.println("After get: pos=" + buffer.position()
            + ", lim=" + buffer.limit());

        // Clear
        buffer.clear();
        System.out.println("After clear: pos=" + buffer.position()
            + ", lim=" + buffer.limit());
    }

    private static void demonstrateBufferStates() {
        System.out.println("\n--- Buffer State Transitions ---");

        ByteBuffer buf = ByteBuffer.allocate(10);

        // State 1: New/empty
        System.out.println("1. New: pos=0, lim=10, remaining=10");

        // State 2: Filled
        buf.put((byte) 1);
        buf.put((byte) 2);
        buf.put((byte) 3);
        System.out.println("2. Filled: pos=" + buf.position()
            + ", remaining=" + buf.remaining());

        // State 3: Flipped (readable)
        buf.flip();
        System.out.println("3. Flipped: pos=0, lim=" + buf.limit()
            + ", remaining=" + buf.remaining());

        // State 4: Partially read
        buf.get();
        buf.get();
        System.out.println("4. Partial read: pos=" + buf.position()
            + ", remaining=" + buf.remaining());

        // State 5: Compact (preserve unread)
        buf.compact();
        System.out.println("5. Compacted: pos=" + buf.position()
            + ", lim=" + buf.limit()
            + ", remaining=" + buf.remaining());
    }

    private static void demonstrateByteOrder() {
        System.out.println("\n--- Byte Order ---");

        int value = 0x12345678;

        // Big-endian (default)
        ByteBuffer bigEndian = ByteBuffer.allocate(4);
        bigEndian.order(ByteOrder.BIG_ENDIAN);
        bigEndian.putInt(value);
        bigEndian.flip();

        // Little-endian
        ByteBuffer littleEndian = ByteBuffer.allocate(4);
        littleEndian.order(ByteOrder.LITTLE_ENDIAN);
        littleEndian.putInt(value);
        littleEndian.flip();

        System.out.println("Value: 0x" + Integer.toHexString(value));
        System.out.print("Big-endian: ");
        while (bigEndian.hasRemaining()) {
            System.out.printf("0x%02X ", bigEndian.get());
        }
        System.out.println();

        System.out.print("Little-endian: ");
        while (littleEndian.hasRemaining()) {
            System.out.printf("0x%02X ", littleEndian.get());
        }
        System.out.println();
    }

    private static void demonstrateSlicing() {
        System.out.println("\n--- Buffer Slicing ---");

        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            buffer.put((byte) (i * 10));
        }
        buffer.flip();

        // Create slice from position 2 to 5
        buffer.position(2);
        buffer.limit(5);
        ByteBuffer slice = buffer.slice();

        System.out.println("Original: pos=" + buffer.position()
            + ", lim=" + buffer.limit());
        System.out.println("Slice: cap=" + slice.capacity()
            + ", pos=" + slice.position()
            + ", lim=" + slice.limit());

        // Read slice
        System.out.print("Slice data: ");
        while (slice.hasRemaining()) {
            System.out.print(slice.get() + " ");
        }
        System.out.println();

        // Modify slice (affects original)
        slice.put(0, (byte) 99);
        buffer.position(0);
        buffer.limit(10);
        System.out.println("Modified original[2]: " + buffer.get(2));
    }

    private static void demonstrateViews() {
        System.out.println("\n--- Buffer Views ---");

        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.order(ByteOrder.BIG_ENDIAN);

        // Write different types
        buffer.putInt(12345);
        buffer.putDouble(3.14159);
        buffer.putChar('A');

        buffer.flip();

        // Create views
        IntBuffer intView = buffer.asIntBuffer();
        DoubleBuffer doubleView = buffer.asDoubleBuffer();

        System.out.println("Int view: " + intView.get(0));
        System.out.println("Double view: " + doubleView.get(0));

        // Duplicate
        ByteBuffer dup = buffer.duplicate();
        dup.position(0);
        System.out.println("Duplicate capacity: " + dup.capacity());
    }

    private static void demonstrateFileIO() throws Exception {
        System.out.println("\n--- Buffer File IO ---");

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "nio-buffer-demo");
        Files.createDirectories(tempDir);

        // Write file with buffer
        Path file = tempDir.resolve("buffer-test.txt");
        ByteBuffer writeBuffer = ByteBuffer.wrap(
            "Hello from NIO Buffer!".getBytes(StandardCharsets.UTF_8));

        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            while (writeBuffer.hasRemaining()) {
                channel.write(writeBuffer);
            }
        }

        // Read file with buffer
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.READ)) {
            channel.read(readBuffer);
        }

        readBuffer.flip();
        String content = StandardCharsets.UTF_8.decode(readBuffer).toString();
        System.out.println("Read from file: " + content);

        // Cleanup
        Files.deleteIfExists(file);
        Files.deleteIfExists(tempDir);
    }
}
