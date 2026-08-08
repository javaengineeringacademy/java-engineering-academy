# Iteration Mastery Roadmap

## Phase 1: Foundations (Week 1)

### Day 1-2: Traditional Loops
- [ ] Master `for` loop syntax and variable scope
- [ ] Understand `while` and `do-while` differences
- [ ] Practice break/continue in nested loops
- [ ] Solve: 5 for-loop exercises, 5 while-loop exercises

### Day 3-4: Enhanced For Loop
- [ ] Understand syntactic sugar over Iterator
- [ ] Learn why ConcurrentModificationException occurs
- [ ] Practice with arrays and Iterable objects
- [ ] Solve: 5 enhanced-for exercises

### Day 5: While Loop Patterns
- [ ] Scanner/BufferedReader reading patterns
- [ ] Sentinel value processing
- [ ] Input validation loops
- [ ] Solve: 5 while-loop exercises

## Phase 2: Iterator Mastery (Week 2)

### Day 6-7: Iterator Interface
- [ ] Implement hasNext(), next(), remove()
- [ ] Understand fail-fast behavior and modCount
- [ ] Practice safe deletion during iteration
- [ ] Solve: 5 iterator exercises

### Day 8-9: ListIterator
- [ ] Bidirectional traversal with hasPrevious(), previous()
- [ ] Modification: add(), set() during iteration
- [ ] Starting from specific index
- [ ] Solve: 5 list-iterator exercises

### Day 10: Enumeration (Legacy)
- [ ] Understand Vector/Hashtable iteration
- [ ] Know when you'll encounter it (legacy code)
- [ ] Practice replacement strategies
- [ ] Solve: 5 enumeration exercises

## Phase 3: Modern Iteration (Week 3)

### Day 11-12: Spliterator
- [ ] Understand characteristics (ORDERED, SIZED, etc.)
- [ ] Practice tryAdvance() and forEachRemaining()
- [ ] Implement trySplit() for parallelism
- [ ] Solve: 5 spliterator exercises

### Day 13-14: Stream Iteration
- [ ] Internal vs external iteration concepts
- [ ] Lazy evaluation mechanics
- [ ] Pipeline: source → intermediate → terminal
- [ ] Parallel streams and ForkJoinPool
- [ ] Solve: 5 stream iteration exercises

### Day 15: Performance Comparison
- [ ] Benchmark all 8 mechanisms
- [ ] Memory profiling
- [ ] Parallel vs sequential analysis

## Phase 4: Production Readiness (Week 4)

### Day 16-17: Common Bugs
- [ ] ConcurrentModificationException patterns
- [ ] Off-by-one errors
- [ ] Infinite loop debugging
- [ ] Memory leak from Iterator references

### Day 18-19: Real-World Patterns
- [ ] Iterator pattern in microservices
- [ ] Custom Iterable implementations
- [ ] Stream integration with Optional
- [ ] Reactive Streams (Flow API)

### Day 20: Interview Prep
- [ ] 10 interview questions practice
- [ ] Whiteboard: implement Iterator for custom collection
- [ ] Explain fail-fast with bytecode analysis
- [ ] Parallel stream internals discussion

## Milestones

| Milestone | Skills Unlocked | Assessment |
|-----------|----------------|------------|
| Week 1 Complete | Basic traversal | Can iterate any collection correctly |
| Week 2 Complete | Safe modification | Can delete/filter during iteration |
| Week 3 Complete | Functional style | Can convert loops to streams |
| Week 4 Complete | Production-ready | Can debug iteration bugs in 30 min |

## Tools to Install

```bash
# JMH for benchmarking
# Add to pom.xml or build.gradle
org.openjdk.jmh:jmh-core:1.37
org.openjdk.jmh:jmh-generator-annprocess:1.37

# Bytecode analysis
javap -c EnhancedForLoopDemo.class  # Shows Iterator under the hood
```

## Recommended Reading

1. *Effective Java* - Item 58: Use for-each loops over for loops
2. *Java Concurrency in Practice* - Chapter 8: Thread Safety
3. *Modern Java in Action* - Chapter 5: Working with streams
4. *Core Java Vol 1* - Chapter 13: Collections (iteration sections)

## Self-Check Questions

After each week, verify:

**Week 1:** Can you write a for loop that processes every 3rd element? Can you read until "quit" with while?

**Week 2:** Can you remove all negative numbers from a List using Iterator? Can you traverse a List backwards with ListIterator?

**Week 3:** Can you parallelize a CPU-intensive iteration with Spliterator? Can you write a parallel stream that sorts and filters?

**Week 4:** Can you fix a ConcurrentModificationException? Can you implement Iterable for a custom tree?
