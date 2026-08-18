# Fork-Join Framework - Quiz

## Multiple Choice Questions

### 1. What scheduling strategy does ForkJoinPool use?
A) Round-robin
B) Work-stealing
C) FIFO
D) Priority-based

**Answer: B** - Idle threads steal tasks from busy threads' deques.

### 2. Which class represents a fork-join task that returns a value?
A) RecursiveAction
B) RecursiveTask
C) CompletableFuture
D) Callable

**Answer: B** - RecursiveTask<V> has a compute() method returning V.

### 3. What method does ForkJoinPool use internally to manage blocking?
A) wait()
B) managedBlock()
C) park()
D) sleep()

**Answer: B** - managedBlock() maintains parallelism when tasks block.

### 4. What is the default parallelism of the common ForkJoinPool?
A) 1
B) Number of CPU cores
C) Number of available processors
D) Both B and C

**Answer: D** - Runtime.getRuntime().availableProcessors() determines default parallelism.

### 5. Which method splits work in a RecursiveTask?
A) execute()
B) invoke()
C) fork()
D) submit()

**Answer: C** - fork() submits a subtask for async execution.

## True/False

### 6. A RecursiveAction has a compute() method that returns void.
**Answer: True** - RecursiveAction extends ForkJoinTask<Void> with void compute().

### 7. ForkJoinPool is ideal for IO-bound tasks that block frequently.
**Answer: False** - ForkJoinPool is designed for CPU-bound, non-blocking computation. Use ExecutorService for IO-bound work.

### 8. You must join() every forked task to avoid resource leaks.
**Answer: True** - Forking without joining can lead to orphaned tasks and pool exhaustion.

## Code Output

### 9. What does this compute?
```java
class SumTask extends RecursiveTask<Integer> {
    int[] arr; int lo, hi;
    SumTask(int[] a, int l, int h) { arr=a; lo=l; hi=h; }
    protected Integer compute() {
        if (hi - lo <= 2) {
            int s = 0;
            for (int i=lo; i<hi; i++) s += arr[i];
            return s;
        }
        int mid = (lo+hi)/2;
        SumTask left = new SumTask(arr, lo, mid);
        SumTask right = new SumTask(arr, mid, hi);
        left.fork();
        int r = right.compute();
        int l = left.join();
        return l + r;
    }
}
ForkJoinPool pool = new ForkJoinPool();
int[] data = {1, 2, 3, 4, 5};
System.out.println(pool.invoke(new SumTask(data, 0, 5)));
```
**Answer:** `15` - Sum of 1+2+3+4+5 = 15 using divide-and-conquer.

### 10. What is the output?
```java
ForkJoinPool pool = ForkJoinPool.commonPool();
System.out.println(pool.getParallelism());
```
**Answer:** Number of available processors (e.g., `8` on an 8-core machine).
