package academy.javaengineering.testing.performance.examples;

import org.openjdk.jmh.annotations.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class PerformanceTestingExamples {

    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private HashSet<Integer> hashSet;
    private TreeSet<Integer> treeSet;

    @Setup
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        hashSet = new HashSet<>();
        treeSet = new TreeSet<>();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(i);
            linkedList.add(i);
            hashSet.add(i);
            treeSet.add(i);
        }
    }

    @Benchmark
    public int arrayListGet() {
        return arrayList.get(500);
    }

    @Benchmark
    public boolean linkedListContains() {
        return linkedList.contains(500);
    }

    @Benchmark
    public boolean hashSetContains() {
        return hashSet.contains(500);
    }

    @Benchmark
    public boolean treeSetContains() {
        return treeSet.contains(500);
    }
}
