package academy.javaengineering.patterns.enterprise.object_pool;

/**
 * Simulated expensive database connection resource.
 * Real connections involve network handshakes and authentication.
 */
public class DatabaseConnection {

    private static int nextId = 1;

    private final int id;
    private boolean active;
    private long createdAt;

    public DatabaseConnection() {
        this.id = nextId++;
        this.createdAt = System.currentTimeMillis();
        this.active = true;
        simulateExpensiveCreation();
    }

    private void simulateExpensiveCreation() {
        // Simulate expensive connection setup
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void execute(String query) {
        if (!active) {
            throw new IllegalStateException("Connection " + id + " is not active");
        }
        System.out.println("[Connection-" + id + "] Executing: " + query);
    }

    public void close() {
        active = false;
        System.out.println("[Connection-" + id + "] Closed");
    }

    public void reset() {
        active = true;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "DatabaseConnection{id=" + id + ", active=" + active + "}";
    }
}
