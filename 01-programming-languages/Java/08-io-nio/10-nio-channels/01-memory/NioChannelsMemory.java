package academy.javaengineering.io.memory;

import java.nio.*;
import java.nio.channels.*;

public class NioChannelsMemory {

    public static void main(String[] args) {
        System.out.println("=== NIO Channels Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Channel Object Size
        System.out.println("--- Channel Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        try {
            FileChannel channel = FileChannel.open(
                java.nio.file.Path.of("/tmp"),
                java.nio.file.StandardOpenOption.READ);
            long after = rt.totalMemory() - rt.freeMemory();
            System.out.println("FileChannel: " + (after - before) + " bytes");
            channel.close();
        } catch (Exception e) {
            System.out.println("Channel creation: " + e.getMessage());
        }

        // 2. Buffer Requirements
        System.out.println("\n--- Buffer Requirements ---");
        System.out.println("Channel.read(buffer): fills buffer");
        System.out.println("Channel.write(buffer): drains buffer");
        System.out.println("Buffer must be flipped between read/write");

        // 3. Memory Mapping
        System.out.println("\n--- Memory Mapping ---");
        System.out.println("FileChannel.map(): memory-mapped file");
        System.out.println("MappedByteBuffer: direct buffer");
        System.out.println("OS manages file memory directly");
    }
}
