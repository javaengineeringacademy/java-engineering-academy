package academy.javaengineering.concurrency.framework.forkjoin.solutions;

import java.util.concurrent.*;

public class ForkJoinSolutions {
    static class ParallelSum extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10000;
        private final long[] array;
        private final int start, end;

        ParallelSum(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) sum += array[i];
                return sum;
            }
            int mid = (start + end) / 2;
            ParallelSum left = new ParallelSum(array, start, mid);
            ParallelSum right = new ParallelSum(array, mid, end);
            left.fork();
            return right.compute() + left.join();
        }
    }

    public static void main(String[] args) throws Exception {
        long[] data = new long[1_000_000];
        for (int i = 0; i < data.length; i++) data[i] = i + 1;

        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new ParallelSum(data, 0, data.length));
        System.out.println("Parallel sum: " + result);
        System.out.println("Expected: " + (1_000_000L * 1_000_001L / 2));

        pool.shutdown();
    }
}
