-- ==========================================
-- CONFIGURATION
-- ==========================================
DO $$
    DECLARE
        total_products      INT := 100000; --100_000
        min_adjusted_count  INT := 5000; --5_000
        max_adjusted_count  INT := 10000; -- 10_000
        batch_size          INT := 1000; -- 1_000
    BEGIN

        -- ==========================================
        -- INSERT PRODUCTS IN BATCHES OF 1000
        -- ==========================================
        FOR batch_start IN 1..total_products BY batch_size LOOP
                INSERT INTO product (sku, name, base_price)
                SELECT
                    'SKU-' || gs AS sku,
                    'Product ' || gs AS name,
                    ROUND((random() * 9999 + 1)::numeric, 2) AS base_price
                FROM generate_series(batch_start, LEAST(batch_start + batch_size - 1, total_products)) AS gs;
            END LOOP;

        RAISE NOTICE 'Inserted % products.', total_products;

        -- ==========================================
        -- RANDOM NUMBER OF PRODUCTS WITH ADJUSTMENTS
        -- ==========================================
        DECLARE
            adjusted_count INT := floor(random() * (max_adjusted_count - min_adjusted_count + 1)) + min_adjusted_count;
        BEGIN
            RAISE NOTICE 'Generating % price adjustments...', adjusted_count;

            -- Collect random product ids
            CREATE TEMP TABLE tmp_adjust_ids AS
            SELECT id
            FROM product
            ORDER BY random()
            LIMIT adjusted_count;

            -- ==========================================
            -- INSERT PRICE ADJUSTMENTS
            -- ==========================================
            INSERT INTO price_adjustment (product_id, type, value, mode)
            SELECT
                p.id,
                -- Random type: PROMO, TAX, FEE
                (ARRAY['PROMO','TAX','FEE'])[floor(random()*3)+1]::price_adjustment_type,

                -- Value calculation depending on mode
                CASE
                    WHEN mode_choice = 'ABSOLUTE' THEN
                        -- Must not exceed base_price
                        ROUND((random() * (p.base_price - 1) + 1)::numeric, 2)
                    ELSE
                        -- PERCENT: 1–99
                        floor(random() * 99 + 1)
                    END AS value,

                mode_choice::price_adjustment_mode
            FROM (
                     SELECT
                         t.id,
                         p.base_price,
                         -- Random ABSOLUTE or PERCENT
                         (ARRAY['ABSOLUTE','PERCENT'])[floor(random()*2)+1] AS mode_choice
                     FROM tmp_adjust_ids t
                              JOIN product p ON p.id = t.id
                 ) p;

            RAISE NOTICE 'Inserted price adjustments: %', adjusted_count;

            DROP TABLE tmp_adjust_ids;
        END;

    END $$;
