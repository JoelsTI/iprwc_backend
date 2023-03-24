package com.example.IPSEN2_GROEP7.security;

import com.example.IPSEN2_GROEP7.model.Employee;
import com.example.IPSEN2_GROEP7.repositories.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Optional;

@Component
public class EmployeeDetailsService implements UserDetailsService {

    private EmployeeRepository employeeRepo;

    public EmployeeDetailsService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> employeeResult = employeeRepo.findByEmail(email);
        if(employeeResult.isEmpty())
            throw new UsernameNotFoundException("Could not find employee with the following email: " + email);
        Employee employee = employeeResult.get();
        return new org.springframework.security.core.userdetails.User(
                email,
                employee.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

