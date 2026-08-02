package academy.javaengineering.cleancode;

/**
 * Demonstrates clean code naming conventions.
 */
public class NamingConventions {

    // Bad naming
    static class Bad {
        int x;
        String s;
        void m1() { }
        boolean f(String a, int b) { return true; }
    }

    // Good naming
    static class Good {
        int userAge;
        String userName;
        void processOrder() { }
        boolean isEligibleForDiscount(String membershipLevel, int purchaseAmount) {
            return true;
        }
    }

    // Constants
    static final int MAX_RETRY_COUNT = 3;
    static final String DEFAULT_ENCODING = "UTF-8";

    // Boolean methods
    boolean isActive;
    boolean hasPermission;
    boolean canExecute;
    boolean shouldRetry;

    // Methods
    void calculateTotal() { }
    void sendNotification() { }
    String getUserName() { return ""; }
    void setUserName(String name) { }
}
