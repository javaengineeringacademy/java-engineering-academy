package academy.javaengineering.exercises.solutions;

/**
 * Solutions: Operators (Arithmetic, Logical, Ternary)
 */
public class OperatorSolutions {

    public int dayOfYear(int month, int day, boolean isLeapYear) {
        int[] daysInMonth = {31, isLeapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int dayOfYear = day;
        for (int i = 0; i < month - 1; i++) {
            dayOfYear += daysInMonth[i];
        }
        return dayOfYear;
    }

    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    public String calculateGrade(int score) {
        return score >= 90 ? "A" : score >= 80 ? "B" : score >= 70 ? "C" : score >= 60 ? "D" : "F";
    }

    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public double calculateShipping(double weightKg, double distanceKm) {
        double cost = 5.00;
        if (weightKg > 5) cost += (weightKg - 5) * 0.50;
        if (distanceKm > 100) cost += (distanceKm - 100) * 0.10;
        return Math.round(cost * 100.0) / 100.0;
    }

    public boolean isEligible(int age, boolean hasInsurance, double income, boolean isBlacklisted) {
        return age >= 18 && age <= 65 && (hasInsurance || income > 30000) && !isBlacklisted;
    }

    public static void main(String[] args) {
        OperatorSolutions solutions = new OperatorSolutions();
        System.out.println("=== Operator Solutions ===\n");

        System.out.println("1. Day of Year (Mar 1, non-leap): " + solutions.dayOfYear(3, 1, false));
        System.out.println("2. Is 16 power of 2? " + solutions.isPowerOfTwo(16));
        System.out.println("3. Grade for 85: " + solutions.calculateGrade(85));
        System.out.println("4. Is 2000 leap year? " + solutions.isLeapYear(2000));
        System.out.println("5. Shipping (10kg, 200km): $" + solutions.calculateShipping(10, 200));
        System.out.println("6. Eligible (30, true, 40000, false): " + solutions.isEligible(30, true, 40000, false));
    }
}
