package com.example.IPSEN2_GROEP7.controller;

import com.example.IPSEN2_GROEP7.dao.ProductDAO;
import com.example.IPSEN2_GROEP7.dto.ProductStoreDTO;
import com.example.IPSEN2_GROEP7.model.ApiResponse;
import com.example.IPSEN2_GROEP7.model.Product;
import com.example.IPSEN2_GROEP7.response.types.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("api/products")
public class ProductController {
    private ProductDAO productDao;

    public ProductController(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity index(){
        ArrayList<Product> list = productDao.index();
        if(list.size() > 0){
            return new ApiResponse(HttpStatus.OK, list).getResponse();
        }

        return new ApiResponse(HttpStatus.NOT_FOUND, new Message("There are currently no Products")).getResponse();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity store(@Valid @RequestBody @NotNull ProductStoreDTO productStoreDTO) {
        Product createdProduct = productStoreDTO.toProduct();

        List<Product> products = this.productDao.index();

        for(Product product: products) {
            if(Objects.equals(product.getName(), createdProduct.getName()) ) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST, new Message("Name already exists")).getResponse();
            }
        }
        this.productDao.store(createdProduct);
        return new ApiResponse<>(HttpStatus.CREATED, createdProduct).getResponse();
    }



}
