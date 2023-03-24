package com.example.IPSEN2_GROEP7.dto;

import com.example.IPSEN2_GROEP7.model.Product;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ProductStoreDTO {

    @Pattern(regexp = "^[A-Za-z]+$")
    @NotNull(message = "A title is required")
    private String name;

    @NotNull(message = "A price is required")
    private double price;

    @NotNull(message = "A description is required")
    private String description;

    public Product toProduct() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        product.setDescription(this.description);
        return product;
    }
}