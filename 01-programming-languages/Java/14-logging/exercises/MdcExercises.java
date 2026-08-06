package academy.javaengineering.exercises;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Exercises: MDC Context and Structured Logging
 *
 * Complete the TODO sections below.
 */
public class MdcExercises {

    // TODO 1: Implement a ThreadLocal MDC context
    public static class Mdc {
        private static final ThreadLocal<Map<String, String>> context =
            ThreadLocal.withInitial(HashMap::new);

        public static void put(String key, String value) {
            // TODO: implement
        }

        public static String get(String key) {
            // TODO: implement
            return null;
        }

        public static void remove(String key) {
            // TODO: implement
        }

        public static Map<String, String> getCopy() {
            // TODO: implement - return defensive copy
            return new HashMap<>();
        }

        public static void clear() {
            // TODO: implement
        }
    }

    // TODO 2: Implement a structured log message builder
    public static class LogMessageBuilder {
        private String level;
        private String logger;
        private String message;
        private Map<String, String> context = new LinkedHashMap<>();
        private Throwable error;

        public LogMessageBuilder level(String level) {
            // TODO: implement
            return this;
        }

        public LogMessageBuilder logger(String logger) {
            // TODO: implement
            return this;
        }

        public LogMessageBuilder message(String message) {
            // TODO: implement
            return this;
        }

        public LogMessageBuilder context(String key, String value) {
            // TODO: implement
            return this;
        }

        public LogMessageBuilder error(Throwable error) {
            // TODO: implement
            return this;
        }

        public String build() {
            // TODO: implement - return JSON-like formatted string
            return "";
        }
    }

    // TODO 3: Implement a RequestContext that propagates through MDC
    public static class RequestContext {
        private final String requestId;
        private final String userId;
        private final long startTime;

        public RequestContext(String requestId, String userId) {
            this.requestId = requestId;
            this.userId = userId;
            this.startTime = System.currentTimeMillis();
        }

        public void attach() {
            // TODO: put all fields into MDC
        }

        public void detach() {
            // TODO: remove all fields from MDC
        }

        public long getElapsedMs() {
            return System.currentTimeMillis() - startTime;
        }

        public String getRequestId() { return requestId; }
        public String getUserId() { return userId; }
    }

    // TODO 4: Implement a MDC-aware executor that propagates context
    public static class MdcExecutorService {
        private final ExecutorService executor;

        public MdcExecutorService(ExecutorService executor) {
            this.executor = executor;
        }

        public void execute(Runnable task) {
            // TODO: capture current MDC, submit task that restores MDC
            Map<String, String> capturedContext = Mdc.getCopy();
            executor.execute(() -> {
                // TODO: set MDC from capturedContext, run task, then clear
            });
        }

        public void shutdown() {
            executor.shutdown();
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        MdcExercises exercises = new MdcExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== MdcExercises Tests ===\n");

        // Test 1
        total++;
        Mdc.put("traceId", "abc-123");
        Mdc.put("userId", "user-456");
        if ("abc-123".equals(Mdc.get("traceId")) && "user-456".equals(Mdc.get("userId"))) {
            System.out.println("Test 1a PASSED: Mdc put/get");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: Mdc put/get");
        }

        total++;
        Mdc.remove("traceId");
        if (Mdc.get("traceId") == null && "user-456".equals(Mdc.get("userId"))) {
            System.out.println("Test 1b PASSED: Mdc remove");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: Mdc remove");
        }

        total++;
        Map<String, String> copy = Mdc.getCopy();
        Mdc.clear();
        if (copy.containsKey("userId") && Mdc.get("userId") == null) {
            System.out.println("Test 1c PASSED: Mdc getCopy/clear");
            passed++;
        } else {
            System.out.println("Test 1c FAILED: Mdc getCopy/clear");
        }

        // Test 2
        total++;
        String msg = new LogMessageBuilder()
            .level("INFO")
            .logger("UserService")
            .message("User logged in")
            .context("userId", "123")
            .context("ip", "192.168.1.1")
            .build();
        if (msg.contains("INFO") && msg.contains("UserService") && msg.contains("userId")) {
            System.out.println("Test 2 PASSED: LogMessageBuilder");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: LogMessageBuilder - " + msg);
        }

        // Test 3
        total++;
        RequestContext reqCtx = new RequestContext("req-001", "user-001");
        reqCtx.attach();
        if ("req-001".equals(Mdc.get("requestId")) && "user-001".equals(Mdc.get("userId"))) {
            System.out.println("Test 3a PASSED: RequestContext attach");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: RequestContext attach");
        }

        total++;
        reqCtx.detach();
        if (Mdc.get("requestId") == null && Mdc.get("userId") == null) {
            System.out.println("Test 3b PASSED: RequestContext detach");
            passed++;
        } else {
            System.out.println("Test 3b FAILED: RequestContext detach");
        }

        // Test 4
        total++;
        Mdc.put("parentKey", "parentValue");
        MdcExecutorService mdcExecutor = new MdcExecutorService(
            Executors.newSingleThreadExecutor()
        );
        List<String> result = new ArrayList<>();
        mdcExecutor.execute(() -> {
            result.add(Mdc.get("parentKey"));
        });
        Thread.sleep(100);
        mdcExecutor.shutdown();
        if (result.size() == 1 && "parentValue".equals(result.get(0))) {
            System.out.println("Test 4 PASSED: MdcExecutorService propagation");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: MdcExecutorService propagation - " + result);
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
