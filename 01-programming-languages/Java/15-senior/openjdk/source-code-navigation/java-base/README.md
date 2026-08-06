# Finding java.base Classes in the Source

The `java.base` module contains the most essential Java classes. All source lives under `src/java.base/share/classes/`.

## Key Packages and Locations

### java.lang

| Class | Path |
|-------|------|
| `Object` | `src/java.base/share/classes/java/lang/Object.java` |
| `String` | `src/java.base/share/classes/java/lang/String.java` |
| `System` | `src/java.base/share/classes/java/lang/System.java` |
| `Thread` | `src/java.base/share/classes/java/lang/Thread.java` |
| `Class` | `src/java.base/share/classes/java/lang/Class.java` |
| `Math` | `src/java.base/share/classes/java/lang/Math.java` |
| `Integer` | `src/java.base/share/classes/java/lang/Integer.java` |
| `Comparable` | `src/java.base/share/classes/java/lang/Comparable.java` |
| `Enum` | `src/java.base/share/classes/java/lang/Enum.java` |
| `Record` | `src/java.base/share/classes/java/lang/Record.java` |

### java.util

| Class | Path |
|-------|------|
| `ArrayList` | `src/java.base/share/classes/java/util/ArrayList.java` |
| `HashMap` | `src/java.base/share/classes/java/util/HashMap.java` |
| `LinkedList` | `src/java.base/share/classes/java/util/LinkedList.java` |
| `TreeMap` | `src/java.base/share/classes/java/util/TreeMap.java` |
| `Stream` | `src/java.base/share/classes/java/util/stream/Stream.java` |
| `Optional` | `src/java.base/share/classes/java/util/Optional.java` |
| `Collections` | `src/java.base/share/classes/java/util/Collections.java` |
| `Arrays` | `src/java.base/share/classes/java/util/Arrays.java` |
| `Random` | `src/java.base/share/classes/java/util/Random.java` |
| `UUID` | `src/java.base/share/classes/java/util/UUID.java` |

### java.io

| Class | Path |
|-------|------|
| `InputStream` | `src/java.base/share/classes/java/io/InputStream.java` |
| `OutputStream` | `src/java.base/share/classes/java/io/OutputStream.java` |
| `Reader` | `src/java.base/share/classes/java/io/Reader.java` |
| `Writer` | `src/java.base/share/classes/java/io/Writer.java` |
| `File` | `src/java.base/share/classes/java/io/File.java` |
| `BufferedReader` | `src/java.base/share/classes/java/io/BufferedReader.java` |
| `PrintWriter` | `src/java.base/share/classes/java/io/PrintWriter.java` |

### java.nio

| Class | Path |
|-------|------|
| `ByteBuffer` | `src/java.base/share/classes/java/nio/ByteBuffer.java` |
| `CharBuffer` | `src/java.base/share/classes/java/nio/CharBuffer.java` |
| `FileChannel` | `src/java.base/share/classes/java/nio/channels/FileChannel.java` |
| `SocketChannel` | `src/java.base/share/classes/java/nio/channels/SocketChannel.java` |
| `Selector` | `src/java.base/share/classes/java/nio/channels/Selector.java` |
| `Path` | `src/java.base/share/classes/java/nio/file/Path.java` |
| `Files` | `src/java.base/share/classes/java/nio/file/Files.java` |

### java.math

| Class | Path |
|-------|------|
| `BigInteger` | `src/java.base/share/classes/java/math/BigInteger.java` |
| `BigDecimal` | `src/java.base/share/classes/java/math/BigDecimal.java` |

### java.time

| Class | Path |
|-------|------|
| `LocalDate` | `src/java.base/share/classes/java/time/LocalDate.java` |
| `LocalTime` | `src/java.base/share/classes/java/time/LocalTime.java` |
| `Instant` | `src/java.base/share/classes/java/time/Instant.java` |
| `Duration` | `src/java.base/share/classes/java/time/Duration.java` |
| `ZonedDateTime` | `src/java.base/share/classes/java/time/ZonedDateTime.java` |

## Finding Internal Implementation

Many `java.lang` classes have native implementations:

```bash
# Find native method implementations
rg "Java_java_lang_Thread" src/java.base/ --include="*.cpp"

# Find JNI registrations
rg "REGISTER_NIJ_METHODS" src/java.base/ --include="*.cpp"
```

The native code for `java.lang` lives in:
- `src/java.base/unix/native/libjava/` (Linux/macOS)
- `src/java.base/windows/native/libjava/` (Windows)
