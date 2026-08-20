package academy.javaengineering.testing.performance.practices;

import org.openjdk.jmh.annotations.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 2: Collection Performance
 *
 * Tasks:
 * 1. Benchmark different collection implementations
 * 2. Compare ArrayList vs LinkedList
 * 3. Compare HashMap vs TreeMap
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Thread)
public class Exercise2CollectionBenchmark {

    private List<Integer> arrayList;
    private List<Integer> linkedList;

    @Setup
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
    }

    @Benchmark
    public Integer arrayListGet() {
        // TODO: Benchmark ArrayList.get()
        return arrayList.get(50);
    }

    @Benchmark
    public Integer linkedListGet() {
        // TODO: Benchmark LinkedList.get()
        return linkedList.get(50);
    }
}
