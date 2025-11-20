package com.yd.price.workflow.model;

import com.yd.price.workflow.db.entity.PriceAdjustment;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Collection;

public class ProductPrice extends BaseProductPrice {
    private Collection<PriceAdjustment> priceAdjustments;
    private BigDecimal finalPrice;

    public ProductPrice(Long productId, BigDecimal basePrice, Collection<PriceAdjustment> priceAdjustments, BigDecimal finalPrice) {
        super(productId, basePrice);
        this.priceAdjustments = priceAdjustments;
        this.finalPrice = finalPrice;
    }

    public Collection<PriceAdjustment> getPriceAdjustments() {
        return priceAdjustments;
    }

    public void setPriceAdjustments(Collection<PriceAdjustment> priceAdjustments) {
        this.priceAdjustments = priceAdjustments;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductPrice that = (ProductPrice) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(priceAdjustments, that.priceAdjustments)
                .append(finalPrice, that.finalPrice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(priceAdjustments)
                .append(finalPrice)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("priceAdjustments", priceAdjustments)
                .append("finalPrice", finalPrice)
                .toString();
    }
}
