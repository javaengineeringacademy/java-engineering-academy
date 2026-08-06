# Finding java.nio Classes in the Source

The `java.nio` module provides non-blocking I/O, buffers, channels, and file system APIs. Source lives under `src/java.base/share/classes/java/nio/`.

## Key Packages and Locations

### java.nio (Buffers)

| Class | Path |
|-------|------|
| `ByteBuffer` | `src/java.base/share/classes/java/nio/ByteBuffer.java` |
| `CharBuffer` | `src/java.base/share/classes/java/nio/CharBuffer.java` |
| `IntBuffer` | `src/java.base/share/classes/java/nio/IntBuffer.java` |
| `LongBuffer` | `src/java.base/share/classes/java/nio/LongBuffer.java` |
| `DirectByteBuffer` | `src/java.base/share/classes/java/nio/DirectByteBuffer.java` |
| `HeapByteBuffer` | `src/java.base/share/classes/java/nio/HeapByteBuffer.java` |

### java.nio.channels

| Class | Path |
|-------|------|
| `Channel` | `src/java.base/share/classes/java/nio/channels/Channel.java` |
| `FileChannel` | `src/java.base/share/classes/java/nio/channels/FileChannel.java` |
| `SocketChannel` | `src/java.base/share/classes/java/nio/channels/SocketChannel.java` |
| `ServerSocketChannel` | `src/java.base/share/classes/java/nio/channels/ServerSocketChannel.java` |
| `DatagramChannel` | `src/java.base/share/classes/java/nio/channels/DatagramChannel.java` |
| `Selector` | `src/java.base/share/classes/java/nio/channels/Selector.java` |
| `SelectionKey` | `src/java.base/share/classes/java/nio/channels/SelectionKey.java` |
| `SelectableChannel` | `src/java.base/share/classes/java/nio/channels/SelectableChannel.java` |

### java.nio.channels.spi

| Class | Path |
|-------|------|
| `SelectorProvider` | `src/java.base/share/classes/java/nio/channels/spi/SelectorProvider.java` |

### java.nio.file

| Class | Path |
|-------|------|
| `Path` | `src/java.base/share/classes/java/nio/file/Path.java` |
| `Files` | `src/java.base/share/classes/java/nio/file/Files.java` |
| `FileSystems` | `src/java.base/share/classes/java/nio/file/FileSystems.java` |
| `FileSystem` | `src/java.base/share/classes/java/nio/file/FileSystem.java` |
| `WatchService` | `src/java.base/share/classes/java/nio/file/WatchService.java` |
| `StandardWatchEventKinds` | `src/java.base/share/classes/java/nio/file/StandardWatchEventKinds.java` |
| `attribute/` | File attributes package |
| `spi/` | FileSystemProvider SPI |

### java.nio.charset

| Class | Path |
|-------|------|
| `Charset` | `src/java.base/share/classes/java/nio/charset/Charset.java` |
| `CharsetEncoder` | `src/java.base/share/classes/java/nio/charset/CharsetEncoder.java` |
| `CharsetDecoder` | `src/java.base/share/classes/java/nio/charset/CharsetDecoder.java` |
| `StandardCharsets` | `src/java.base/share/classes/java/nio/charset/StandardCharsets.java` |

### Platform-Specific NIO

| Platform | Path |
|----------|------|
| Linux | `src/java.base/unix/native/libnio/ch/` |
| Windows | `src/java.base/windows/native/libnio/ch/` |
| macOS | `src/java.base/macosx/native/libnio/ch/` |

## FileChannel Internals

```
FileChannel (Java)
  → UnixFileDispatcherImpl (native, Linux: epoll)
  → FileDispatcher (C++ in HotSpot)
```

The Linux implementation uses `epoll` for non-blocking I/O. The Windows implementation uses `IOCP`.

## Selector Implementation

The `Selector` implementation varies by platform:

| Platform | Implementation |
|----------|----------------|
| Linux | `EPollSelectorImpl` (epoll) |
| macOS | `KQueueSelectorImpl` (kqueue) |
| Windows | `WindowsSelectorImpl` (IOCP) |
| Solaris | `DevPollSelectorImpl` (dev/poll) |

## Finding Native Methods

```bash
# Find NIO native methods
rg "Java_sun_nio_ch" src/java.base/ --include="*.cpp"
rg "Java_sun_nio_fs" src/java.base/ --include="*.cpp"

# Find specific implementations
rg "FileChannelImpl" src/java.base/ --include="*.java"
```
