package com.yd.price.workflow.rest.impl;

import com.yd.price.workflow.model.ProductPrice;
import com.yd.price.workflow.rest.api.ProductApi;
import com.yd.price.workflow.service.api.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {

    @Autowired
    private ProductService productService;

    @Override
    @GetMapping("api/v1/products/{productId}/price")
    public ResponseEntity<ProductPrice> getPrice(@PathVariable(name = "productId") Long productId) {
        return ResponseEntity.ok(productService.getProductPrice(productId));
    }
}
