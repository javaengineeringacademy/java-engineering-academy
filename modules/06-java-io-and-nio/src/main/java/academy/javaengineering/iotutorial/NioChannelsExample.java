package academy.javaengineering.iotutorial;

import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.charset.*;

/**
 * NIO Channel Operations - Comprehensive examples of channel usage.
 *
 * <p>This class demonstrates FileChannel operations, zero-copy transfers,
 * scatter/gather IO, and memory-mapped files.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class NioChannelsExample {

    private NioChannelsExample() {}

    public static void main(String[] args) throws Exception {
        System.out.println("=== NIO Channel Demo ===\n");

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "nio-channels-demo");
        Files.createDirectories(tempDir);

        demonstrateFileChannel(tempDir);
        demonstrateZeroCopy(tempDir);
        demonstrateScatterGather(tempDir);
        demonstrateMemoryMapped(tempDir);

        // Cleanup
        Files.walk(tempDir)
            .sorted(java.util.Comparator.reverseOrder())
            .forEach(path -> {
                try { Files.deleteIfExists(path); }
                catch (Exception ignored) {}
            });
    }

    private static void demonstrateFileChannel(Path dir) throws Exception {
        System.out.println("--- FileChannel Operations ---");

        Path file = dir.resolve("channel-test.txt");

        // Write using FileChannel
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {

            ByteBuffer buffer = ByteBuffer.wrap(
                "Hello from FileChannel!\nLine 2\nLine 3".getBytes(
                    StandardCharsets.UTF_8));
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }

        // Read using FileChannel
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.READ)) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            buffer.flip();

            String content = StandardCharsets.UTF_8.decode(buffer).toString();
            System.out.println("Read " + content.length() + " chars");
            System.out.println("Content: " + content);
        }

        // File info
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.READ)) {
            System.out.println("Size: " + channel.size() + " bytes");
            System.out.println("Position: " + channel.position());
        }
    }

    private static void demonstrateZeroCopy(Path dir) throws Exception {
        System.out.println("\n--- Zero-Copy Transfer ---");

        Path source = dir.resolve("source.txt");
        Files.writeString(source, "Zero-copy transfer test content!");

        Path target = dir.resolve("target.txt");

        // Zero-copy transfer
        long startTime = System.nanoTime();
        try (FileChannel srcChannel = FileChannel.open(source,
                StandardOpenOption.READ);
             FileChannel tgtChannel = FileChannel.open(target,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {

            long transferred = srcChannel.transferTo(
                0, srcChannel.size(), tgtChannel);
            System.out.println("Transferred: " + transferred + " bytes");
        }
        long elapsed = System.nanoTime() - startTime;
        System.out.println("Time: " + elapsed / 1000 + " microseconds");
        System.out.println("Content: " + Files.readString(target));
    }

    private static void demonstrateScatterGather(Path dir) throws Exception {
        System.out.println("\n--- Scatter/Gather IO ---");

        Path file = dir.resolve("scatter-gather.txt");

        // Gather write
        ByteBuffer header = ByteBuffer.wrap("HEADER:metadata\n".getBytes());
        ByteBuffer body = ByteBuffer.wrap("Body: actual content here".getBytes());

        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            channel.write(new ByteBuffer[]{header, body});
        }

        // Scatter read
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.READ)) {

            ByteBuffer readHeader = ByteBuffer.allocate(64);
            ByteBuffer readBody = ByteBuffer.allocate(256);
            ByteBuffer[] buffers = {readHeader, readBody};

            long totalRead = channel.read(buffers);
            System.out.println("Total read: " + totalRead + " bytes");

            readHeader.flip();
            readBody.flip();

            System.out.println("Header: " +
                StandardCharsets.UTF_8.decode(readHeader).toString().trim());
            System.out.println("Body: " +
                StandardCharsets.UTF_8.decode(readBody).toString().trim());
        }
    }

    private static void demonstrateMemoryMapped(Path dir) throws Exception {
        System.out.println("\n--- Memory-Mapped Files ---");

        Path file = dir.resolve("mapped.txt");
        String content = "Memory-mapped file content for random access!";

        // Write using memory-mapped buffer
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.CREATE,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE)) {

            MappedByteBuffer mapped = channel.map(
                FileChannel.MapMode.READ_WRITE,
                0,
                content.length()
            );

            mapped.put(content.getBytes(StandardCharsets.UTF_8));
            mapped.force();
        }

        // Read using memory-mapped buffer
        try (FileChannel channel = FileChannel.open(file,
                StandardOpenOption.READ)) {

            MappedByteBuffer mapped = channel.map(
                FileChannel.MapMode.READ_ONLY,
                0,
                channel.size()
            );

            byte[] data = new byte[(int) channel.size()];
            mapped.get(data);
            System.out.println("Mapped content: " + new String(data));

            // Random access - read from middle
            mapped.position(10);
            byte[] middle = new byte[10];
            mapped.get(middle);
            System.out.println("From position 10: " + new String(middle));
        }
    }
}
