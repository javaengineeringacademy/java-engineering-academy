package academy.javaengineering.springhibernate.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import academy.javaengineering.springhibernate.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Employee repository with Hibernate.
 */
@Repository
public class EmployeeRepository {

    private final SessionFactory sessionFactory;

    public EmployeeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Employee> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Employee", Employee.class)
            .getResultList();
    }

    public Optional<Employee> findById(Long id) {
        Employee employee = sessionFactory.getCurrentSession().get(Employee.class, id);
        return Optional.ofNullable(employee);
    }

    public Employee save(Employee employee) {
        Session session = sessionFactory.getCurrentSession();
        if (employee.getId() == null) {
            session.persist(employee);
            return employee;
        } else {
            return session.merge(employee);
        }
    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Employee employee = session.get(Employee.class, id);
        if (employee != null) {
            session.remove(employee);
        }
    }

    public List<Employee> findByDepartmentName(String departmentName) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT e FROM Employee e WHERE e.department.name = :name", Employee.class)
            .setParameter("name", departmentName)
            .getResultList();
    }
}
