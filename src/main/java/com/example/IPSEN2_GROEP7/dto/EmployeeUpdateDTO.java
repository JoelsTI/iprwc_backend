package com.example.IPSEN2_GROEP7.dto;

import com.example.IPSEN2_GROEP7.Permission;
import com.example.IPSEN2_GROEP7.model.Employee;
import com.example.IPSEN2_GROEP7.model.Role;
import com.example.IPSEN2_GROEP7.service.RoleService;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class EmployeeUpdateDTO {



    @JsonCreator
    public EmployeeUpdateDTO() {

    }

    @Pattern(regexp = "^[A-Za-z1-9 @.]{1,}$")
    @NotNull(message = "The email attribute is required")
    private String email;

    @Pattern(regexp = "^[A-Za-z @.]{1,}$")
    @NotNull(message = "The name attribute is required")
    private String name;

    @NotNull(message = "The password attribute is required")
    private String password;

    private List<Role> roles;

    public Employee toEmployee(Employee employeeToUpdate, Employee requestEmployee) {
        employeeToUpdate.setEmail(this.email);
        employeeToUpdate.setName(this.name);
        employeeToUpdate.setPassword(this.password);

        AtomicBoolean isAdmin = new AtomicBoolean(false);
        requestEmployee.getRoles().forEach(role -> {
            if(Arrays.asList(role.getPermissions()).contains(String.valueOf(Permission.ADMIN))) {
                isAdmin.set(true);
            }
        });

        if(isAdmin.get()) {
            employeeToUpdate.setRoles(this.roles);
        }

        return employeeToUpdate;
    }
}
