# Internals: Immutability

## Overview
This folder contains internal implementation details of immutability in Java — how the JVM enforces immutability guarantees.

## Files
- `ImmutabilityInternals.java` — Demonstrates final field semantics, defensive copying, and safe publication

## What You'll Learn
- How final fields guarantee safe publication across threads
- The JVM's role in enforcing immutability at the memory level
- Defensive copying techniques for mutable internal state
- How records achieve immutability automatically

## Prerequisites
- Understanding of final keyword
- Familiarity with object construction and publication
