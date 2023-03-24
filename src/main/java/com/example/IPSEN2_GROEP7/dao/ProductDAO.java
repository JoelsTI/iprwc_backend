package com.example.IPSEN2_GROEP7.dao;

import com.example.IPSEN2_GROEP7.model.Product;
import com.example.IPSEN2_GROEP7.model.Role;
import com.example.IPSEN2_GROEP7.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ProductDAO {
    private ProductRepository productRepository;

    public ProductDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ArrayList<Product> index(){
        return (ArrayList<Product>) productRepository.findAll();
    }

    public Product show(int id){
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public boolean store(Product product){
        if(product != null && productRepository.findById(product.getId()).isEmpty()){
            productRepository.save(product);
            return true;
        } else{
            return false;
        }
    }
}