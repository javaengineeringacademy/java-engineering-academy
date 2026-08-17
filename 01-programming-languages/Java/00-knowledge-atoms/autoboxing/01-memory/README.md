# Memory: Autoboxing

## Overview
This folder contains memory-related demonstrations for autoboxing.

## Files
- `AutoboxingMemory.java` — Demonstrates memory overhead of autoboxing and cache behavior

## What You'll Learn
- Memory cost of autoboxing (object headers, alignment padding)
- How the Integer cache affects memory retention
- Memory implications of autoboxing in collections
- Heap usage patterns with wrapper vs primitive types

## Key Concepts
- Object header overhead: 12-16 bytes per wrapper object
- Cache range objects are permanently retained in memory
- Collections of wrappers consume significantly more memory than primitive arrays
