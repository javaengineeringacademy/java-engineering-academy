# Employee Management System — Part 2: Implementation Guide

**[← Part 1: Project Overview & Design](README.md)**

---

## Implementation Guide

### Step 1: Create Abstract Employee Class

```java
package com.academy.employee.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Employee {
    protected String employeeId;
    protected String firstName;
    protected String lastName;
    protected Department department;
    protected LocalDate hireDate;
    protected PerformanceRating rating;

    public Employee(String employeeId, String firstName, String lastName, Department department) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.hireDate = LocalDate.now();
        this.rating = PerformanceRating.STANDARD;
    }

    public abstract double calculateMonthlySalary();

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getYearsOfService() {
        return (int) ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }
}
```

### Step 2: Create Employee Subclasses

```java
package com.academy.employee.model;

public class FullTimeEmployee extends Employee {
    private double annualSalary;
    private double bonusPercentage;

    public FullTimeEmployee(String id, String firstName, String lastName, 
                           Department department, double annualSalary) {
        super(id, firstName, lastName, department);
        this.annualSalary = annualSalary;
        this.bonusPercentage = 10.0;
    }

    @Override
    public double calculateMonthlySalary() {
        double monthly = annualSalary / 12;
        double bonus = monthly * (bonusPercentage / 100);
        return monthly + bonus;
    }
}

public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursPerWeek;

    public PartTimeEmployee(String id, String firstName, String lastName,
                           Department department, double hourlyRate, int hoursPerWeek) {
        super(id, firstName, lastName, department);
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
    }

    @Override
    public double calculateMonthlySalary() {
        return hourlyRate * hoursPerWeek * 4;
    }
}

public class Contractor extends Employee {
    private double dailyRate;
    private int contractDays;
    private LocalDate contractEndDate;

    @Override
    public double calculateMonthlySalary() {
        return dailyRate * 22;
    }
}
```

### Step 3: Implement Strategy Pattern

```java
package com.academy.employee.strategy;

public interface SalaryCalculator {
    double calculateSalary(Employee employee);
}

package com.academy.employee.strategy;

public class FullTimeSalaryStrategy implements SalaryCalculator {
    @Override
    public double calculateSalary(Employee employee) {
        FullTimeEmployee fte = (FullTimeEmployee) employee;
        return fte.getAnnualSalary() / 12;
    }
}

package com.academy.employee.service;

import com.academy.employee.strategy.*;

public class PayrollService {
    private Map<EmployeeType, SalaryCalculator> strategies;

    public PayrollService() {
        strategies = new EnumMap<>(EmployeeType.class);
        strategies.put(EmployeeType.FULL_TIME, new FullTimeSalaryStrategy());
        strategies.put(EmployeeType.PART_TIME, new PartTimeSalaryStrategy());
        strategies.put(EmployeeType.CONTRACTOR, new ContractorSalaryStrategy());
    }

    public double calculatePayroll(Employee employee) {
        SalaryCalculator calculator = strategies.get(employee.getType());
        return calculator.calculateSalary(employee);
    }

    public List<PayrollRecord> processMonthlyPayroll(List<Employee> employees) {
        return employees.stream()
            .map(e -> new PayrollRecord(e, calculatePayroll(e)))
            .collect(Collectors.toList());
    }
}
```

### Step 4: Implement Report Generation

```java
package com.academy.employee.report;

public class ReportGenerator {
    
    public String generateDepartmentReport(Department department) {
        StringBuilder report = new StringBuilder();
        report.append("=== Department Report ===\n");
        report.append("Department: ").append(department.getName()).append("\n");
        report.append("Employee Count: ").append(department.getEmployeeCount()).append("\n");
        report.append("Total Salary: $").append(String.format("%.2f", department.getTotalSalary())).append("\n\n");
        
        for (Employee e : department.getEmployees()) {
            report.append(String.format("%-20s $%,.2f\n", e.getFullName(), e.calculateMonthlySalary()));
        }
        
        return report.toString();
    }
}
```

## Unit Tests

```java
package com.academy.employee;

import com.academy.employee.model.*;
import com.academy.employee.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {
    private EmployeeService service;
    private Department department;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();
        department = new Department("D001", "Engineering");
    }

    @Test
    void testAddFullTimeEmployee() {
        FullTimeEmployee emp = new FullTimeEmployee("E001", "John", "Doe", department, 75000);
        assertTrue(service.addEmployee(emp));
        assertEquals(75000, emp.getAnnualSalary());
    }

    @Test
    void testCalculateFullTimeSalary() {
        FullTimeEmployee emp = new FullTimeEmployee("E001", "John", "Doe", department, 72000);
        assertEquals(6000, emp.calculateMonthlySalary(), 0.01);
    }

    @Test
    void testCalculatePartTimeSalary() {
        PartTimeEmployee emp = new PartTimeEmployee("E002", "Jane", "Doe", department, 25, 20);
        assertEquals(2000, emp.calculateMonthlySalary(), 0.01);
    }

    @Test
    void testDepartmentTotalSalary() {
        department.addEmployee(new FullTimeEmployee("E001", "John", "Doe", department, 72000));
        department.addEmployee(new PartTimeEmployee("E002", "Jane", "Doe", department, 25, 20));
        assertEquals(8000, department.getTotalSalary(), 0.01);
    }

    @Test
    void testSearchByDepartment() {
        service.addEmployee(new FullTimeEmployee("E001", "John", "Doe", department, 72000));
        List<Employee> results = service.searchByDepartment("Engineering");
        assertEquals(1, results.size());
    }
}
```

## Extension Challenges

1. **Performance Bonuses**: Calculate bonuses based on performance ratings
2. **Salary History**: Track salary changes over time for each employee
3. **CSV Import/Export**: Import employees from CSV and export reports
4. **Organizational Chart**: Generate visual org chart representation
5. **Leave Management**: Add vacation days and sick leave tracking

## Interview Questions

1. **Why did you use the Strategy pattern for salary calculation?**
   - Discuss open/closed principle, easy addition of new employee types

2. **How would you redesign this to support multiple companies?**
   - Discuss composition over inheritance, multi-tenancy

3. **What are the benefits of using an abstract Employee class?**
   - Discuss polymorphism, code reuse, enforcing contract

4. **How would you implement salary history tracking?**
   - Discuss audit trail, Observer pattern, event sourcing

5. **How would you optimize for a company with 100,000+ employees?**
   - Discuss pagination, lazy loading, database optimization

## References

- [Java Inheritance Tutorial](https://docs.oracle.com/en/java/javase/21/java/IandI/subclasses.html)
- [Design Patterns - Strategy](https://www.baeldung.com/java-strategy-pattern)