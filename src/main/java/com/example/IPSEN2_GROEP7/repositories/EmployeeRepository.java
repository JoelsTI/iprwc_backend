package com.example.IPSEN2_GROEP7.repositories;

import com.example.IPSEN2_GROEP7.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    public Optional<Employee> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Employee e SET e.name = :username, e.email = :email, e.password = :password WHERE e.id = :employee_id")
    void update(String username, String email, String password, int employee_id);

}