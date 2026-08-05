# JVM Internals

## Overview

The Java Virtual Machine (JVM) executes Java bytecode and provides the runtime environment for Java applications. HotSpot, the primary JVM implementation, uses JIT compilation and adaptive optimization for high performance.

## Architecture

The JVM consists of the class loader subsystem, runtime data areas, execution engine, and native method interface. Each component handles specific aspects of program execution.

## Class Loading

The class loader subsystem loads, links, and initializes Java classes. Bootstrap, extension, and application class loaders form a hierarchy that provides namespace isolation and security.

## Memory Areas

JVM memory includes heap, method area, stack, program counter, and native method stack. The heap stores objects while stacks handle method execution and local variables.

## JIT Compilation

HotSpot JIT compilers (C1 and C2) convert frequently executed bytecode to native machine code. The tiered compilation model balances startup time with peak performance.

## Garbage Collection

JVM garbage collectors include Serial, Parallel, CMS, G1, and ZGC. Each collector optimizes for different goals: throughput, pause time, or memory footprint.

## Performance Tuning

Key tuning parameters include heap size (-Xmx, -Xms), garbage collector selection, thread stack size, and JIT compilation thresholds. Profiling identifies optimization opportunities.

## Monitoring and Diagnostics

JMX, jcmd, jstat, and VisualVM provide runtime monitoring. Flight Recorder captures low-overhead production profiling data for performance analysis.

## Platform Independence

The JVM enables platform independence through bytecode execution. Each platform provides a JVM implementation that translates bytecode to native instructions while maintaining consistent behavior.
