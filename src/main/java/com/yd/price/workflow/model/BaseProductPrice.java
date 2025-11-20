package com.yd.price.workflow.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class BaseProductPrice {
    private Long productId;
    private BigDecimal basePrice;

    public BaseProductPrice(Long productId, BigDecimal basePrice) {
        this.productId = productId;
        this.basePrice = basePrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseProductPrice that = (BaseProductPrice) o;

        return new EqualsBuilder()
                .append(productId, that.productId)
                .append(basePrice, that.basePrice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(productId)
                .append(basePrice)
                .toHashCode();
    }
}
