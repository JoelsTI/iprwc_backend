package com.example.IPSEN2_GROEP7.controller;

import com.example.IPSEN2_GROEP7.Permission;
import com.example.IPSEN2_GROEP7.dao.EmployeeDAO;
import com.example.IPSEN2_GROEP7.dto.EmployeeUpdateDTO;
import com.example.IPSEN2_GROEP7.model.*;
import com.example.IPSEN2_GROEP7.response.types.Message;
import com.example.IPSEN2_GROEP7.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@RestController
@RequestMapping(value = "/api/user")
public class EmployeeController {

    private EmployeeDAO employeeDAO;
    private final RoleService roleService;

    public EmployeeController(EmployeeDAO employeeDAO, RoleService roleService) {
        this.employeeDAO = employeeDAO;
        this.roleService = roleService;
    }

    @GetMapping("/info")
    public Employee getInfo() {
        return employeeDAO.getEmployeeDetails();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity index() {
        List<Employee> employees = employeeDAO.index();
        if (employees.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Failed to find any employees").getResponse();
        }

        if (roleService.hasPermission(getInfo().getRoles(), Permission.ADMIN)) {
            return new ApiResponse<>(HttpStatus.OK, employees).getResponse();
        }

        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
        for (Employee employee : employees) {
            employeeInfoList.add(new EmployeeInfo(employee.getId(), employee.getName(), employee.getEmail()));
        }

        return new ApiResponse<>(HttpStatus.OK, employeeInfoList).getResponse();
    }

    @RequestMapping(value = "{employee_id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateUserRole(@PathVariable("employee_id") int employee_id, @Valid @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        // Get the orignal employee and check if its not null
        Employee originalEmployee = this.employeeDAO.show(employee_id);
        if (isNull(originalEmployee)) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, new Message("Could not find the employee")).getResponse();
        }

        if(employeeUpdateDTO.getRoles().size() == 0) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("Roles size is 0")).getResponse();
        }

        if (roleService.hasPermission(employeeUpdateDTO.getRoles(), Permission.ADMIN) && Objects.equals(employeeUpdateDTO.getName(), "root")) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("Can't disable admin account")).getResponse();
        }

        // Check if no other things about the employee account are changed
        if(!Objects.equals(employeeUpdateDTO.getEmail(), originalEmployee.getEmail()) ||
                !Objects.equals(employeeUpdateDTO.getPassword(), originalEmployee.getPassword()) ||
                !Objects.equals(employeeUpdateDTO.getName(), originalEmployee.getName())) {

            return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("The employee details do not match")).getResponse();
        }

        // Check if all the roles  of the employeeUpdateDTO exist
        List<Role> allRoles = new ArrayList<>(this.roleService.getRoles());
        for(Role role: employeeUpdateDTO.getRoles()) {
            if(!allRoles.contains(role)) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("Role " + role.getTitle() + " doesn't exist")).getResponse();
            }
        }

        // Check if all the permissions are enum values
        if(this.roleService.validatePermissions(employeeUpdateDTO.getRoles())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("One of the roles contains a permission that does not exist")).getResponse();
        }

        employeeUpdateDTO.setRoles(this.roleService.removeDoubleRoles(employeeUpdateDTO.getRoles()));

        if(this.roleService.hasPermission(employeeUpdateDTO.getRoles(), Permission.ADMIN)) {
            employeeUpdateDTO.setRoles(allRoles);
        }

        if(!this.roleService.hasPermission(employeeUpdateDTO.getRoles(), Permission.AUTHENTICATE)) {
            int userRoleIndex = this.roleService.getRoles().size() - 2;
            employeeUpdateDTO.getRoles().add(this.roleService.getRoles().get(userRoleIndex));
        }

        Employee employee = employeeUpdateDTO.toEmployee(originalEmployee, this.employeeDAO.getEmployeeDetails());

        Role disabledRole = null;
        for(Role role: employee.getRoles()) {
            List<String> permissions = Arrays.asList(role.getPermissions());
            if(permissions.contains(String.valueOf(Permission.NONE))) {
                disabledRole = role;
                break;
            }
        }
        if(disabledRole != null) {
            employee.getRoles().remove(disabledRole);
        }

        // Update the role of the employee
        employee = this.employeeDAO.update(employee);

        return new ApiResponse<>(HttpStatus.OK, employee).getResponse();
    }

    @RequestMapping(value = "{employee_id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity disableEmployee(@PathVariable("employee_id") int employee_id) {
        Employee employee = employeeDAO.show(employee_id);
        if (isNull(employee)) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, new Message("Could not find the employee")).getResponse();
        }

        if (roleService.hasPermission(employee.getRoles(), Permission.ADMIN) && Objects.equals(employee.getName(), "root")) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("Can't disable admin account")).getResponse();
        }

        List<Role> disabled = new ArrayList<>();
        disabled.add(this.roleService.getRoles().get(roleService.getRoles().size() - 1));
        employee.setRoles(disabled);

        employeeDAO.update(employee);

        return new ApiResponse<>(HttpStatus.NO_CONTENT, new Message("Employee account has been disabled")).getResponse();
    }
}