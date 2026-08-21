package academy.javaengineering.logging.structured.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Exercise 1: Configure structured logging for a REST API.
 *
 * Requirements:
 * 1. Create a filter that sets MDC context for every request
 * 2. Include: requestId, userId, method, path, clientIp
 * 3. Log request start and completion
 * 4. Include response status code in completion log
 * 5. Ensure MDC is cleaned up properly
 *
 * Expected JSON output:
 * {
 *   "timestamp": "...",
 *   "level": "INFO",
 *   "message": "Request completed",
 *   "method": "POST",
 *   "path": "/api/orders",
 *   "clientIp": "192.168.1.100",
 *   "statusCode": 201,
 *   "durationMs": 150,
 *   "requestId": "..."
 * }
 */
public class Exercise1 {

    // TODO: Create logger
    // TODO: Implement request context filter
    // TODO: Log request lifecycle
}
