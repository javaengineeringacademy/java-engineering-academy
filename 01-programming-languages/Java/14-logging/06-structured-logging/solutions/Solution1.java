package academy.javaengineering.logging.structured.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Solution 1: REST API request context filter.
 */
public class Solution1 {

    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    public static class RequestContextFilter implements Filter {

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                throws IOException, ServletException {
            
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            String requestId = UUID.randomUUID().toString();
            long startTime = System.currentTimeMillis();

            MDC.put("requestId", requestId);
            MDC.put("method", request.getMethod());
            MDC.put("path", request.getRequestURI());
            MDC.put("clientIp", request.getRemoteAddr());
            MDC.put("userAgent", request.getHeader("User-Agent"));

            logger.info("Request started");

            try {
                chain.doFilter(req, res);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                MDC.put("statusCode", String.valueOf(response.getStatus()));
                MDC.put("durationMs", String.valueOf(duration));
                logger.info("Request completed");
                MDC.clear();
            }
        }
    }
}
