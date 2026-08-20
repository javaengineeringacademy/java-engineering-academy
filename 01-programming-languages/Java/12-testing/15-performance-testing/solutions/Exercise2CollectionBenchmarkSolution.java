package academy.javaengineering.testing.performance.solutions;

import org.openjdk.jmh.annotations.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class Exercise2CollectionBenchmarkSolution {

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
        return arrayList.get(50);
    }

    @Benchmark
    public Integer linkedListGet() {
        return linkedList.get(50);
    }
}
