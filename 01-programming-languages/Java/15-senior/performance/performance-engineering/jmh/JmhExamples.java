package performance.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(value = 2, jvmArgsAppend = {"-Xmx256m", "-Xms256m"})
public class JmhExamples {

    private static final int SIZE = 1000;
    private static final int LOOP_LIMIT = 100;
    private long[] primitiveArray;
    private Long[] boxedArray;
    private List<Long> boxedList;

    @Setup(Level.Trial)
    public void setup() {
        primitiveArray = new long[SIZE];
        boxedArray = new Long[SIZE];
        boxedList = new ArrayList<>(SIZE);

        for (int i = 0; i < SIZE; i++) {
            primitiveArray[i] = i;
            boxedArray[i] = (long) i;
            boxedList.add((long) i);
        }
    }

    @Benchmark
    public String stringConcatenation() {
        String result = "";
        for (int i = 0; i < LOOP_LIMIT; i++) {
            result += i;
        }
        return result;
    }

    @Benchmark
    public String stringConcatenationStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LOOP_LIMIT; i++) {
            sb.append(i);
        }
        return sb.toString();
    }

    @Benchmark
    public String stringConcatenationFormat() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LOOP_LIMIT; i++) {
            sb.append(String.format("%d", i));
        }
        return sb.toString();
    }

    @Benchmark
    public long autoboxedSum() {
        Long sum = 0L;
        for (int i = 0; i < SIZE; i++) {
            sum += primitiveArray[i];
        }
        return sum;
    }

    @Benchmark
    public long primitiveSum() {
        long sum = 0L;
        for (int i = 0; i < SIZE; i++) {
            sum += primitiveArray[i];
        }
        return sum;
    }

    @Benchmark
    public long boxedArraySum() {
        long sum = 0L;
        for (int i = 0; i < SIZE; i++) {
            sum += boxedArray[i];
        }
        return sum;
    }

    @Benchmark
    public long boxedListSum() {
        long sum = 0L;
        for (Long val : boxedList) {
            sum += val;
        }
        return sum;
    }

    @Benchmark
    public double streamSum() {
        return LongStream.rangeClosed(0, SIZE)
            .average()
            .orElse(0.0);
    }

    @Benchmark
    public double loopSum() {
        long sum = 0;
        for (long i = 0; i <= SIZE; i++) {
            sum += i;
        }
        return (double) sum / SIZE;
    }

    @Benchmark
    public double streamFilterMap() {
        return LongStream.rangeClosed(0, SIZE)
            .filter(i -> i % 2 == 0)
            .map(i -> i * 2)
            .average()
            .orElse(0.0);
    }

    @Benchmark
    public double loopFilterMap() {
        long sum = 0;
        int count = 0;
        for (long i = 0; i <= SIZE; i++) {
            if (i % 2 == 0) {
                sum += i * 2;
                count++;
            }
        }
        return count > 0 ? (double) sum / count : 0.0;
    }

    @Benchmark
    public Map<String, Long> streamCollect() {
        return LongStream.rangeClosed(0, SIZE)
            .boxed()
            .collect(Collectors.toMap(
                i -> "key" + i,
                i -> i
            ));
    }

    @Benchmark
    public Map<String, Long> loopCollect() {
        Map<String, Long> map = new HashMap<>();
        for (long i = 0; i <= SIZE; i++) {
            map.put("key" + i, i);
        }
        return map;
    }

    @Benchmark
    public void hashMapPut(Blackhole bh) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            map.put("key" + i, i);
        }
        bh.consume(map);
    }

    @Benchmark
    public void treeMapPut(Blackhole bh) {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < SIZE; i++) {
            map.put("key" + i, i);
        }
        bh.consume(map);
    }

    @Benchmark
    public void hashMapGet(Blackhole bh) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < SIZE; i++) {
            map.put("key" + i, i);
        }
        for (int i = 0; i < SIZE; i++) {
            bh.consume(map.get("key" + i));
        }
    }

    @Benchmark
    public void treeMapGet(Blackhole bh) {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < SIZE; i++) {
            map.put("key" + i, i);
        }
        for (int i = 0; i < SIZE; i++) {
            bh.consume(map.get("key" + i));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(JmhExamples.class.getSimpleName())
            .build();
        new Runner(opt).run();
    }
}
