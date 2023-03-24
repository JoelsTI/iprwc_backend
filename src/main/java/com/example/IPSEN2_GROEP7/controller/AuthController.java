package com.example.IPSEN2_GROEP7.controller;

import com.example.IPSEN2_GROEP7.Permission;
import com.example.IPSEN2_GROEP7.dto.EmployeeStoreDTO;
import com.example.IPSEN2_GROEP7.model.Employee;
import com.example.IPSEN2_GROEP7.model.LoginCredentials;
import com.example.IPSEN2_GROEP7.model.Role;
import com.example.IPSEN2_GROEP7.repositories.EmployeeRepository;
import com.example.IPSEN2_GROEP7.security.JWTUtilization;
import com.example.IPSEN2_GROEP7.service.EncryptionService;
import com.example.IPSEN2_GROEP7.service.RoleService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private EmployeeRepository employeeRepo;
    private JWTUtilization jwtUtil;
    private AuthenticationManager authManager;
    private RoleService roleService;

    public AuthController(EmployeeRepository employeeRepo, JWTUtilization jwtUtil, AuthenticationManager authManager, PasswordEncoder passEncoder, RoleService roleService) {
        this.employeeRepo = employeeRepo;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public Map<String, Object> registrationHandler(@Valid @RequestBody EmployeeStoreDTO employeeDTO) throws Exception {
        Employee employee = employeeDTO.toEmployee();
        employee.setPassword(EncryptionService.decrypt(employee.getPassword()));

        List<Role> roles = new ArrayList<>();
        int userRoleIndex = 2;
        roles.add(this.roleService.getRoles().get(userRoleIndex));
        employee.setRoles(roles);

        employeeRepo.save(employee);
        String token = jwtUtil.generateToken(employee.getEmail());
        return Collections.singletonMap("jwt_token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
        try {
            String email = body.getEmail();
            String password = EncryptionService.decrypt(body.getPassword());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            authManager.authenticate(authToken);
            String token = jwtUtil.generateToken(body.getEmail());
            return Collections.singletonMap("jwt_token", token);
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Login credentials are invalid");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}