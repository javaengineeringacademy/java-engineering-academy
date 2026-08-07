import java.time.LocalDateTime;

/**
 * StockMovement entity tracking inventory changes.
 * Records all stock movements for audit and history.
 */
public class StockMovement {
    public enum MovementType {
        IN, OUT, TRANSFER
    }

    private final String id;
    private final String productId;
    private final MovementType type;
    private final int quantity;
    private final String reason;
    private final LocalDateTime timestamp;
    private final String destinationProductId; // For transfers

    public StockMovement(String id, String productId, MovementType type, 
                         int quantity, String reason) {
        this(id, productId, type, quantity, reason, null);
    }

    public StockMovement(String id, String productId, MovementType type,
                         int quantity, String reason, String destinationProductId) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
        this.destinationProductId = destinationProductId;
    }

    // Getters
    public String getId() { return id; }
    public String getProductId() { return productId; }
    public MovementType getType() { return type; }
    public int getQuantity() { return quantity; }
    public String getReason() { return reason; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDestinationProductId() { return destinationProductId; }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", type=" + type +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}