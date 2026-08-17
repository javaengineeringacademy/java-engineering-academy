# Control Flow References

## Official Documentation

- [Control Flow Statements](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/flow.html) — if, else, switch, for, while, do-while
- [Switch Expressions (Java 14+)](https://openjdk.org/jeps/361) — Enhanced switch with arrow syntax
- [Pattern Matching for switch (Java 21)](https://openjdk.org/jeps/441) — Pattern matching in switch statements
- [Enhanced for Loop](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/for.html) — For-each syntax

## Language Specification

- [JLS: if-then-else](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.4.1) — If statement specification
- [JLS: switch](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.11) — Switch statement specification
- [JLS: for loop](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.14.1) — For loop specification
- [JLS: while loop](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.12) — While loop specification
- [JLS: break](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.15) — Break statement specification
- [JLS: continue](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.16) — Continue statement specification

## Control Flow Patterns

- [Guard Clause Pattern](https://refactoring.guards/) — Reducing nesting with early returns
- [Strategy Pattern with Switch](https://www.baeldung.com/java-strategy-pattern) — Using switch for strategy selection
- [State Machine with Enums](https://www.baeldung.com/java-state-machine) — Implementing state machines

## Performance

- [Branch Prediction](https://en.wikipedia.org/wiki/Branch_predictor) — How CPUs optimize conditional branching
- [Loop Unrolling](https://en.wikipedia.org/wiki/Loop_unrolling) — Compiler optimization for loops
- [JIT Compilation of Loops](https://wiki.openjdk.org/display/HotSpot/Loop+Optimizations) — JVM loop optimizations

## Testing Control Flow

- [JUnit 5 Parameterized Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests) — Testing multiple branches
- [Test Coverage for Conditionals](https://www.jacoco.org/) — Ensuring all paths are tested

## Common Patterns

- [Null Check Patterns](https://www.baeldung.com/java-null-checks) — Handling null values
- [Optional for Control Flow](https://www.baeldung.com/java-optional) — Replacing null checks with Optional
- [Switch Expression Best Practices](https://www.baeldung.com/java-switch-expression) — Modern switch usage

## Related Topics in This Course

- [01 - Variables](../01-variables/) — Values used in conditions
- [02 - Operators](../02-operators/) — Logical operators for conditions
- [04 - Methods](../04-methods/) — Method calls within control flow
