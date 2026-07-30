# Sprint 2 Assignment: Employee Management System

## 📋 Assignment Overview

Design and implement an **Employee Management System** demonstrating mastery of Sprint 2 OOP concepts.

**Due:** End of Sprint 2  
**Weight:** 100 points  
**Submission:** Push to your fork, create PR to main repo

---

## 🎯 Requirements

### Functional Requirements

1. **Abstract Base Class: `Employee`**
   - Fields: `employeeId` (String), `name` (String), `department` (Department)
   - Abstract method: `calculateSalary(): BigDecimal`
   - Proper `equals()`, `hashCode()`, `toString()` based on `employeeId`
   - Constructor with validation

2. **Concrete Subclasses**
   - `FullTimeEmployee`: monthly salary
   - `PartTimeEmployee`: hourly rate × hours per month
   - `Contractor`: project fee
   - Each with proper constructors and validation

3. **Department Class**
   - Manages collection of employees (composition)
   - Methods: `addEmployee()`, `removeEmployee()`, `getEmployee()`, `getAllEmployees()`
   - `totalPayroll()`: sum of all employee salaries
   - `getEmployeesByType()`: filter by employee type

3. **Interface: `Payable`**
   - `pay(BigDecimal amount)` method
   - Default method `printReceipt()`
   - Static method `calculateTax(BigDecimal amount)` (18% GST)

4. **Payment Implementations**
   - `SalaryPayment`: pays employee salary
   - `BonusPayment`: pays bonus amount
   - `ReimbursementPayment`: pays reimbursement

5. **Payroll Service**
   - Uses `Payable` strategy for payments
   - Processes payroll for entire department
   - Generates payroll report

### Error Handling
- Custom exceptions: `EmployeeNotFoundException`, `InvalidSalaryException`, `PayrollProcessingException`
- Input validation with meaningful error messages
- Proper exception hierarchy

---

## 🏗️ Technical Requirements

### Code Structure
```
src/main/java/com/javaacademy/sprint2/assignment/
├── model/
│   ├── Employee.java
│   ├── FullTimeEmployee.java
│   ├── PartTimeEmployee.java
│   ├── Contractor.java
│   └── Department.java
├── payment/
│   ├── Payable.java
│   ├── SalaryPayment.java
│   ├── BonusPayment.java
│   └── ReimbursementPayment.java
├── service/
│   └── PayrollService.java
├── exception/
│   ├── EmployeeNotFoundException.java
│   ├── InvalidSalaryException.java
│   └── PayrollProcessingException.java
└── PayrollApp.java          # Main class with demo
```

### Mandatory Features
- [ ] **Abstract class** with abstract method
- [ ] **Inheritance** hierarchy (3+ subclasses)
- [ ] **Interface** with default & static methods
- [ ] **Polymorphism** (runtime method dispatch)
- [ ] **Composition** (Department has Employees)
- [ ] **Interface** for payment strategy
- [ ] **Custom exceptions** with proper hierarchy
- [ ] **Javadoc** on all public classes/methods
- [ ] **Java 21** features (records, pattern matching if applicable)
- [ ] **Google Java Style** (Checkstyle passes)

### Testing Requirements
- [ ] **JUnit 5** tests in `src/test/...`
- [ ] Minimum **85% code coverage**
- [ ] Test all salary calculations
- [ ] Test department operations
- [ ] Test payment processing
- [ ] Test exception scenarios
- [ ] Test polymorphic behavior

### Quality Gates (Must Pass)
```bash
mvn clean verify -Pci
```
- ✅ Checkstyle (Google style)
- ✅ SpotBugs (Max effort, Low threshold)
- ✅ PMD (Custom ruleset)
- ✅ All tests pass
- ✅ Javadoc generates without warnings

---

## 📊 Evaluation Rubric (100 Points)

| Criteria | Points | Details |
|----------|--------|---------|
| **OOP Design Correctness** | 35 | Proper inheritance, polymorphism, encapsulation, composition |
| **Code Quality** | 25 | Clean architecture, SOLID, naming, Javadoc |
| **Test Coverage** | 20 | 85%+ coverage, meaningful assertions |
| **Documentation** | 10 | README, Javadoc, inline comments |
| **Git Hygiene** | 10 | Logical commits, meaningful messages, no merge commits |

### Deductions
- -15: Checkstyle violations
- -15: SpotBugs findings
- -15: PMD violations
- -20: Tests fail
- -30: Doesn't compile
- -50: Plagiarism / AI-generated without understanding

---

## 🚀 Getting Started

```bash
# 1. Create feature branch
git checkout -b assignment/employee-management

# 2. Implement in oop-fundamentals/assignment/

# 3. Run tests locally
cd oop-fundamentals
mvn test

# 4. Run quality checks
mvn checkstyle:check spotbugs:check pmd:check

# 5. Commit frequently with meaningful messages
git add .
git commit -m "feat: add Employee abstract class with salary calculation"
git commit -m "feat: implement FullTimeEmployee and PartTimeEmployee"
git commit -m "feat: add Department with composition"

# 6. Push and create PR
git push origin assignment/employee-management
# Create PR to main branch
```

---

## 📚 Reference Implementation Hints

### Employee Abstract Class
```java
public abstract class Employee {
    private final String employeeId;
    private final String name;
    private final Department department;

    protected Employee(String employeeId, String name, Department department) {
        this.employeeId = Objects.requireNonNull(employeeId);
        this.name = Objects.requireNonNull(name);
        this.department = Objects.requireNonNull(department);
    }

    public abstract BigDecimal calculateSalary();

    // equals/hashCode based on employeeId
    // toString with all fields
}
```

### Department with Composition
```java
public final class Department {
    private final String name;
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) { ... }
    public void removeEmployee(String employeeId) { ... }
    public Optional<Employee> getEmployee(String employeeId) { ... }
    public List<Employee> getAllEmployees() { return List.copyOf(employees); }
    public BigDecimal totalPayroll() { ... }
}
```

### Payable Interface
```java
public interface Payable {
    void pay(BigDecimal amount);

    default void printReceipt() { ... }

    static BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
    }
}
```

---

## 📝 Submission Checklist

- [ ] Code compiles: `mvn compile`
- [ ] Tests pass: `mvn test`
- [ ] Quality gates: `mvn verify -Pci`
- [ ] Javadoc generates: `mvn javadoc:javadoc`
- [ ] README.md with build/run instructions
- [ ] Meaningful commit history (5+ commits)
- [ ] PR created with description
- [ ] Self-review completed (check rubric)

---

## 💡 Tips for Success

1. **Start with tests** - Write salary calculation tests first
2. **Use records** - For immutable data (Money, Address)
3. **Enum for employee types** - `EmployeeType.FULL_TIME`, `PART_TIME`, `CONTRACTOR`
4. **Validate early** - In constructors, fail fast
5. **Immutable where possible** - Use `final`, records, defensive copies
6. **Run checks often** - `mvn checkstyle:check` during development

---

## 🎓 Learning Outcomes

After completing this assignment, you will have demonstrated:
- Abstract class design with proper abstraction
- Inheritance hierarchy with 3+ concrete subclasses
- Runtime polymorphism via method overriding
- Interface design with default/static methods
- Composition over inheritance (Department-Employee)
- Strategy pattern for payment processing
- Custom exception hierarchy
- Unit testing with JUnit 5 (85%+ coverage)
- Code quality tools (Checkstyle, SpotBugs, PMD)
- Git workflow & PR process

---

**Good luck!** 🎯 This assignment consolidates everything from Sprint 2.