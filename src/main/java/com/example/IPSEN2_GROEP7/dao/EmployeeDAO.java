package com.example.IPSEN2_GROEP7.dao;

import com.example.IPSEN2_GROEP7.model.Employee;
import com.example.IPSEN2_GROEP7.repositories.EmployeeRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDAO {

    private final EmployeeRepository employeeRepository;

    public EmployeeDAO(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee show(Integer id) {
        Optional<Employee> employee = this.employeeRepository.findById(id);

        return employee.orElse(null);
    }

    public List<Employee> index() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeDetails(){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeRepository.findByEmail(email).get();
    }

    public Employee store(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) {
        this.employeeRepository.update(employee.getName(),  employee.getEmail(), employee.getPassword(), employee.getId());
        return employee;
    }
}