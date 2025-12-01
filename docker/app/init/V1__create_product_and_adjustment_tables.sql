CREATE TYPE price_adjustment_type AS ENUM ('PROMO', 'TAX', 'FEE');
CREATE TYPE price_adjustment_mode AS ENUM ('ABSOLUTE', 'PERCENT');


CREATE TABLE IF NOT EXISTS product
(
    id         BIGSERIAL PRIMARY KEY,
    sku        VARCHAR(64) UNIQUE NOT NULL,
    name       VARCHAR(255)       NOT NULL,
    base_price NUMERIC(19, 2)     NOT NULL
);

-- Create PriceAdjustment table
CREATE TABLE IF NOT EXISTS price_adjustment
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT                NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    type       price_adjustment_type NOT NULL,
    value      NUMERIC(19, 2)        NOT NULL,
    mode       price_adjustment_mode NOT NULL,
    updated_at TIMESTAMP             NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_price_adjustment_product ON price_adjustment (product_id);