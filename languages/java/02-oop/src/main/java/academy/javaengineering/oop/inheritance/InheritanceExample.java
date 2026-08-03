package academy.javaengineering.oop.inheritance;

/**
 * Demonstrates Java inheritance with {@code extends}, {@code super},
 * and method overriding.
 *
 * <p>Inheritance allows a class (subclass) to inherit fields and methods
 * from another class (superclass), promoting code reuse and establishing
 * an is-a relationship.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>{@code extends} keyword for inheritance</li>
 *   <li>{@code super()} to call parent constructors</li>
 *   <li>{@code super.method()} to call overridden methods</li>
 *   <li>Method overriding with {@code @Override}</li>
 *   <li>The {@code protected} access modifier</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class InheritanceExample {

    /**
     * Base class representing a generic employee in an enterprise system.
     */
    public static class Employee {
        protected final long id;
        protected String name;
        protected double baseSalary;

        public Employee(long id, String name, double baseSalary) {
            this.id = id;
            this.name = name;
            this.baseSalary = baseSalary;
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public double getBaseSalary() { return baseSalary; }

        /**
         * Calculates total compensation. Subclasses override to add bonuses.
         *
         * @return total salary including bonuses
         */
        public double calculateCompensation() {
            return baseSalary;
        }

        /**
         * Returns the employee type. Subclasses override to specify type.
         *
         * @return employee type string
         */
        public String getEmployeeType() {
            return "EMPLOYEE";
        }

        /**
         * Generates a performance review. Each employee type has a different format.
         *
         * @return formatted review string
         */
        public String generateReview() {
            return "[%s] %s (ID: %d) - Base Salary: $%.2f".formatted(
                    getEmployeeType(), name, id, baseSalary);
        }

        @Override
        public String toString() {
            return "%s{id=%d, name='%s', salary=%.2f}".formatted(
                    getEmployeeType(), id, name, baseSalary);
        }
    }

    /**
     * Full-time employee with benefits and annual bonus.
     */
    public static class FullTimeEmployee extends Employee {
        private double annualBonus;
        private int vacationDays;

        public FullTimeEmployee(long id, String name, double baseSalary,
                                double annualBonus, int vacationDays) {
            super(id, name, baseSalary); // Call parent constructor
            this.annualBonus = annualBonus;
            this.vacationDays = vacationDays;
        }

        public double getAnnualBonus() { return annualBonus; }
        public int getVacationDays() { return vacationDays; }

        @Override
        public double calculateCompensation() {
            return super.calculateCompensation() + annualBonus; // Extend parent logic
        }

        @Override
        public String getEmployeeType() {
            return "FULL_TIME";
        }

        @Override
        public String generateReview() {
            return super.generateReview() + "\n  Benefits: %d vacation days, Bonus: $%.2f".formatted(
                    vacationDays, annualBonus);
        }
    }

    /**
     * Contractor with hourly rate and limited project assignment.
     */
    public static class Contractor extends Employee {
        private double hourlyRate;
        private int hoursPerWeek;
        private String projectCode;

        public Contractor(long id, String name, double hourlyRate,
                          int hoursPerWeek, String projectCode) {
            super(id, name, 0.0); // Contractors don't have base salary
            this.hourlyRate = hourlyRate;
            this.hoursPerWeek = hoursPerWeek;
            this.projectCode = projectCode;
        }

        public double getHourlyRate() { return hourlyRate; }
        public int getHoursPerWeek() { return hoursPerWeek; }
        public String getProjectCode() { return projectCode; }

        @Override
        public double calculateCompensation() {
            return hourlyRate * hoursPerWeek * 52; // Annual compensation
        }

        @Override
        public String getEmployeeType() {
            return "CONTRACTOR";
        }

        @Override
        public String generateReview() {
            return super.generateReview() + "\n  Project: %s, Rate: $%.2f/hr, Hours: %d/week".formatted(
                    projectCode, hourlyRate, hoursPerWeek);
        }
    }

    /**
     * Manager who oversees other employees.
     */
    public static class Manager extends FullTimeEmployee {
        private String department;

        public Manager(long id, String name, double baseSalary,
                       double annualBonus, int vacationDays, String department) {
            super(id, name, baseSalary, annualBonus, vacationDays);
            this.department = department;
        }

        public String getDepartment() { return department; }

        @Override
        public double calculateCompensation() {
            return super.calculateCompensation() * 1.15; // 15% management premium
        }

        @Override
        public String getEmployeeType() {
            return "MANAGER";
        }

        @Override
        public String generateReview() {
            return super.generateReview() + "\n  Department: %s".formatted(department);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Inheritance Demo ===\n");

        Employee emp = new Employee(1L, "John Smith", 50000);
        FullTimeEmployee fte = new FullTimeEmployee(2L, "Jane Doe", 75000, 10000, 20);
        Contractor ctr = new Contractor(3L, "Bob Wilson", 95.00, 40, "PROJ-2024");
        Manager mgr = new Manager(4L, "Carol Davis", 95000, 20000, 25, "Engineering");

        Employee[] employees = {emp, fte, ctr, mgr};

        System.out.println("Employee Reviews:");
        System.out.println("-".repeat(60));
        for (Employee e : employees) {
            System.out.println(e.generateReview());
            System.out.printf("  Annual Compensation: $%.2f%n%n", e.calculateCompensation());
        }

        // Demonstrating polymorphism through inheritance
        System.out.println("\nCompensation Summary:");
        System.out.println("-".repeat(40));
        double totalPayroll = 0;
        for (Employee e : employees) {
            double comp = e.calculateCompensation();
            totalPayroll += comp;
            System.out.printf("%-20s $%,12.2f%n", e.getName(), comp);
        }
        System.out.printf("%-20s $%,12.2f%n", "TOTAL PAYROLL", totalPayroll);
    }
}
