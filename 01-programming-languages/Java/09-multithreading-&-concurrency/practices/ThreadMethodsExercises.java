package academy.javaengineering.concurrency.practices;

public class ThreadMethodsExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Thread start() vs run()
     * TODO: Demonstrate that calling run() directly does NOT create a new thread.
     *       Print the thread name in both cases to prove it.
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: start() vs run() ===");
        // TODO: Implement here
        // Hint: Create a thread, call run() directly, then call start() and compare thread names
    }

    /**
     * Exercise 2: Thread.join()
     * TODO: Create a thread that sleeps for 1 second, then use join() to wait for it.
     *       Print "Thread completed" after join() returns.
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Thread join() ===");
        // TODO: Implement here
        // Hint: t.join() blocks until thread t finishes
    }

    /**
     * Exercise 3: Thread.sleep()
     * TODO: Create a thread that prints "Start", sleeps for 500ms, then prints "End".
     *       The main thread should print "Waiting..." before the thread starts.
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Thread sleep() ===");
        // TODO: Implement here
        // Hint: Use Thread.sleep(500) inside the run() method
    }

    /**
     * Exercise 4: Thread.interrupt()
     * TODO: Create a thread that runs an infinite loop printing numbers.
     *       After 1 second, interrupt the thread from main.
     *       The thread should catch InterruptedException and print "Interrupted!"
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Thread interrupt() ===");
        // TODO: Implement here
        // Hint: Use t.interrupt() and handle InterruptedException in the loop
    }

    /**
     * Exercise 5: Thread.yield()
     * TODO: Create two threads with yield() calls in their loops.
     *       Print thread names to observe yielding behavior.
     *       Note: yield() is advisory and may not always have visible effect.
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Thread yield() ===");
        // TODO: Implement here
        // Hint: Call Thread.yield() inside a loop to suggest thread scheduler give others a turn
    }
}
