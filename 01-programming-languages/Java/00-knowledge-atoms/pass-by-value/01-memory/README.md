# Memory: Pass by Value

## Overview
This folder contains memory-related demonstrations for pass-by-value semantics.

## Files
- `PassByValueMemory.java` — Demonstrates stack frame creation, reference copying, and shared object memory

## What You'll Learn
- How method calls create stack frames with copied parameters
- Memory layout of references on the stack vs objects on the heap
- How pass-by-value prevents unintended reference corruption
- Memory implications of returning objects vs modifying parameters

## Key Concepts
- Stack frame: local variables + copied parameters (8 bytes per reference)
- Object on heap: shared between caller and callee via copied references
- Return value: pushed onto stack, popped by caller
