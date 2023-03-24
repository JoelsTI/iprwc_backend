package com.example.IPSEN2_GROEP7.dto;

import com.example.IPSEN2_GROEP7.model.Employee;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class EmployeeStoreDTO {

    @Pattern(regexp = "^[A-Za-z0-9 @.]{1,}$")
    @NotNull
    private String email;

    @Pattern(regexp = "^[A-Za-z @.]{1,}$")
    @NotNull
    private String name;

    @NotNull
    private String password;

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setName(name);
        employee.setPassword(password);
        return employee;
    }

}
