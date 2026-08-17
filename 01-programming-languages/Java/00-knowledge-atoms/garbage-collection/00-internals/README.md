# Internals: Garbage Collection

## Overview
This folder contains internal implementation details of how the JVM garbage collector works.

## Files
- `GarbageCollectionInternals.java` — Demonstrates GC mechanisms, reference types, and generational behavior

## What You'll Learn
- How the generational GC model works (Young Gen, Old Gen, Metaspace)
- Reference types (strong, soft, weak, phantom) and their GC behavior
- How object promotion from Young to Old generation works
- GC triggers and when different collectors activate

## Prerequisites
- Basic understanding of Java memory management
- Familiarity with heap and stack concepts
