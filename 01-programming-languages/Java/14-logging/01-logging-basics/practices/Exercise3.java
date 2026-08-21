package academy.javaengineering.logging.basics.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 3: Implement a proper logging strategy for a notification system.
 *
 * Requirements:
 * 1. Create a well-structured Logger
 * 2. Implement sendNotification with appropriate logging at each stage
 * 3. Handle multiple notification channels (email, SMS, push)
 * 4. Log channel selection, delivery attempts, successes, and failures
 * 5. Include correlation information in log messages
 *
 * Deliverables:
 * - Complete the sendNotification method
 * - Add logging for each decision point
 * - Log retries with attempt counts
 * - Log final outcome (success or all channels failed)
 */
public class Exercise3 {

    // TODO: Create Logger

    /**
     * Sends a notification through the best available channel.
     *
     * Flow:
     * 1. Determine preferred channel for user
     * 2. Try preferred channel first
     * 3. If fails, try fallback channels in order
     * 4. Log each attempt and result
     */
    public boolean sendNotification(String userId, String message, String preferredChannel) {
        // TODO: Implement with proper logging
        //
        // Log levels to use:
        // - DEBUG: Channel selection logic, retry attempts
        // - INFO: Notification sent successfully
        // - WARN: Channel failed, falling back to alternative
        // - ERROR: All channels exhausted
        //
        // Context to include in logs:
        // - userId
        // - preferredChannel
        // - actualChannel used
        // - attempt number
        // - failure reason

        String[] channels = {"email", "sms", "push"};
        boolean sent = false;

        for (String channel : channels) {
            try {
                // TODO: Log attempt
                // TODO: Try to send
                // TODO: Log success
                sent = true;
                break;
            } catch (Exception e) {
                // TODO: Log failure with reason
            }
        }

        if (!sent) {
            // TODO: Log all channels failed
        }

        return sent;
    }
}
