package academy.javaengineering.exceptions.custom;

/**
 * Solutions for custom exception exercises.
 */
public class CustomExceptionSolutions {

    // ================================================================
    // Exercise 1: Checked Exception
    // ================================================================

    public static class FileProcessingException extends Exception {

        private static final long serialVersionUID = 1L;
        private final String filePath;

        public FileProcessingException(String filePath) {
            super("Error processing file: " + filePath);
            this.filePath = filePath;
        }

        public FileProcessingException(String filePath, String message) {
            super(message);
            this.filePath = filePath;
        }

        public FileProcessingException(
                String filePath, Throwable cause) {
            super("Error processing file: " + filePath, cause);
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    // ================================================================
    // Exercise 2: Unchecked Exception
    // ================================================================

    public static class RateLimitExceededException extends RuntimeException {

        private static final long serialVersionUID = 1L;
        private final String clientId;
        private final int retryAfterSeconds;

        public RateLimitExceededException(String clientId, int retryAfterSeconds) {
            super(String.format(
                "Rate limit exceeded for client %s. Retry after %d seconds",
                clientId, retryAfterSeconds));
            this.clientId = clientId;
            this.retryAfterSeconds = retryAfterSeconds;
        }

        public String getClientId() {
            return clientId;
        }

        public int getRetryAfterSeconds() {
            return retryAfterSeconds;
        }
    }

    // ================================================================
    // Exercise 3: Exception with Factory Method
    // ================================================================

    public static class OrderException extends RuntimeException {

        private static final long serialVersionUID = 1L;
        private final String orderId;
        private final String errorCode;

        private OrderException(String orderId, String errorCode, String message) {
            super(message);
            this.orderId = orderId;
            this.errorCode = errorCode;
        }

        public static OrderException notFound(String orderId) {
            return new OrderException(
                orderId, "ORDER_NOT_FOUND",
                "Order not found: " + orderId);
        }

        public static OrderException cannotCancel(String orderId) {
            return new OrderException(
                orderId, "ORDER_CANNOT_CANCEL",
                "Order cannot be cancelled: " + orderId);
        }

        public String getOrderId() {
            return orderId;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

    // ================================================================
    // Exercise 4: Domain Exception Hierarchy
    // ================================================================

    public static class ServiceException extends RuntimeException {

        private static final long serialVersionUID = 1L;
        private final String serviceName;
        private final String operationName;

        protected ServiceException(
                String serviceName, String operationName, String message) {
            super(message);
            this.serviceName = serviceName;
            this.operationName = operationName;
        }

        protected ServiceException(
                String serviceName, String operationName,
                String message, Throwable cause) {
            super(message, cause);
            this.serviceName = serviceName;
            this.operationName = operationName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getOperationName() {
            return operationName;
        }
    }

    public static class TimeoutException extends ServiceException {

        private static final long serialVersionUID = 1L;
        private final long timeoutMillis;

        public TimeoutException(
                String serviceName, String operationName, long timeoutMillis) {
            super(serviceName, operationName,
                  String.format("Timeout in %s.%s after %dms",
                                serviceName, operationName, timeoutMillis));
            this.timeoutMillis = timeoutMillis;
        }

        public long getTimeoutMillis() {
            return timeoutMillis;
        }
    }

    public static class ConnectionException extends ServiceException {

        private static final long serialVersionUID = 1L;
        private final String endpointUrl;

        public ConnectionException(
                String serviceName, String operationName, String endpointUrl) {
            super(serviceName, operationName,
                  String.format("Connection failed in %s.%s to %s",
                                serviceName, operationName, endpointUrl));
            this.endpointUrl = endpointUrl;
        }

        public String getEndpointUrl() {
            return endpointUrl;
        }
    }

    // ================================================================
    // Exercise 5: Builder Pattern
    // ================================================================

    public static class InventoryException extends RuntimeException {

        private static final long serialVersionUID = 1L;
        private final String productId;
        private final String warehouseId;
        private final int requestedQuantity;
        private final int availableQuantity;
        private final String errorCode;

        private InventoryException(
                String productId, String warehouseId,
                int requestedQuantity, int availableQuantity,
                String errorCode, String message) {
            super(message);
            this.productId = productId;
            this.warehouseId = warehouseId;
            this.requestedQuantity = requestedQuantity;
            this.availableQuantity = availableQuantity;
            this.errorCode = errorCode;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String productId;
            private String warehouseId;
            private int requestedQuantity;
            private int availableQuantity;
            private String errorCode;

            public Builder productId(String productId) {
                this.productId = productId;
                return this;
            }

            public Builder warehouseId(String warehouseId) {
                this.warehouseId = warehouseId;
                return this;
            }

            public Builder requestedQuantity(int requestedQuantity) {
                this.requestedQuantity = requestedQuantity;
                return this;
            }

            public Builder availableQuantity(int availableQuantity) {
                this.availableQuantity = availableQuantity;
                return this;
            }

            public Builder errorCode(String errorCode) {
                this.errorCode = errorCode;
                return this;
            }

            public InventoryException build() {
                String message = String.format(
                    "Inventory insufficient for product %s in warehouse %s: "
                    + "requested %d, available %d",
                    productId, warehouseId,
                    requestedQuantity, availableQuantity);
                return new InventoryException(
                    productId, warehouseId,
                    requestedQuantity, availableQuantity,
                    errorCode, message);
            }
        }

        public String getProductId() {
            return productId;
        }

        public String getWarehouseId() {
            return warehouseId;
        }

        public int getRequestedQuantity() {
            return requestedQuantity;
        }

        public int getAvailableQuantity() {
            return availableQuantity;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

    // ================================================================
    // Demo
    // ================================================================

    public static void main(String[] args) {
        System.out.println("=== Custom Exception Solutions ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: Checked Exception ---");
        try {
            throw new FileProcessingException(
                "/data/file.csv", "File not found");
        } catch (FileProcessingException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("File: %s%n", e.getFilePath());
        }

        // Exercise 2
        System.out.println("\n--- Exercise 2: Rate Limit ---");
        try {
            throw new RateLimitExceededException("client-123", 30);
        } catch (RateLimitExceededException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Retry after: %d seconds%n",
                              e.getRetryAfterSeconds());
        }

        // Exercise 3
        System.out.println("\n--- Exercise 3: Order Exception ---");
        try {
            throw OrderException.notFound("ORD-999");
        } catch (OrderException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Code: %s, Order: %s%n",
                              e.getErrorCode(), e.getOrderId());
        }

        // Exercise 4
        System.out.println("\n--- Exercise 4: Service Hierarchy ---");
        try {
            throw new TimeoutException("PaymentService", "charge", 5000);
        } catch (ServiceException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Service: %s, Op: %s%n",
                              e.getServiceName(), e.getOperationName());
        }

        // Exercise 5
        System.out.println("\n--- Exercise 5: Builder ---");
        try {
            throw InventoryException.builder()
                .productId("PROD-001")
                .warehouseId("WH-EAST")
                .requestedQuantity(100)
                .availableQuantity(25)
                .errorCode("INV_INSUFFICIENT")
                .build();
        } catch (InventoryException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.out.printf("Deficit: %d%n",
                e.getRequestedQuantity() - e.getAvailableQuantity());
        }
    }
}
