package academy.javaengineering.microservices;

import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreakerExample {

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private State state = State.CLOSED;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final int failureThreshold;
    private final int successThreshold;
    private final long waitDurationMs;
    private long lastFailureTime = 0;

    public CircuitBreakerExample(int failureThreshold, int successThreshold, long waitDurationMs) {
        this.failureThreshold = failureThreshold;
        this.successThreshold = successThreshold;
        this.waitDurationMs = waitDurationMs;
    }

    public <T> T execute(ServiceCall<T> serviceCall, Fallback<T> fallback) {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime > waitDurationMs) {
                state = State.HALF_OPEN;
                System.out.println("Circuit breaker: OPEN -> HALF_OPEN");
            } else {
                System.out.println("Circuit breaker: OPEN, using fallback");
                return fallback.execute();
            }
        }

        try {
            T result = serviceCall.execute();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure(e);
            return fallback.execute();
        }
    }

    private void onSuccess() {
        if (state == State.HALF_OPEN) {
            if (successCount.incrementAndGet() >= successThreshold) {
                state = State.CLOSED;
                failureCount.set(0);
                successCount.set(0);
                System.out.println("Circuit breaker: HALF_OPEN -> CLOSED");
            }
        } else {
            failureCount.set(0);
        }
    }

    private void onFailure(Exception e) {
        lastFailureTime = System.currentTimeMillis();
        if (failureCount.incrementAndGet() >= failureThreshold) {
            state = State.OPEN;
            System.out.println("Circuit breaker: OPEN (failures: " + failureCount.get() + ")");
        }
    }

    public State getState() { return state; }

    public interface ServiceCall<T> {
        T execute() throws Exception;
    }

    public interface Fallback<T> {
        T execute();
    }

    public static void main(String[] args) {
        CircuitBreakerExample cb = new CircuitBreakerExample(3, 2, 5000);

        System.out.println("=== Circuit Breaker Demo ===\n");

        for (int i = 0; i < 10; i++) {
            int requestNum = i + 1;
            System.out.print("Request " + requestNum + ": ");

            String result = cb.execute(
                    () -> {
                        if (Math.random() < 0.5) {
                            throw new RuntimeException("Service failure");
                        }
                        return "Success";
                    },
                    () -> "Fallback response"
            );

            System.out.println("Result: " + result + " | State: " + cb.getState());
        }
    }
}
