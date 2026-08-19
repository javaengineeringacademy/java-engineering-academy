# 05. Garbage Collection - Quiz

## Questions

### Q1: GC Roots
Which of the following is NOT a GC root?
- A) Local variables on the stack
- B) Static fields in loaded classes
- C) Objects in the Young Generation
- D) JNI references

**Answer: C**
Explanation: GC roots include stack references, static fields, JNI references, monitors, and JVM internal references. Objects in the Young Generation are not roots; they are reached FROM roots.

### Q2: Minor vs Major GC
What is the difference between Minor GC and Major GC?
- A) Minor GC is faster than Major GC
- B) Minor GC collects Young Generation, Major GC collects entire heap
- C) There is no difference
- D) Minor GC pauses all threads, Major GC does not

**Answer: B**
Explanation: Minor GC collects the Young Generation (Eden + Survivors). Major GC (or Full GC) collects the entire heap including Old Generation. Major GC typically has longer pauses.

### Q3: G1 GC
What is the key feature of G1 GC?
- A) It uses a single contiguous heap
- B) It divides the heap into regions for more predictable pauses
- C) It is single-threaded
- D) It never compacts the heap

**Answer: B**
Explanation: G1 divides the heap into equal-sized regions and can collect individual regions independently, providing more predictable pause times than traditional collectors.

### Q4: ZGC
What is the maximum pause time target for ZGC?
- A) 200ms
- B) 50ms
- C) 10ms
- D) Less than 1ms

**Answer: D**
Explanation: ZGC targets sub-millisecond pause times regardless of heap size. It achieves this through concurrent collection with load barriers and colored pointers.

### Q5: Promotion
What happens when an object survives enough Minor GC cycles?
- A) It is collected
- B) It is promoted to Old Generation
- C) It is moved to a Survivor space
- D) It is compacted

**Answer: B**
Explanation: After surviving MaxTenuringThreshold (default 15) Minor GC cycles, objects are promoted from Young to Old Generation.

### Q6: TLAB
What is a TLAB (Thread-Local Allocation Buffer)?
- A) A thread-safe data structure
- B) A region of Eden space allocated to a specific thread for lock-free allocation
- C) A type of garbage collector
- D) A memory barrier

**Answer: B**
Explanation: TLABs are small chunks of Eden space assigned to individual threads. Each thread allocates objects in its own TLAB without synchronization, improving allocation performance.

### Q7: Stop-the-World
When does a stop-the-world pause occur?
- A) Only during Full GC
- B) During any GC phase that requires all application threads to be paused
- C) Never with concurrent collectors
- D) Only when OutOfMemoryError occurs

**Answer: B**
Explanation: Stop-the-world pauses occur when the JVM needs a consistent snapshot of the heap. Even concurrent collectors have brief STW phases (e.g., root scanning).

### Q8: Memory Leak
Can the garbage collector prevent all memory leaks?
- A) Yes, GC prevents all memory leaks
- B) No, GC cannot collect objects still reachable from GC roots
- C) Only if the heap is large enough
- D) Only with the G1 collector

**Answer: B**
Explanation: GC only collects unreachable objects. If code unintentionally keeps references (e.g., static collections, ThreadLocal), the objects remain reachable and cannot be collected.

### Q9: Full GC Causes
Which of the following does NOT cause a Full GC?
- A) Old Generation is full
- B) Metaspace is full
- C) System.gc() is called
- D) Young Generation is full

**Answer: D**
Explanation: Young Generation full triggers Minor GC, not Full GC. Full GC is triggered by Old/Metaspace full, System.gc() (if enabled), or promotion failure.

### Q10: GC Logging
What is the correct way to enable GC logging in Java 11+?
- A) -verbose:gc
- B) -Xlog:gc*:file=gc.log
- C) -XX:+PrintGCDetails
- D) -XX:+UseGCLogFileRotation

**Answer: B**
Explanation: In Java 9+, the unified logging system uses -Xlog. The syntax -Xlog:gc* enables all GC logging. Older flags like -verbose:gc and -XX:+PrintGCDetails are deprecated.

## Score Guide
- **9-10 correct**: GC expert
- **7-8 correct**: Solid understanding, review collector specifics
- **5-6 correct**: Good start, study GC algorithm details
- **Below 5**: Review basics before proceeding
