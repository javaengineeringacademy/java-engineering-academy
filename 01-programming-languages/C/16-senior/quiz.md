# Senior Level Quiz

## Questions

1. What is ABI stability?
2. What is an opaque pointer pattern?
3. How do you design for portability?
4. What is cache-friendly code?
5. What is branch prediction?
6. How do you handle technical debt?
7. What is a design review?
8. How do you mentor junior developers?
9. What is technical leadership?
10. How do you balance speed vs quality?
11. What is a software architecture decision record (ADR)?
12. What is the strangler fig pattern in system migration?
13. How do you design a C library for maximum portability?
14. What is the difference between horizontal and vertical scaling?
15. How do you approach debugging a complex production issue?

## Answers

1. Maintaining binary compatibility between versions
2. Hiding implementation details in headers
3. Using abstraction layers and conditional compilation
4. Organizing data for efficient cache usage
5. CPU predicting branch outcomes for optimization
6. Identifying and systematically improving code quality
7. Reviewing design decisions before implementation
8. Through code review, guidance, and knowledge sharing
9. Making technical decisions and guiding teams
10. Through risk assessment and incremental delivery
11. A document capturing a significant architectural choice, its context, alternatives considered, and consequences
12. Gradually replacing parts of a legacy system with new components while keeping the system running; avoids big-bang rewrites
13. Use portable types (`stdint.h`), avoid compiler extensions, use POSIX or abstraction layers, and test on multiple platforms
14. Horizontal: add more machines (scale out); vertical: add more resources to a single machine (scale up); trade-offs in cost, complexity, and fault tolerance
15. Reproduce the issue, check logs and monitoring, use debuggers and profilers, form hypotheses, binary search the codebase, and validate fixes with tests
