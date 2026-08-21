package academy.javaengineering.logging.mdc.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Example: MDC integration with Servlet filter.
 */
public class MdcServletFilterExample implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(MdcServletFilterExample.class);

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("MDC Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Use existing request ID or generate new one
        String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        
        // Set MDC context
        MDC.put("requestId", requestId);
        MDC.put("method", httpRequest.getMethod());
        MDC.put("uri", httpRequest.getRequestURI());
        MDC.put("remoteAddr", httpRequest.getRemoteAddr());
        
        try {
            logger.info("Request started");
            chain.doFilter(request, response);
            logger.info("Request completed");
        } catch (Exception e) {
            logger.error("Request failed", e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {
        logger.info("MDC Filter destroyed");
    }
}
