package academy.javaengineering.springhibernate.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import academy.javaengineering.springhibernate.entity.Employee;
import academy.javaengineering.springhibernate.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

/**
 * Employee service with transaction management.
 */
@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Employee> findByDepartment(String departmentName) {
        return employeeRepository.findByDepartmentName(departmentName);
    }
}
