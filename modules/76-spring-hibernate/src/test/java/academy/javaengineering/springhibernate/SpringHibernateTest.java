package academy.javaengineering.springhibernate;

import academy.javaengineering.springhibernate.entity.Employee;
import academy.javaengineering.springhibernate.entity.Department;
import academy.javaengineering.springhibernate.repository.EmployeeRepository;
import academy.javaengineering.springhibernate.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spring + Hibernate Tests")
class SpringHibernateTest {

    @Test
    @DisplayName("Employee entity should be created correctly")
    void testEmployeeCreation() {
        Employee employee = new Employee("John", "john@example.com", 75000);
        
        assertEquals("John", employee.getName());
        assertEquals("john@example.com", employee.getEmail());
        assertEquals(75000, employee.getSalary(), 0.01);
    }

    @Test
    @DisplayName("Department entity should manage employees")
    void testDepartmentEmployees() {
        Department dept = new Department("Engineering");
        Employee emp1 = new Employee("Alice", "alice@example.com", 85000);
        Employee emp2 = new Employee("Bob", "bob@example.com", 90000);
        
        dept.addEmployee(emp1);
        dept.addEmployee(emp2);
        
        assertEquals(2, dept.getEmployees().size());
        assertEquals(dept, emp1.getDepartment());
        assertEquals(dept, emp2.getDepartment());
    }

    @Test
    @DisplayName("BaseEntity should track timestamps")
    void testBaseEntity() {
        BaseEntity entity = new BaseEntity() {};
        entity.prePersist();
        
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
    }
}
