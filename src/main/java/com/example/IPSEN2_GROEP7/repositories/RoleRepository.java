package com.example.IPSEN2_GROEP7.repositories;

import com.example.IPSEN2_GROEP7.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Role r SET r.permissions = :permissions, r.description = :description, r.title = :title WHERE r.id = :id")
    void update(String permissions, String description, String title, int id);
}
