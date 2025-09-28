package com.example.simple_crud_app;

import com.example.simple_crud_app.repository.EmployeeRepository;
import com.example.simple_crud_app.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldFindEmployeeByEmail(){
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@test.com");
        employee.setPosition("Developer");
        employee.setDepartment("IT");
        employee.setSalary(BigDecimal.valueOf(50000));
        employee.setHireDate(LocalDate.now());

        entityManager.persist(employee);
        entityManager.flush();

        Optional<Employee> found = employeeRepository.findByEmail("john.doe@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        Employee employee = new Employee();
        employee.setEmail("test@example.com");
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setPosition("Tester");
        employee.setDepartment("QA");
        employee.setSalary(BigDecimal.valueOf(45000));
        employee.setHireDate(LocalDate.now());

        entityManager.persist(employee);
        entityManager.flush();

        boolean exists = employeeRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }
}
