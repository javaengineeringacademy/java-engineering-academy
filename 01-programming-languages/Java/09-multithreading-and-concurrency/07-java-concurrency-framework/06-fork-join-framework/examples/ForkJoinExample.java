package academy.javaengineering.concurrency.framework.forkjoin;

import java.util.concurrent.*;

public class ForkJoinExample {
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 1000;
        private final long[] array;
        private final int start, end;

        SumTask(long[] array, int start, int end) {
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
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();
            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) throws Exception {
        long[] data = new long[100000];
        for (int i = 0; i < data.length; i++) data[i] = i + 1;

        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SumTask(data, 0, data.length));
        System.out.println("Sum: " + result);
        System.out.println("Expected: " + (100000L * 100001L / 2));

        pool.shutdown();
    }
}
