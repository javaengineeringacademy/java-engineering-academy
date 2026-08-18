package academy.javaengineering.concurrency.practices;

public class ThreadBasicsExercises {

    public static void main(String[] args) throws InterruptedException {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Create a thread using the Runnable interface
     * TODO: Create a Runnable that prints "Hello from Runnable" and start it in a new thread
     */
    static void exercise1() {
        System.out.println("=== Exercise 1: Runnable Interface ===");
        // TODO: Implement here
        // Hint: Use Thread constructor that accepts Runnable
    }

    /**
     * Exercise 2: Create a thread by extending the Thread class
     * TODO: Create a class that extends Thread, override run() to print "Hello from Extended Thread"
     */
    static void exercise2() {
        System.out.println("=== Exercise 2: Extending Thread Class ===");
        // TODO: Implement here
        // Hint: Create a static inner class extending Thread
    }

    /**
     * Exercise 3: Create a lambda-based thread
     * TODO: Create a thread using a lambda expression that prints "Hello from Lambda"
     */
    static void exercise3() {
        System.out.println("=== Exercise 3: Lambda Thread ===");
        // TODO: Implement here
        // Hint: Thread(() -> System.out.println("...")).start();
    }

    /**
     * Exercise 4: Multiple threads running concurrently
     * TODO: Create 3 threads, each printing its thread number (1, 2, 3).
     *       Start all three and wait for them to complete.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("=== Exercise 4: Multiple Threads ===");
        // TODO: Implement here
        // Hint: Store Thread references in an array, use join() on each
    }

    /**
     * Exercise 5: Anonymous inner class thread
     * TODO: Create a thread using an anonymous inner class that implements Runnable
     *       and prints "Hello from Anonymous Inner Class"
     */
    static void exercise5() {
        System.out.println("=== Exercise 5: Anonymous Inner Class ===");
        // TODO: Implement here
        // Hint: new Thread(new Runnable() { @Override public void run() { ... } }).start();
    }
}
