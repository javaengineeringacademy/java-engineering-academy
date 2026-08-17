# Internals: Java Memory Model

## Overview
This folder contains internal implementation details of the Java Memory Model — how threads interact through memory.

## Files
- `JavaMemoryModelInternals.java` — Demonstrates happens-before relationships, volatile semantics, and safe publication

## What You'll Learn
- How happens-before ordering guarantees memory visibility
- The difference between volatile and synchronized at the JVM level
- How the JIT compiler reorders operations and how to prevent it
- Safe publication patterns for multi-threaded initialization

## Prerequisites
- Understanding of threads and synchronization
- Familiarity with volatile and synchronized keywords
