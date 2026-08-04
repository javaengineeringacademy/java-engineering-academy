package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Demonstrates structured logging: key-value pairs, correlation IDs,
 * and request tracing patterns.
 */
public class StructuredLogging {

    private static final Logger logger = LoggerFactory.getLogger(StructuredLogging.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("academy.javaengineering.logging.Audit");

    public void demonstrateKeyValuePairs() {
        logger.atInfo()
                .addKeyValue("userId", "USR-123")
                .addKeyValue("action", "LOGIN")
                .addKeyValue("ip", "192.168.1.100")
                .addKeyValue("userAgent", "Mozilla/5.0")
                .log("User login event recorded");

        logger.atDebug()
                .addKeyValue("orderId", "ORD-456")
                .addKeyValue("items", 5)
                .addKeyValue("total", 299.99)
                .addKeyValue("currency", "USD")
                .log("Order created");

        logger.atWarn()
                .addKeyValue("requestId", "REQ-789")
                .addKeyValue("endpoint", "/api/users")
                .addKeyValue("duration", 5000)
                .addKeyValue("threshold", 3000)
                .log("Slow request detected");
    }

    public void demonstrateCorrelationIds() {
        String correlationId = UUID.randomUUID().toString();

        MDC.put("correlationId", correlationId);
        MDC.put("service", "order-service");
        MDC.put("version", "1.0.0");

        logger.info("Request received: POST /api/orders");
        logger.debug("Validating order payload");
        logger.info("Order created: ORD-101");

        MDC.clear();
    }

    public void demonstrateRequestTracing() {
        String traceId = UUID.randomUUID().toString();
        String spanId = UUID.randomUUID().toString().substring(0, 8);

        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);

        logger.info("HTTP GET /api/products/123");
        long dbStart = System.currentTimeMillis();
        logger.debug("SELECT * FROM products WHERE id = 123");
        long dbDuration = System.currentTimeMillis() - dbStart;
        logger.info("Database query completed in {}ms", dbDuration);

        MDC.put("parentSpanId", spanId);
        MDC.put("spanId", UUID.randomUUID().toString().substring(0, 8));
        logger.info("Calling inventory-service");
        logger.info("Response from inventory-service: 200 OK");

        MDC.clear();
    }

    public void demonstrateAuditLogging() {
        MDC.put("auditId", "AUD-" + System.currentTimeMillis());

        auditLogger.atInfo()
                .addKeyValue("action", "USER_CREATED")
                .addKeyValue("actor", "admin@example.com")
                .addKeyValue("resource", "User")
                .addKeyValue("resourceId", "USR-200")
                .addKeyValue("details", "New user account created")
                .log("Audit event");

        auditLogger.atWarn()
                .addKeyValue("action", "PERMISSION_CHANGED")
                .addKeyValue("actor", "admin@example.com")
                .addKeyValue("resource", "Role")
                .addKeyValue("resourceId", "ROLE-ADMIN")
                .addKeyValue("oldValue", "USER")
                .addKeyValue("newValue", "ADMIN")
                .log("Audit event: permission change");

        auditLogger.atInfo()
                .addKeyValue("action", "DATA_EXPORT")
                .addKeyValue("actor", "analyst@example.com")
                .addKeyValue("resource", "Report")
                .addKeyValue("recordCount", 1500)
                .addKeyValue("format", "CSV")
                .log("Audit event: data export");

        MDC.clear();
    }

    public void demonstrateErrorContext() {
        MDC.put("requestId", "REQ-ERR-" + System.currentTimeMillis());
        MDC.put("userId", "USR-42");

        try {
            throw new RuntimeException("Payment gateway timeout");
        } catch (RuntimeException e) {
            logger.atError()
                    .addKeyValue("errorCode", "PAY_504")
                    .addKeyValue("service", "payment-gateway")
                    .addKeyValue("timeout", 30000)
                    .addKeyValue("retryCount", 3)
                    .log("Payment processing failed", e);
        }

        MDC.clear();
    }

    public void demonstrateStructuredPatterns() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", "ORD-300");
        orderData.put("customer", "CUST-42");
        orderData.put("items", 3);
        orderData.put("total", 149.99);
        orderData.put("status", "CREATED");

        logger.info("Order event: {}", orderData);

        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", "USR-789");
        userData.put("email", "user@example.com");
        userData.put("plan", "PREMIUM");
        userData.put("active", true);

        logger.info("User profile: {}", userData);
    }

    public void demonstrateServiceMeshLogging() {
        MDC.put("traceId", UUID.randomUUID().toString());
        MDC.put("spanId", UUID.randomUUID().toString().substring(0, 8));
        MDC.put("service", "api-gateway");
        MDC.put("instance", "api-gateway-1");

        logger.info("Incoming request: POST /api/v1/orders");
        logger.debug("Route matched: /api/v1/orders -> order-service");

        MDC.put("service", "order-service");
        MDC.put("spanId", UUID.randomUUID().toString().substring(0, 8));
        logger.info("Processing order request");
        logger.debug("Calling inventory-service");
        logger.info("Inventory check passed");

        MDC.put("service", "inventory-service");
        MDC.put("spanId", UUID.randomUUID().toString().substring(0, 8));
        logger.info("Stock verified for {} items", 3);

        MDC.clear();
    }

    public void demonstrateMetricsLogging() {
        for (int i = 0; i < 5; i++) {
            logger.atInfo()
                    .addKeyValue("metric", "http_requests_total")
                    .addKeyValue("endpoint", "/api/users")
                    .addKeyValue("method", "GET")
                    .addKeyValue("status", 200)
                    .addKeyValue("duration", 100 + i * 10)
                    .log("Metric recorded");
        }

        logger.atInfo()
                .addKeyValue("metric", "db_query_duration")
                .addKeyValue("query", "SELECT * FROM users")
                .addKeyValue("duration", 45)
                .addKeyValue("rows", 100)
                .log("Database metric");
    }

    public static void main(String[] args) {
        StructuredLogging demo = new StructuredLogging();

        System.out.println("=== Structured Logging Demo ===");

        System.out.println("\n--- Key-Value Pairs ---");
        demo.demonstrateKeyValuePairs();

        System.out.println("\n--- Correlation IDs ---");
        demo.demonstrateCorrelationIds();

        System.out.println("\n--- Request Tracing ---");
        demo.demonstrateRequestTracing();

        System.out.println("\n--- Audit Logging ---");
        demo.demonstrateAuditLogging();

        System.out.println("\n--- Error Context ---");
        demo.demonstrateErrorContext();

        System.out.println("\n--- Structured Patterns ---");
        demo.demonstrateStructuredPatterns();

        System.out.println("\n--- Service Mesh Logging ---");
        demo.demonstrateServiceMeshLogging();

        System.out.println("\n--- Metrics Logging ---");
        demo.demonstrateMetricsLogging();
    }
}
