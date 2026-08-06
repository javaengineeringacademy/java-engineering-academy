package academy.javaengineering.senior.production;

/**
 * Circuit Breaker Pattern Demo
 * States: CLOSED (normal), OPEN (failing), HALF_OPEN (testing)
 */
public class CircuitBreakerDemo {

    enum State { CLOSED, OPEN, HALF_OPEN }

    static class CircuitBreaker {
        private State state = State.CLOSED;
        private int failureCount = 0;
        private final int failureThreshold;
        private final long timeoutMs;
        private long lastFailureTime = 0;

        public CircuitBreaker(int failureThreshold, long timeoutMs) {
            this.failureThreshold = failureThreshold;
            this.timeoutMs = timeoutMs;
        }

        public synchronized <T> T execute(java.util.function.Supplier<T> action,
                                           java.util.function.Supplier<T> fallback) {
            if (state == State.OPEN) {
                if (System.currentTimeMillis() - lastFailureTime > timeoutMs) {
                    state = State.HALF_OPEN;
                    System.out.println("[CircuitBreaker] Transitioning to HALF_OPEN");
                } else {
                    System.out.println("[CircuitBreaker] Circuit is OPEN, using fallback");
                    return fallback.get();
                }
            }

            try {
                T result = action.get();
                onSuccess();
                return result;
            } catch (Exception e) {
                onFailure(e);
                return fallback.get();
            }
        }

        private synchronized void onSuccess() {
            failureCount = 0;
            if (state == State.HALF_OPEN) {
                state = State.CLOSED;
                System.out.println("[CircuitBreaker] Transitioning to CLOSED (recovered)");
            }
        }

        private synchronized void onFailure(Exception e) {
            failureCount++;
            lastFailureTime = System.currentTimeMillis();
            if (failureCount >= failureThreshold) {
                state = State.OPEN;
                System.out.println("[CircuitBreaker] Transitioning to OPEN after " + failureCount + " failures");
            }
            System.out.println("[CircuitBreaker] Failure recorded: " + e.getMessage());
        }

        public State getState() { return state; }
        public int getFailureCount() { return failureCount; }
    }

    // Simulated external service
    static class ExternalService {
        private final boolean shouldFail;
        private int callCount = 0;

        public ExternalService(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        public String call() {
            callCount++;
            System.out.println("[ExternalService] Call #" + callCount);
            if (shouldFail && callCount <= 5) {
                throw new RuntimeException("Service unavailable");
            }
            return "Response from service";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Circuit Breaker Demo ===\n");

        // Config: 3 failures to open, 2s timeout
        CircuitBreaker cb = new CircuitBreaker(3, 2000);
        ExternalService service = new ExternalService(true);

        // First 3 calls will fail → circuit opens
        for (int i = 1; i <= 6; i++) {
            System.out.println("Request #" + i);
            String result = cb.execute(
                () -> service.call(),
                () -> "Fallback response"
            );
            System.out.println("Result: " + result + "\n");
        }

        // Wait for timeout → half-open
        System.out.println("Waiting 2.5s for timeout...");
        Thread.sleep(2500);

        // Now service returns success
        ExternalService recoveredService = new ExternalService(false);
        System.out.println("\nRequest (after timeout)");
        String result = cb.execute(
            () -> recoveredService.call(),
            () -> "Fallback response"
        );
        System.out.println("Result: " + result);
        System.out.println("State: " + cb.getState());
    }
}
