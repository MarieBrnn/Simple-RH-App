package com.example.simple_crud_app;

import com.example.simple_crud_app.entity.Employee;
import com.example.simple_crud_app.repository.EmployeeRepository;
import com.example.simple_crud_app.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@test.com");
        employee.setPosition("Developer");
        employee.setDepartment("IT");
        employee.setSalary(BigDecimal.valueOf(50000));
        employee.setHireDate(LocalDate.now());
    }

    @Test
    void shouldCreateEmployee() {

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee(employee);

        assertThat(result)
                .isNotNull()
                .extracting(Employee::getFirstName, Employee::getLastName, Employee::getEmail)
                .containsExactly("John", "Doe", "john.doe@test.com");
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");

        verify(employeeRepository).findAll();
        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldGetEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldUpdateEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);  // AJOUTEZ cette ligne
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.updateEmployee(employee);

        assertThat(result).isNotNull();
        assertThat(result.getSalary()).isEqualByComparingTo("50000");
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldDeleteEmployee() {
        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

























}
