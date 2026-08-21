package academy.javaengineering.logging.basics.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution 3: Complete notification system with proper logging.
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);

    private static final String[] FALLBACK_CHANNELS = {"email", "sms", "push"};

    public boolean sendNotification(String userId, String message, String preferredChannel) {
        logger.info("Sending notification to user={}, preferredChannel={}", userId, preferredChannel);

        String actualChannel = selectChannel(userId, preferredChannel);
        logger.debug("Selected channel={} for user={}", actualChannel, userId);

        boolean sent = attemptDelivery(userId, message, actualChannel, 1);

        if (!sent) {
            for (String fallback : FALLBACK_CHANNELS) {
                if (!fallback.equals(actualChannel)) {
                    logger.warn("Primary channel={} failed, trying fallback={}", actualChannel, fallback);
                    sent = attemptDelivery(userId, message, fallback, 1);
                    if (sent) {
                        actualChannel = fallback;
                        break;
                    }
                }
            }
        }

        if (sent) {
            logger.info("Notification delivered: user={}, channel={}", userId, actualChannel);
        } else {
            logger.error("All notification channels exhausted for user={}", userId);
        }

        return sent;
    }

    private String selectChannel(String userId, String preferredChannel) {
        if (preferredChannel != null && isValidChannel(preferredChannel)) {
            logger.debug("Using preferred channel={}", preferredChannel);
            return preferredChannel;
        }
        logger.debug("Preferred channel={} invalid, defaulting to email", preferredChannel);
        return "email";
    }

    private boolean attemptDelivery(String userId, String message, String channel, int attempt) {
        logger.debug("Delivery attempt {}: channel={}, user={}", attempt, channel, userId);

        try {
            // Simulated delivery logic
            if (channel.equals("sms") && attempt > 1) {
                throw new RuntimeException("SMS gateway timeout");
            }
            return true;
        } catch (Exception e) {
            if (attempt < 3) {
                logger.warn("Delivery failed on attempt {}: channel={}, reason={}",
                        attempt, channel, e.getMessage());
                return attemptDelivery(userId, message, channel, attempt + 1);
            } else {
                logger.error("Delivery failed after {} attempts: channel={}, user={}",
                        attempt, channel, userId, e);
                return false;
            }
        }
    }

    private boolean isValidChannel(String channel) {
        return "email".equals(channel) || "sms".equals(channel) || "push".equals(channel);
    }
}
