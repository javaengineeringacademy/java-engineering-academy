package academy.javaengineering.concurrency.practices;

public class MemoryModelExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Visibility problem
     * TODO: Demonstrate that without synchronization, a thread may not see
     *       changes made by another thread due to CPU caching.
     *       Create a shared boolean flag that one thread sets and another checks.
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Visibility Problem ===");
        // TODO: Implement here
        // Hint: One thread sets flag=true in a loop, other thread loops while !flag
    }

    /**
     * Exercise 2: volatile keyword
     * TODO: Fix the visibility problem from Exercise 1 using volatile.
     *       Mark the shared boolean as volatile and observe correct behavior.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: volatile Keyword ===");
        // TODO: Implement here
        // Hint: volatile boolean flag ensures visibility across threads
    }

    /**
     * Exercise 3: Happens-before relationship
     * TODO: Demonstrate happens-before between synchronized blocks.
     *       One thread writes to a variable inside synchronized,
     *       another reads inside synchronized - should see the update.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Happens-Before ===");
        // TODO: Implement here
        // Hint: synchronized(this) { write } happens-before synchronized(this) { read }
    }

    /**
     * Exercise 4: Instruction reordering
     * TODO: Demonstrate that without proper synchronization, instructions
     *       may be reordered by compiler or CPU, causing unexpected behavior.
     *       Use volatile to prevent reordering.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Instruction Reordering ===");
        // TODO: Implement here
        // Hint: Without volatile, non-volatile reads/writes can be reordered
    }

    /**
     * Exercise 5: double-checked locking
     * TODO: Implement double-checked locking singleton pattern.
     *       Demonstrate why volatile is essential for the instance field.
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Double-Checked Locking ===");
        // TODO: Implement here
        // Hint: private static volatile Singleton instance;
    }
}
