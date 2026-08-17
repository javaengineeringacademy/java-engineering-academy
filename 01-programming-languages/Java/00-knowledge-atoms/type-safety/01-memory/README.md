# Memory: Type Safety

## Overview
This folder contains memory-related demonstrations for type safety.

## Files
- `TypeSafetyMemory.java` — Demonstrates memory implications of generics, type erasure, and casting

## What You'll Learn
- How type erasure affects memory layout (no generic type info at runtime)
- Memory cost of boxing/unboxing in generic collections
- How instanceof checks are implemented in the JVM
- Memory implications of raw types vs parameterized types

## Key Concepts
- Type erasure: generics exist only at compile time
- Boxing overhead: generic collections create wrapper objects
- instanceof: JVM checks class hierarchy at runtime
- Bridge methods: compiler-generated methods for type safety
