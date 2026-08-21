package academy.javaengineering.logging.structured.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Solution 3: JSON log entry builder.
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);

    public static class LogEntry {
        private final Map<String, Object> fields = new LinkedHashMap<>();

        public LogEntry level(String level) {
            fields.put("level", level);
            return this;
        }

        public LogEntry message(String message) {
            fields.put("message", message);
            return this;
        }

        public LogEntry logger(String logger) {
            fields.put("logger", logger);
            return this;
        }

        public LogEntry timestamp(long timestamp) {
            fields.put("timestamp", timestamp);
            return this;
        }

        public LogEntry field(String key, Object value) {
            fields.put(key, value);
            return this;
        }

        public LogEntry exception(Throwable t) {
            if (t != null) {
                fields.put("exception_class", t.getClass().getName());
                fields.put("exception_message", t.getMessage());
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement e : t.getStackTrace()) {
                    sb.append(e.toString()).append("\n");
                }
                fields.put("stack_trace", sb.toString());
            }
            return this;
        }

        public String toJson() {
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(escape(entry.getKey())).append("\":");
                Object value = entry.getValue();
                if (value instanceof String) {
                    json.append("\"").append(escape((String) value)).append("\"");
                } else {
                    json.append(value);
                }
                first = false;
            }
            json.append("}");
            return json.toString();
        }

        private String escape(String s) {
            return s.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }

    public static void main(String[] args) {
        LogEntry entry = new LogEntry()
            .level("INFO")
            .message("Order processed")
            .logger("OrderService")
            .field("orderId", "ORD-123")
            .field("total", 99.99)
            .field("items", 5);

        logger.info(entry.toJson());

        LogEntry errorEntry = new LogEntry()
            .level("ERROR")
            .message("Payment failed")
            .logger("PaymentService")
            .field("paymentId", "PAY-456")
            .exception(new RuntimeException("Card declined"));

        logger.info(errorEntry.toJson());
    }
}
