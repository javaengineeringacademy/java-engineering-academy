package academy.javaengineering.logging.structured.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Example: Structured logging for microservices with trace propagation.
 */
public class MicroserviceTraceExample {

    private static final Logger logger = LoggerFactory.getLogger(MicroserviceTraceExample.class);

    public static void main(String[] args) {
        String traceId = UUID.randomUUID().toString();
        String spanId = UUID.randomUUID().toString();

        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);
        MDC.put("service", "api-gateway");
        MDC.put("environment", "production");

        try {
            logger.info("Incoming request from mobile app");
            callOrderService(traceId);
            logger.info("Request completed");
        } finally {
            MDC.clear();
        }
    }

    private static void callOrderService(String parentTraceId) {
        String childSpanId = UUID.randomUUID().toString();
        MDC.put("spanId", childSpanId);
        MDC.put("parentSpanId", parentTraceId);
        MDC.put("service", "order-service");

        logger.info("Calling order service");
        logger.debug("Processing order");

        MDC.put("spanId", parentTraceId);
        MDC.remove("parentSpanId");
    }
}
