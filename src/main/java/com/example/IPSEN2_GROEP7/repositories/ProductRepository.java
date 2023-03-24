package com.example.IPSEN2_GROEP7.repositories;

import com.example.IPSEN2_GROEP7.model.Employee;
import com.example.IPSEN2_GROEP7.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}