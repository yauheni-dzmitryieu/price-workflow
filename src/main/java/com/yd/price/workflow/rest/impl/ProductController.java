package com.yd.price.workflow.rest.impl;

import com.yd.price.workflow.db.repository.ProductMapper;
import com.yd.price.workflow.rest.api.ProductApi;
import com.yd.price.workflow.rest.dto.ProductPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @GetMapping("api/v1/products/{productId}/price")
    public ResponseEntity<ProductPriceResponse> getPrice(@PathVariable Long productId) {
        return ResponseEntity.ok(null);
    }
}
