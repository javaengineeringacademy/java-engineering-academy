package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * Code Coverage Exercises
 * Practice writing tests for high coverage
 */
class CodeCoverageExercises {

    // ============================================
    // Exercise 1: Order Processor
    // ============================================

    static class OrderProcessor {
        static String processOrder(int quantity, boolean isMember, String couponCode) {
            if (quantity <= 0) return "INVALID_QUANTITY";

            double price = quantity * 10.0;

            if (isMember) {
                price *= 0.9;
            }

            if (couponCode != null) {
                if ("SAVE20".equals(couponCode)) {
                    price *= 0.8;
                } else if ("SAVE10".equals(couponCode)) {
                    price *= 0.9;
                }
            }

            if (price > 1000) {
                return "PREMIUM_ORDER:" + price;
            } else if (price > 500) {
                return "STANDARD_ORDER:" + price;
            } else {
                return "BASIC_ORDER:" + price;
            }
        }
    }

    /*
     * TODO: Write tests to achieve 100% branch coverage
     * Cover all if/else branches:
     * 1. quantity <= 0
     * 2. isMember true/false
     * 3. couponCode null, SAVE20, SAVE10
     * 4. price > 1000, > 500, else
     */

    // ============================================
    // Exercise 2: String Processor
    // ============================================

    static class StringProcessor {
        static String processString(String input, boolean toUpperCase, boolean removeSpaces) {
            if (input == null) return null;
            if (input.isEmpty()) return input;

            String result = input;

            if (toUpperCase) {
                result = result.toUpperCase();
            }

            if (removeSpaces) {
                result = result.replaceAll("\\s+", "");
            }

            return result;
        }
    }

    /*
     * TODO: Write tests to achieve 100% branch coverage
     * Cover all branches:
     * 1. input null, empty, normal
     * 2. toUpperCase true/false
     * 3. removeSpaces true/false
     */

    // ============================================
    // Exercise 3: Payment Processor
    // ============================================

    static class PaymentProcessor {
        static String processPayment(double amount, String method, boolean isInternational) {
            if (amount <= 0) return "INVALID_AMOUNT";

            double fee = 0;

            if ("CREDIT".equals(method)) {
                fee = amount * 0.029;
            } else if ("DEBIT".equals(method)) {
                fee = amount * 0.015;
            } else if ("PAYPAL".equals(method)) {
                fee = amount * 0.035;
            } else {
                return "INVALID_METHOD";
            }

            if (isInternational) {
                fee += amount * 0.01;
            }

            double total = amount + fee;
            return "SUCCESS:" + total;
        }
    }

    /*
     * TODO: Write tests to achieve 100% branch coverage
     * Cover all branches:
     * 1. amount <= 0
     * 2. method: CREDIT, DEBIT, PAYPAL, invalid
     * 3. isInternational true/false
     */

    // ============================================
    // Exercise 4: Age Verifier
    // ============================================

    static class AgeVerifier {
        static String verify(int age, boolean hasID) {
            if (age < 0) return "INVALID_AGE";

            if (age < 13) {
                return "CHILD";
            } else if (age < 18) {
                return hasID ? "TEEN_WITH_ID" : "TEEN_NO_ID";
            } else if (age < 21) {
                return hasID ? "YOUNG_ADULT" : "YOUNG_ADULT_NO_ID";
            } else {
                return hasID ? "ADULT" : "ADULT_NO_ID";
            }
        }
    }

    /*
     * TODO: Write tests to achieve 100% branch coverage
     * Cover all branches:
     * 1. age < 0
     * 2. age < 13
     * 3. age < 18 with/without ID
     * 4. age < 21 with/without ID
     * 5. age >= 21 with/without ID
     */

    // ============================================
    // Exercise 5: Discount Calculator
    // ============================================

    static class DiscountCalculator {
        static double calculateFinalPrice(double originalPrice, int discountPercent, boolean isVIP) {
            if (originalPrice <= 0) throw new IllegalArgumentException("Invalid price");
            if (discountPercent < 0 || discountPercent > 100) {
                throw new IllegalArgumentException("Invalid discount");
            }

            double discount = originalPrice * (discountPercent / 100.0);

            if (isVIP && discountPercent < 50) {
                discount += originalPrice * 0.05;
            }

            double finalPrice = originalPrice - discount;

            if (finalPrice < 0) finalPrice = 0;

            return finalPrice;
        }
    }

    /*
     * TODO: Write tests to achieve 100% branch coverage
     * Cover all branches:
     * 1. originalPrice <= 0
     * 2. discountPercent out of range
     * 3. isVIP true/false with discountPercent < 50
     * 4. finalPrice < 0
     */

    public static void main(String[] args) {
        System.out.println("=== Code Coverage Exercises ===");
        System.out.println("Write tests to achieve 100% branch coverage.");
        System.out.println("Use JaCoCo to verify coverage.");
    }
}
