package academy.javaengineering.springjms.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Order message for JMS examples.
 */
public class OrderMessage implements Serializable {

    private String orderId;
    private String customerId;
    private double amount;
    private LocalDateTime timestamp;
    private String status;

    public OrderMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public OrderMessage(String orderId, String customerId, double amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = "CREATED";
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("OrderMessage{orderId='%s', customer='%s', amount=%.2f, status='%s'}",
            orderId, customerId, amount, status);
    }
}
