# 06. GC Algorithms - Quiz

## Questions

### Q1: G1 Regions
How does G1 GC manage heap memory?
- A) Contiguous Young and Old generations
- B) Equal-sized regions that can be Eden, Survivor, Old, or Humongous
- C) Fixed-size pages with demand paging
- D) Single contiguous space with mark-sweep

**Answer: B**
Explanation: G1 divides the heap into equal-sized regions (1-32MB). Each region can be assigned as Eden, Survivor, Old, or Humongous depending on the collection phase.

### Q2: ZGC Colored Pointers
What technique does ZGC use for concurrent collection?
- A) Card tables
- B) Colored pointers with load barriers
- C) Brooks pointers
- D) Reference counting

**Answer: B**
Explanation: ZGC encodes GC state in pointer metadata (colors). Load barriers check pointer colors and self-heal, enabling nearly all collection work to happen concurrently.

### Q3: Shenandoah Brooks Pointers
What is the key technique used by Shenandoah?
- A) Colored pointers
- B) Brooks pointers (forwarding pointers stored in object headers)
- C) Card marking
- D) Snapshot-at-the-beginning

**Answer: B**
Explanation: Shenandoah stores a Brooks pointer in each object header that points to the object's current location. This enables concurrent compaction by allowing concurrent object relocation.

### Q4: G1 Mixed GC
When does G1 perform a Mixed GC?
- A) When any region is full
- B) After concurrent marking, when Old regions exceed IHOP threshold
- C) Only during Full GC
- D) Every 10 Minor GCs

**Answer: B**
Explanation: G1 Mixed GCs collect Young regions plus selected Old regions that have the most garbage. They are triggered when Old region occupancy exceeds the InitiatingHeapOccupancyPercent (IHOP).

### Q5: ZGC Pause Phases
How many stop-the-world pause phases does ZGC have?
- A) One
- B) Two
- C) Three
- D) None

**Answer: C**
Explanation: ZGC has three brief STW pauses: Pause Mark Start (root scanning), Pause Mark End (reference processing), and Pause Relocate Start (initialize relocation). Each is sub-millisecond.

### Q6: G1 Evacuation
What happens during G1 evacuation?
- A) Regions are deleted from the heap
- B) Live objects are copied from selected regions to new regions
- C) Old regions are compacted in place
- D) Free regions are added to the heap

**Answer: B**
Explanation: During evacuation, G1 copies live objects from selected regions to new regions (typically in Survivor or Old space). The original regions become free after evacuation.

### Q7: Concurrent Marking
What is the purpose of concurrent marking in G1 and ZGC?
- A) To copy objects between regions
- B) To identify reachable objects while application threads run
- C) To compact the heap
- D) To resize the heap

**Answer: B**
Explanation: Concurrent marking traces the object graph from GC roots while application threads continue running. Only brief pauses are needed at the start and end of marking.

### Q8: Shenandoah Heuristics
What does the Shenandoah "compact" heuristics mode do?
- A) Avoids compaction entirely
- B) Aggressively compacts to minimize memory usage
- C) Only compacts during Full GC
- D) Uses mark-sweep without compaction

**Answer: B**
Explanation: The compact heuristics mode in Shenandoah prioritizes aggressive compaction, keeping memory usage low at the cost of more concurrent work.

### Q9: G1 Humongous Objects
What are humongous objects in G1?
- A) Objects larger than 50% of a region size
- B) Objects stored in the Old generation only
- C) Objects that have survived many GC cycles
- D) Objects allocated directly in Metaspace

**Answer: A**
Explanation: Humongous objects are larger than 50% of a G1 region size. They are allocated in special humongous regions and can cause issues if they are frequent or very large.

### Q10: Algorithm Selection
For a latency-critical application requiring < 10ms pauses with a 32GB heap, which collector is best?
- A) Serial GC
- B) Parallel GC
- C) G1 GC
- D) ZGC

**Answer: D**
Explanation: ZGC is designed for ultra-low latency with sub-millisecond pauses regardless of heap size. G1 targets 200ms pauses by default. ZGC is the best choice for < 10ms targets.

## Score Guide
- **9-10 correct**: GC algorithm expert
- **7-8 correct**: Solid understanding, review specific algorithm internals
- **5-6 correct**: Good start, study collector comparisons
- **Below 5**: Review basics before proceeding
