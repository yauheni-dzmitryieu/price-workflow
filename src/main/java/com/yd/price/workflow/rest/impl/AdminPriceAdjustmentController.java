package com.yd.price.workflow.rest.impl;

import com.yd.price.workflow.persistence.entity.PriceAdjustment;
import com.yd.price.workflow.rest.api.AdminPriceAdjustmentApi;
import com.yd.price.workflow.rest.dto.PriceAdjustmentRequest;
import com.yd.price.workflow.rest.dto.PriceAdjustmentResponse;
import com.yd.price.workflow.service.api.PriceAdjustmentService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/products")
public class AdminPriceAdjustmentController implements AdminPriceAdjustmentApi {
    private final Converter<PriceAdjustmentRequest, PriceAdjustment> converterToEntity;
    private final PriceAdjustmentService service;

    public AdminPriceAdjustmentController(Converter<PriceAdjustmentRequest, PriceAdjustment> converterToEntity,
                                          PriceAdjustmentService service) {
        Assert.notNull(converterToEntity, "converterToEntity must not be null");
        Assert.notNull(service, "service must not be null");

        this.converterToEntity = converterToEntity;
        this.service = service;
    }

    @Override
    @PostMapping("/{productId}/adjustments")
    public ResponseEntity<PriceAdjustmentResponse> addAdjustment(
            @PathVariable Long productId,
            @Validated
            @RequestBody PriceAdjustmentRequest priceAdjustmentRequest) {
        PriceAdjustment entity = converterToEntity.convert(priceAdjustmentRequest);
        service.addToProduct(productId, entity);
        return ResponseEntity.ok(null);
    }
}
