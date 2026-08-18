package academy.javaengineering.concurrency.communication.waitnotify.practices;

/**
 * Practice exercises for wait/notify.
 * Complete each exercise by implementing the TODO sections.
 */
public class Practices {

    /**
     * Exercise 1: Simple Signal
     * Thread A should wait until Thread B sets a flag to true,
     * then print "Received".
     */
    public static void exercise1() {
        System.out.println("=== Exercise 1: Simple Signal ===");
        Object lock = new Object();
        boolean[] flag = {false};

        // TODO: Create Thread A that waits on lock while flag[0] is false,
        //       then prints "Received"

        // TODO: Create Thread B that sleeps 500ms, sets flag[0]=true, notifies lock

        // TODO: Start both threads and join

        System.out.println();
    }

    /**
     * Exercise 2: Ping-Pong
     * Two threads alternate printing "Ping" and "Pong" 5 times each.
     */
    public static void exercise2() {
        System.out.println("=== Exercise 2: Ping-Pong ===");
        Object lock = new Object();
        boolean[] isPingTurn = {true};

        // TODO: Create Ping thread that prints "Ping" 5 times, alternating with Pong
        //       Use wait/notify to synchronize turns

        // TODO: Create Pong thread that prints "Pong" 5 times

        // TODO: Start both threads and join

        System.out.println();
    }

    /**
     * Exercise 3: Countdown Latch
     * Implement a simple latch: 3 threads count down, main thread waits
     * until all 3 have counted down, then prints "All done!"
     */
    public static void exercise3() {
        System.out.println("=== Exercise 3: Countdown Latch ===");
        Object lock = new Object();
        int[] count = {3};

        // TODO: Create 3 worker threads that decrement count and notify

        // TODO: Main thread waits while count > 0, then prints "All done!"

        System.out.println();
    }

    /**
     * Exercise 4: Producer-Consumer with single item
     * Producer creates items, consumer takes them.
     * Use a shared single-slot buffer with wait/notify.
     */
    public static void exercise4() {
        System.out.println("=== Exercise 4: Single-Slot Producer-Consumer ===");
        Object lock = new Object();
        int[] buffer = {0};
        boolean[] hasItem = {false};

        // TODO: Producer produces 5 items (values 1-5), waits if hasItem is true
        //       Sets hasItem=true and notifies after producing

        // TODO: Consumer takes 5 items, waits if hasItem is false
        //       Sets hasItem=false and notifies after consuming

        // TODO: Start both threads and join

        System.out.println();
    }

    /**
     * Exercise 5: Thread Termination
     * Implement a worker thread that can be gracefully stopped using wait/notify.
     */
    public static void exercise5() {
        System.out.println("=== Exercise 5: Graceful Thread Termination ===");
        Object lock = new Object();
        boolean[] terminate = {false};

        // TODO: Create worker thread that loops, does work (print "Working..."),
        //       checks terminate flag with while loop, waits on lock
        //       Sleep 300ms between iterations

        // TODO: After 2 seconds, set terminate=true and notify

        // TODO: Start worker, sleep 2s, signal termination, join

        System.out.println();
    }

    public static void main(String[] args) {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
        System.out.println("Complete the exercises in Practices.java and check solutions/");
    }
}
