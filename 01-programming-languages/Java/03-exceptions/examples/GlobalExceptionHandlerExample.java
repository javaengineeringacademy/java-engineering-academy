package academy.javaengineering.exceptions.examples;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Global exception handler example with Thread.UncaughtExceptionHandler and logging.
 * Demonstrates production-grade exception handling and logging patterns.
 */
public class GlobalExceptionHandlerExample {

    // Simple logging utility
    static class Logger {
        private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        
        public static void log(String level, String message) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            System.out.printf("[%s] %s: %s%n", timestamp, level, message);
        }
        
        public static void logError(String message, Throwable throwable) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            System.out.printf("[%s] ERROR: %s%n", timestamp, message);
            if (throwable != null) {
                StringWriter sw = new StringWriter();
                throwable.printStackTrace(new PrintWriter(sw));
                System.out.println(sw.toString());
            }
        }
        
        public static void logToFile(String message, Throwable throwable) {
            // In production, this would write to a log file
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String logEntry = String.format("[%s] FATAL: %s%n%s%n", 
                timestamp, message, 
                throwable != null ? throwable.getMessage() : "No stack trace");
            
            try (PrintWriter writer = new PrintWriter(new FileWriter("app.log", true))) {
                writer.println(logEntry);
            } catch (IOException e) {
                System.err.println("Failed to write to log file: " + e.getMessage());
            }
        }
    }
    
    // Custom UncaughtExceptionHandler
    static class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            String threadName = t != null ? t.getName() : "Unknown";
            
            // Log the exception
            Logger.logError("Uncaught exception in thread: " + threadName, e);
            
            // In production: send to monitoring system, alert, etc.
            Logger.logToFile("Thread " + threadName + " died with exception", e);
            
            // Optionally restart the thread or perform cleanup
            if (e instanceof OutOfMemoryError) {
                Logger.log("FATAL", "OutOfMemoryError detected, initiating shutdown");
                System.exit(1);
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Global Exception Handler Example ===");
        
        // Set default uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        
        // Example 1: Thread with exception
        testThreadExceptionHandler();
        
        // Example 2: Executor service with exception handling
        testExecutorServiceExceptionHandler();
        
        // Example 3: Main thread exception handling
        testMainThreadExceptionHandling();
        
        System.out.println("=== End of Global Exception Handler Example ===");
    }
    
    private static void testThreadExceptionHandler() {
        System.out.println("\n--- Thread Exception Handler ---");
        
        Thread workerThread = new Thread(() -> {
            Logger.log("INFO", "Worker thread starting");
            
            // Simulate work that throws exception
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // This will throw RuntimeException and be caught by handler
            throw new RuntimeException("Worker thread failed");
        }, "WorkerThread");
        
        workerThread.start();
        
        try {
            workerThread.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Logger.log("INFO", "Main thread continues after worker thread exception");
    }
    
    private static void testExecutorServiceExceptionHandler() {
        System.out.println("\n--- Executor Service Exception Handler ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Submit tasks that will throw exceptions
        executor.submit(() -> {
            Logger.log("INFO", "Task 1 starting");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Task 1 failed");
        });
        
        executor.submit(() -> {
            Logger.log("INFO", "Task 2 starting");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Task 2 failed");
        });
        
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Logger.log("INFO", "Executor service shutdown complete");
    }
    
    private static void testMainThreadExceptionHandling() {
        System.out.println("\n--- Main Thread Exception Handling ---");
        
        try {
            // Simulate operation in main thread
            performMainOperation();
            
        } catch (Exception e) {
            Logger.logError("Main thread exception caught", e);
            
            // In production: send alert, cleanup resources, etc.
            Logger.log("ALERT", "Sending notification to monitoring system");
        }
        
        // Program continues
        Logger.log("INFO", "Program completed successfully");
    }
    
    private static void performMainOperation() {
        // Simulate operation that might fail
        if (Math.random() > 0.5) {
            throw new RuntimeException("Main operation failed");
        }
        Logger.log("INFO", "Main operation completed");
    }
}