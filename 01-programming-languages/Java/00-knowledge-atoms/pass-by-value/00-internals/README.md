# Internals: Pass by Value

## Overview
This folder contains internal implementation details of how Java passes arguments to methods.

## Files
- `PassByValueInternals.java` — Demonstrates how references are copied and how method calls work at the JVM level

## What You'll Learn
- How the JVM copies primitive values and object references on the stack
- Why reassigning a method parameter does not affect the caller
- How return values interact with pass-by-value semantics
- The difference between modifying object state vs reassigning a reference

## Prerequisites
- Understanding of stack vs heap memory
- Basic knowledge of method invocation
