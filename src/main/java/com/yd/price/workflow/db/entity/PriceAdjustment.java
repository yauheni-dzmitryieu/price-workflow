package com.yd.price.workflow.db.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceAdjustment {

    private Long id;
    private Long productId;
    private PriceAdjustmentType type;
    private BigDecimal value;
    private PriceAdjustmentMode mode;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public PriceAdjustmentType getType() {
        return type;
    }

    public void setType(PriceAdjustmentType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public PriceAdjustmentMode getMode() {
        return mode;
    }

    public void setMode(PriceAdjustmentMode mode) {
        this.mode = mode;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PriceAdjustment that = (PriceAdjustment) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(productId, that.productId)
                .append(type, that.type)
                .append(value, that.value)
                .append(mode, that.mode)
                .append(updatedAt, that.updatedAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(productId)
                .append(type)
                .append(value)
                .append(mode)
                .append(updatedAt)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("productId", productId)
                .append("type", type)
                .append("value", value)
                .append("mode", mode)
                .append("updatedAt", updatedAt)
                .toString();
    }
}
