# Module 06: Java I/O and NIO

## Overview

This module covers Java's comprehensive Input/Output system, from traditional stream-based I/O to the modern New I/O (NIO) API. Students will learn to handle file operations, data streams, serialization, and asynchronous channel-based I/O for building efficient, scalable applications.

## Learning Objectives

By the end of this module, you will be able to:

- Read and write files using byte and character streams
- Implement buffered I/O for improved performance
- Work with Java NIO buffers, channels, and selectors
- Serialize and deserialize Java objects
- Process XML and JSON data formats
- Handle file system operations using NIO.2 API
- Apply best practices for resource management

## Prerequisites

- [Module 05: Generics](../05-generics/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Introduction](01-introduction/) | 1 hour | I/O concepts, stream types, and overview |
| 02 | [File Operations](02-file-operations/) | 2 hours | File class, path manipulation, file attributes |
| 03 | [Byte Streams](03-byte-streams/) | 2 hours | InputStream, OutputStream, file I/O |
| 04 | [Character Streams](04-character-streams/) | 2 hours | Reader, Writer, encoding/decoding |
| 05 | [Buffered Streams](05-buffered-streams/) | 1 hour | BufferedInputStream, BufferedReader |
| 06 | [Data Streams](06-data-streams/) | 2 hours | DataInputStream, DataOutputStream |
| 07 | [Object Streams](07-object-streams/) | 2 hours | Serialization, ObjectOutputStream |
| 08 | [Try-with-resources](08-try-with-resources/) | 1 hour | AutoCloseable, resource management |
| 09 | [Random Access File](09-random-access-file/) | 2 hours | RandomAccessFile, file positioning |
| 10 | [NIO Buffers](10-nio-buffers/) | 3 hours | Buffer operations, memory mapping |
| 11 | [NIO Channels](11-nio-channels/) | 3 hours | FileChannel, SocketChannel, Selectors |
| 12 | [NIO.2 FileSystem](12-nio2-filesystem/) | 2 hours | Files, Paths, directory streams |
| 13 | [Properties and Config](11-properties-config/) | 2 hours | Properties class, configuration files |
| 14 | [XML Processing](12-xml-processing/) | 3 hours | DOM, SAX, StAX parsers |
| 15 | [Jackson JSON](13-jackson-json/) | 3 hours | JSON serialization/deserialization |

## Key Concepts

- Stream-based vs. channel-based I/O
- Character encoding and Unicode support
- File locking and memory-mapped files
- Asynchronous I/O operations
- Serialization versioning and security

## Enterprise Applications

Java I/O is fundamental to enterprise applications for file processing, data import/export, logging systems, and integration with external systems. NIO provides non-blocking capabilities essential for high-performance networking and real-time data processing in microservices architectures.

## Estimated Total Time

**30 hours**

## Module Project

Build a **File Processing System** that:
- Reads CSV/JSON data files using streams
- Processes and transforms data
- Writes results to multiple output formats
- Implements file monitoring and watching
- Demonstrates NIO channel operations

## Resources

- [Java I/O Tutorial](https://docs.oracle.com/javase/tutorial/essential/io/)
- [Java NIO Documentation](https://docs.oracle.com/javase/8/docs/api/java/nio/package-summary.html)

**Previous Module**: [Module 05: Generics](../05-generics/)
**Next Module**: [Module 07: Functional Programming](../07-functional-programming/)